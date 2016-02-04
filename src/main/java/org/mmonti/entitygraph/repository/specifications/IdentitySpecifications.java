package org.mmonti.entitygraph.repository.specifications;

import org.mmonti.entitygraph.model.Identity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmonti on 12/16/15.
 */
public class IdentitySpecifications {

    public static final String IDENTITY_EMAIL_ADDRESS = "emailAddress";
    public static final String IDENTITY_FEDERATED_ID = "federatedId";
    public static final String IDENTITY_REVOKED = "revoked";

    private IdentitySpecifications() {}

    /**
     *
     * @param emailAddress
     * @param federatedId
     * @param revoked
     *
     * @return
     */
    public static Specification<Identity> withEmailAddressOrFederatedIdAndRevoked(final String emailAddress,
                                                                                  final String federatedId, final Boolean revoked) {
        return new Specification<Identity>() {
            @Override
            public Predicate toPredicate(final Root<Identity> root, final CriteriaQuery<?> criteriaQuery,
                                         final CriteriaBuilder criteriaBuilder) {

                final List<Predicate> predicates = new ArrayList<>();
                if (emailAddress != null && !emailAddress.isEmpty()) {
                    predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(IDENTITY_EMAIL_ADDRESS)), emailAddress.toLowerCase()));
                }
                if (federatedId != null && !federatedId.isEmpty()) {
                    predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(IDENTITY_FEDERATED_ID)), federatedId.toLowerCase()));
                }

                // = revoked always has a value. (Default: FALSE)
                predicates.add(criteriaBuilder.equal(root.get(IDENTITY_REVOKED), revoked));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    /**
     *
     * @param emailAddress
     * @return
     */
    public static Specification<Identity> emailAddress(final String emailAddress) {
        return new Specification<Identity>() {
            @Override
            public Predicate toPredicate(final Root<Identity> root,
                                         final CriteriaQuery<?> criteriaQuery,
                                         final CriteriaBuilder criteriaBuilder) {

                if (emailAddress != null && !emailAddress.isEmpty()) {
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get(IDENTITY_EMAIL_ADDRESS)), "%"+emailAddress.toLowerCase()+"%");
                }
                return null;
            }
        };
    }

    /**
     *
     * @param federatedId
     * @return
     */
    public static Specification<Identity> federatedId(final String federatedId) {
        return new Specification<Identity>() {
            @Override
            public Predicate toPredicate(final Root<Identity> root,
                                         final CriteriaQuery<?> criteriaQuery,
                                         final CriteriaBuilder criteriaBuilder) {

                if (federatedId != null && !federatedId.isEmpty()) {
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get(IDENTITY_FEDERATED_ID)), "%"+federatedId.toLowerCase()+"%");
                }
                return null;
            }
        };
    }

    /**
     *
     * @param revoked
     * @return
     */
    public static Specification<Identity> revoked(final Boolean revoked) {
        return new Specification<Identity>() {
            @Override
            public Predicate toPredicate(final Root<Identity> root,
                                         final CriteriaQuery<?> criteriaQuery,
                                         final CriteriaBuilder criteriaBuilder) {
                if (revoked != null) {
                    return criteriaBuilder.equal(root.get(IDENTITY_REVOKED), revoked);
                }
                return null;
            }
        };
    }



}
