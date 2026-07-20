package com.ogoma.marketing.core.domain.audience;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public record RuleSet(
        RuleGroup ruleGroup
) {


    public enum Condition {
        AND, OR
    }

    public record Rule(
            String column,
            Operator operator,
            Object value
    ) implements Node {
    }

    public record RuleGroup(
            Condition condition,
            List<Node> rules
    ) implements Node {
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


    public enum Operator {
        EQUAL,
        LESS_THAN,
        GREATER_THAN,
        LESS_THAN_OR_EQUAL,
        CONTAINS,
        DOES_NOT_CONTAIN,
        CONTAINS_IGNORE_CASE,
        IS_NULL,
        IS_NOT_NULL,
        GREATER_THAN_OR_EQUAL,
        ENDS_WITH,
        DOES_NOT_END_WITH,
        STARTS_WITH,
        DOES_NOT_START_WITH,
        IN,
        NOT_IN,
        IS_EMPTY,
        IS_NOT_EMPTY,
        BETWEEN,
        NOT_BETWEEN
    }
}
