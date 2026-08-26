package com.ogoma.marketing.core.domain.audience;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public record RuleSet(
        @NotNull
        @Valid
        RuleGroup ruleGroup
) {


    public record Rule(

            @NotBlank
            String column,
            @NotNull
            Operator operator,
            @Valid
            @NotNull
            Value value
    ) implements Node {
        public Rule {
            CustomAssert.hasText(column, () -> new IllegalArgumentException("Column is required"));
            CustomAssert.notNull(operator, () -> new IllegalArgumentException("Operator must not be null"));
            CustomAssert.isTrue(operator.accepts(value), () -> new IllegalArgumentException(operator + " does not accept value type "));

        }
    }

    public record RuleGroup(
            @NotNull
            Condition condition,
            @NotNull
            @NotEmpty
            @Valid
            List<Node> rules
    ) implements Node {

        public enum Condition {
            AND, OR
        }

        public RuleGroup {
            CustomAssert.notNull(condition, () -> new IllegalArgumentException("Condition must not be null"));
            rules = rules == null ? List.of() : List.copyOf(rules);
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RuleGroup.class),
            @JsonSubTypes.Type(value = Rule.class)
    })
    public sealed interface Node permits Rule, RuleGroup {
    }

    public record SingleValue(String value) implements Value {
        public SingleValue {
            CustomAssert.notNull(value, () -> new IllegalArgumentException("SingleValue cannot contain null"));
        }
    }

    public record RangeValue(String min, String max) implements Value {
        public RangeValue {
            CustomAssert.isTrue(min != null && max != null, () -> new IllegalArgumentException("RangeValue requires both min and max"));
        }
    }

    public record ListValue(List<String> values) implements Value {
        public ListValue {
            CustomAssert.notEmpty(values, () -> new IllegalArgumentException("ListValue requires at least one value"));
            values = List.copyOf(values);
        }
    }


    public record NoValue() implements Value {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SingleValue.class),
            @JsonSubTypes.Type(value = RangeValue.class),
            @JsonSubTypes.Type(value = ListValue.class),
            @JsonSubTypes.Type(value = NoValue.class)
    })

    public sealed interface Value permits SingleValue,
            RangeValue,
            ListValue, NoValue {

    }


    public enum Operator {
        EQUAL(Shape.SINGLE),
        LESS_THAN(Shape.SINGLE),
        GREATER_THAN(Shape.SINGLE),
        LESS_THAN_OR_EQUAL(Shape.SINGLE),
        CONTAINS(Shape.SINGLE),
        DOES_NOT_CONTAIN(Shape.SINGLE),
        CONTAINS_IGNORE_CASE(Shape.SINGLE),
        IS_NULL(Shape.NONE),
        IS_NOT_NULL(Shape.NONE),
        GREATER_THAN_OR_EQUAL(Shape.SINGLE),
        ENDS_WITH(Shape.SINGLE),
        DOES_NOT_END_WITH(Shape.SINGLE),
        STARTS_WITH(Shape.SINGLE),
        DOES_NOT_START_WITH(Shape.SINGLE),
        IN(Shape.LIST),
        NOT_IN(Shape.LIST),
        IS_EMPTY(Shape.NONE),
        IS_NOT_EMPTY(Shape.NONE),
        BETWEEN(Shape.RANGE),
        NOT_BETWEEN(Shape.RANGE);

        Operator(Shape shape) {
            this.shape = shape;
        }

        private enum Shape {SINGLE, RANGE, LIST, NONE}

        private final Shape shape;

        public boolean accepts(Value value) {
            return switch (shape) {
                case SINGLE -> value instanceof SingleValue;
                case RANGE -> value instanceof RangeValue;
                case LIST -> value instanceof ListValue;
                case NONE -> value instanceof NoValue;
            };
        }
    }
    public record SqlSegment(String sql, Map<String, Object> params) {
        public SqlSegment {
            params = params == null ? Map.of() : Map.copyOf(params);
        }
    }

    public SqlSegment toNamedSQL(String prefix) {
        CustomAssert.hasText(prefix, () -> new IllegalArgumentException("Prefix is required"));
        CustomAssert.isTrue(prefix.matches("[A-Za-z_][A-Za-z0-9_]*"),
                () -> new IllegalArgumentException("Prefix must be a valid identifier fragment: " + prefix));
        return toSql(ruleGroup(), prefix, new AtomicInteger(0));
    }

    private static SqlSegment toSql(Node node, String prefix, AtomicInteger counter) {
        return switch (node) {
            case Rule r -> ruleToSql(r, prefix, counter);
            case RuleGroup g -> groupToSql(g, prefix, counter);
        };
    }

    private static SqlSegment groupToSql(RuleGroup group, String prefix, AtomicInteger counter) {
        String joiner = group.condition() == RuleGroup.Condition.AND ? " AND " : " OR ";

        List<SqlSegment> segments = group.rules().stream()
                .map(n -> toSql(n, prefix, counter))
                .toList();

        String sql = segments.stream()
                .map(SqlSegment::sql)
                .collect(Collectors.joining(joiner, "(", ")"));

        Map<String, Object> params = new LinkedHashMap<>();
        segments.forEach(s -> params.putAll(s.params()));

        return new SqlSegment(sql, params);
    }

    private static SqlSegment ruleToSql(Rule rule, String prefix, AtomicInteger counter) {
        String column = escapeIdentifier(rule.column());
        Value value = rule.value();

        return switch (rule.operator()) {
            case EQUAL -> singleParamSegment(column + " = :%s", singleValue(value), prefix, counter);
            case LESS_THAN -> singleParamSegment(column + " < :%s", singleValue(value), prefix, counter);
            case GREATER_THAN -> singleParamSegment(column + " > :%s", singleValue(value), prefix, counter);
            case LESS_THAN_OR_EQUAL -> singleParamSegment(column + " <= :%s", singleValue(value), prefix, counter);
            case GREATER_THAN_OR_EQUAL -> singleParamSegment(column + " >= :%s", singleValue(value), prefix, counter);
            case CONTAINS -> singleParamSegment(column + " LIKE :%s", "%" + singleValue(value) + "%", prefix, counter);
            case DOES_NOT_CONTAIN -> singleParamSegment(column + " NOT LIKE :%s", "%" + singleValue(value) + "%", prefix, counter);
            case CONTAINS_IGNORE_CASE ->
                    singleParamSegment("LOWER(" + column + ") LIKE LOWER(:%s)", "%" + singleValue(value) + "%", prefix, counter);
            case STARTS_WITH -> singleParamSegment(column + " LIKE :%s", singleValue(value) + "%", prefix, counter);
            case DOES_NOT_START_WITH -> singleParamSegment(column + " NOT LIKE :%s", singleValue(value) + "%", prefix, counter);
            case ENDS_WITH -> singleParamSegment(column + " LIKE :%s", "%" + singleValue(value), prefix, counter);
            case DOES_NOT_END_WITH -> singleParamSegment(column + " NOT LIKE :%s", "%" + singleValue(value), prefix, counter);
            case IS_NULL -> new SqlSegment(column + " IS NULL", Map.of());
            case IS_NOT_NULL -> new SqlSegment(column + " IS NOT NULL", Map.of());
            case IS_EMPTY -> new SqlSegment("(" + column + " IS NULL OR " + column + " = '')", Map.of());
            case IS_NOT_EMPTY -> new SqlSegment("(" + column + " IS NOT NULL AND " + column + " <> '')", Map.of());
            case IN -> listParamSegment(column, "IN", value, prefix, counter);
            case NOT_IN -> listParamSegment(column, "NOT IN", value, prefix, counter);
            case BETWEEN -> rangeParamSegment(column, "BETWEEN", value, prefix, counter);
            case NOT_BETWEEN -> rangeParamSegment(column, "NOT BETWEEN", value, prefix, counter);
        };
    }

    private static SqlSegment singleParamSegment(String sqlTemplate, String paramValue, String prefix, AtomicInteger counter) {
        String name = nextParamName(prefix, counter);
        return new SqlSegment(sqlTemplate.formatted(name), Map.of(name, paramValue));
    }

    private static SqlSegment listParamSegment(String column, String keyword, Value value, String prefix, AtomicInteger counter) {
        List<String> values = listValues(value);
        Map<String, Object> params = new LinkedHashMap<>();
        String placeholders = values.stream()
                .map(v -> {
                    String name = nextParamName(prefix, counter);
                    params.put(name, v);
                    return ":" + name;
                })
                .collect(Collectors.joining(", "));
        return new SqlSegment(column + " " + keyword + " (" + placeholders + ")", params);
    }

    private static SqlSegment rangeParamSegment(String column, String keyword, Value value, String prefix, AtomicInteger counter) {
        RangeValue range = rangeValue(value);
        String minName = nextParamName(prefix, counter);
        String maxName = nextParamName(prefix, counter);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(minName, range.min());
        params.put(maxName, range.max());
        return new SqlSegment(column + " " + keyword + " :" + minName + " AND :" + maxName, params);
    }

    private static String nextParamName(String prefix, AtomicInteger counter) {
        return prefix + counter.incrementAndGet();
    }

    private static String singleValue(Value value) {
        CustomAssert.isTrue(value instanceof SingleValue, () -> new IllegalStateException("Expected SingleValue"));
        return ((SingleValue) value).value();
    }

    private static RangeValue rangeValue(Value value) {
        CustomAssert.isTrue(value instanceof RangeValue, () -> new IllegalStateException("Expected RangeValue"));
        return (RangeValue) value;
    }

    private static List<String> listValues(Value value) {
        CustomAssert.isTrue(value instanceof ListValue, () -> new IllegalStateException("Expected ListValue"));
        return ((ListValue) value).values();
    }

    private static String escapeIdentifier(String column) {
        if (!column.matches("[A-Za-z0-9_.]+")) {
            throw new IllegalArgumentException("Invalid column name: " + column);
        }
        return column;
    }
}
