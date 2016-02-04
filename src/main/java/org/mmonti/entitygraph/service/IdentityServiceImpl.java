package org.mmonti.entitygraph.service;

import lombok.extern.slf4j.Slf4j;
import org.mmonti.entitygraph.model.Identity;
import org.mmonti.entitygraph.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mmonti.entitygraph.repository.CustomGenericJpaRepository.EntityGraphType.LOAD;
import static org.mmonti.entitygraph.repository.specifications.IdentitySpecifications.*;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by mmonti on 12/4/15.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class IdentityServiceImpl implements IdentityService {

    private IdentityRepository identityRepository;

    @Autowired
    public IdentityServiceImpl(final IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    @Override
    public Page<Identity> page(final String emailAddress, final String federatedId, final Boolean revoked, final Pageable pageable, String ... attrs) {
        final Specifications<Identity> specification = where(emailAddress(emailAddress)).or(federatedId(federatedId));
        if (revoked != null) {
            return this.identityRepository.findAll(specification.and(revoked(revoked)), pageable, LOAD, attrs);
        }
        return this.identityRepository.findAll(specification, pageable, LOAD, attrs);
    }

    @Override
    public List<Identity> identities(final String emailAddress, final String federatedId, final Boolean revoked, String ... attrs) {
        final Specifications<Identity> specification = where(emailAddress(emailAddress)).or(federatedId(federatedId));
        if (revoked != null) {
            return this.identityRepository.findAll(specification.and(revoked(revoked)), LOAD, attrs);
        }
        return this.identityRepository.findAll(specification, LOAD, attrs);
    }

    @Override
    public Identity identity(final String id) {
        if (id == null || id.isEmpty()) {
            log.debug("id is null or empty");
            throw new RuntimeException("id is null or empty");
        }

        final Identity identity = this.identityRepository.findOne(id);
        if (identity == null) {
            log.debug("identity(id={}) not found");
            throw new RuntimeException();
        }

        return identity;
    }

    @Override
    @Transactional(readOnly = false)
    public Identity save(final Identity identity) {
        if (identity == null) {
            log.debug("identity is null)");
            throw new RuntimeException("identity is null");
        }
        return this.identityRepository.save(identity);
    }

}
