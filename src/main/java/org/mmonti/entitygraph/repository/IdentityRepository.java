package org.mmonti.entitygraph.repository;

import org.mmonti.entitygraph.model.Identity;
import org.springframework.stereotype.Repository;

/**
 * Created by mmonti on 12/3/15.
 */
@Repository
public interface IdentityRepository extends CustomGenericRepository<Identity, String> {

}
