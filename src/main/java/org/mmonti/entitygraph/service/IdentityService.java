package org.mmonti.entitygraph.service;

import org.mmonti.entitygraph.model.Identity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by mmonti on 12/15/15.
 */
public interface IdentityService {

    /**
     *
     * @param emailAddress
     * @param federatedId
     * @param revoked
     * @return
     */
    List<Identity> identities(final String emailAddress, final String federatedId, final Boolean revoked, String ... attrs);

    /**
     *
     * @param emailAddress
     * @param federatedId
     * @param revoked
     * @param pageable
     * @return
     */
    Page<Identity> page(final String emailAddress, final String federatedId, final Boolean revoked, final Pageable pageable, String ... attrs);

    /**
     *
     * @param id
     * @return
     */
    Identity identity(final String id);

    /**
     *
     * @param identity
     * @return
     */
    Identity save(final Identity identity);


}
