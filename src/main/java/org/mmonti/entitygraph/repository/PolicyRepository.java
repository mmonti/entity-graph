package org.mmonti.entitygraph.repository;

import org.mmonti.entitygraph.model.Policy;
import org.springframework.stereotype.Repository;

/**
 * Created by mmonti on 12/3/15.
 */
@Repository
public interface PolicyRepository extends CustomGenericRepository<Policy, String> {
}
