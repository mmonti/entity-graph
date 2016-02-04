package org.mmonti.entitygraph.repository.specifications;


import org.mmonti.entitygraph.model.Policy;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by mmonti on 12/16/15.
 */
public class PolicySpecifications {

    public static final String NAME = "name";

    private PolicySpecifications() {}

    /**
     *
     * @param name
     * @return
     */
    public static Specification<Policy> name(final String name) {
        return new Specification<Policy>() {
            @Override
            public Predicate toPredicate(final Root<Policy> root, final CriteriaQuery<?> criteriaQuery,
                                         final CriteriaBuilder criteriaBuilder) {

                if (name != null && !name.isEmpty()) {
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get(NAME)), "%"+name.toLowerCase()+"%");
                }
                return null;
            }
        };
    }

}
