package com.ogoma.marketing.infrastructure.querybuilder;

import com.ogoma.marketing.core.sharedkernel.FilterParams;
import com.ogoma.marketing.core.sharedkernel.Range;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.ogoma.marketing.core.sharedkernel.FilterParams.Operator.*;

public class RelationalTreeQueryBuilder {

    public static Query buildQuery(FilterParams params, Pageable pageable) {
        if (params == null || params.getRootGroup() == null) {
            return Query.query(Criteria.empty()).with(pageable);
        }
        Criteria finalCriteria = parseGroup(params.getRootGroup());
        return Query.query(finalCriteria).with(pageable);
    }

    private static Criteria parseGroup(FilterParams.FilterGroup group) {
        List<Criteria> compiledCriteriaList = new ArrayList<>();

        // 1. Process flat field rules safely
        if (group.getRules() != null) {
            for (FilterParams.SearchCriteria rule : group.getRules()) {
                if (rule.value() == null &&
                        rule.operator() != IS_NULL &&
                        rule.operator() != IS_NOT_NULL) {
                    continue;
                }
                compiledCriteriaList.add(mapRuleToCriteria(rule));
            }
        }

        // 2. Process nested sub-groups recursively
        if (group.getGroups() != null) {
            for (FilterParams.FilterGroup subGroup : group.getGroups()) {
                Criteria subCriteria = parseGroup(subGroup);
                if (!subCriteria.isEmpty()) {
                    compiledCriteriaList.add(subCriteria);
                }
            }
        }

        if (compiledCriteriaList.isEmpty()) {
            return Criteria.empty();
        }

        // 3. Correctly combine nodes dynamically
        Criteria base = compiledCriteriaList.get(0);
        if (group.getOperator() == FilterParams.FilterGroup.GroupOperator.OR) {
            for (int i = 1; i < compiledCriteriaList.size(); i++) {
                base = base.or(compiledCriteriaList.get(i));
            }
        } else {
            for (int i = 1; i < compiledCriteriaList.size(); i++) {
                base = base.and(compiledCriteriaList.get(i));
            }
        }

        return base;
    }

    private static Criteria mapRuleToCriteria(FilterParams.SearchCriteria rule) {
        String column = rule.field();
        Object val = rule.value();

        return switch (rule.operator()) {
            case EQUALS -> Criteria.where(column).is(val);
            case NOT_EQUALS -> Criteria.where(column).not(val);
            case GREATER_THAN -> Criteria.where(column).greaterThan(val);
            case GREATER_THAN_OR_EQUAL -> Criteria.where(column).greaterThanOrEquals(val);
            case LESS_THAN -> Criteria.where(column).lessThan(val);
            case LESS_THAN_OR_EQUAL -> Criteria.where(column).lessThanOrEquals(val);
            case LIKE -> Criteria.where(column).like("%" + val + "%");
            case NOT_LIKE -> Criteria.where(column).notLike("%" + val + "%");
            case LIKE_IGNORE_CASE -> Criteria.where(column).like("%" + val + "%").ignoreCase(true);
            case STARTS_WITH -> Criteria.where(column).like("%"+val).ignoreCase(true);
            case ENDS_WITH -> Criteria.where(column).like(val+"%").ignoreCase(true);
            case IN -> val instanceof Collection<?> ? Criteria.where(column).in((Collection<?>) val) : Criteria.where(column).is(val);
            case NOT_IN -> val instanceof Collection<?> ? Criteria.where(column).notIn((Collection<?>) val) : Criteria.where(column).not(val);
            case BETWEEN -> val instanceof Range<?>?Criteria.where(column).between(((Range<?>) val).start(),((Range<?>) val).end()):Criteria.empty();
            case IS_NULL -> Criteria.where(column).isNull();
            case IS_NOT_NULL -> Criteria.where(column).isNotNull();
        };
    }
}