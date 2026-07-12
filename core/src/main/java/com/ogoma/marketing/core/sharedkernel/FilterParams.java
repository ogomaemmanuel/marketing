package com.ogoma.marketing.core.sharedkernel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FilterParams {

    private FilterGroup rootGroup; // The root logic block
    private String sortBy;
    private String sortDirection = "ASC";
    private int page = 1;
    private int size = 10;




    @Getter
    @Setter
    public class FilterGroup{
        private GroupOperator operator = GroupOperator.AND; // AND / OR
        private List<SearchCriteria> rules = new ArrayList<>(); // Flat field rules inside this block
        private List<FilterGroup> groups = new ArrayList<>(); // Nested logic subgroups inside this block

        public enum GroupOperator {
            AND, OR
        }

    }
    public enum GroupOperator {
        AND, OR
    }

    public enum Operator {
        // Core Comparison
        EQUALS, NOT_EQUALS,
        GREATER_THAN, GREATER_THAN_OR_EQUAL,
        LESS_THAN, LESS_THAN_OR_EQUAL,

        // String Pattern Matching
        LIKE, LIKE_IGNORE_CASE, STARTS_WITH, ENDS_WITH, NOT_LIKE,

        // Collections & Ranges
        IN, NOT_IN, BETWEEN,

        // Null Checks
        IS_NULL, IS_NOT_NULL
    }

   public record SearchCriteria(
            String field,

            Object value,
            Operator operator
    ) {

    }
}
