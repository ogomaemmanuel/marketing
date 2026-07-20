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

import java.util.List;

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
}
