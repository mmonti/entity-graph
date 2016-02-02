package com.example.resource;

import com.example.model.Identity;
import com.example.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mmonti on 12/13/15.
 */
@CrossOrigin(origins = "*")
@RestController
public class IdentityResource {

    private IdentityService identityService;

    /**
     *
     * @param identityService
     */
    @Autowired
    public IdentityResource(final IdentityService identityService) {
        this.identityService = identityService;
    }

    @RequestMapping(value = "/identities", method = RequestMethod.GET)
    public ResponseEntity<List<Identity>> identities(
            @RequestParam(name = "emailAddress", required = false) final String emailAddress,
            @RequestParam(name = "federatedId", required = false) final String federatedId,
            @RequestParam(name = "revoked", required = false) final Boolean revoked,
            @RequestParam(name = "attrs", required = false) final String[] attrs) {

        List<Identity> identities = this.identityService.identities(emailAddress, federatedId, revoked, attrs);
        return ResponseEntity.ok(identities);
    }

    @RequestMapping(value = "/identities/page", method = RequestMethod.GET)
    public ResponseEntity<Page<Identity>> page(
            @RequestParam(name = "emailAddress", required = false) final String emailAddress,
            @RequestParam(name = "federatedId", required = false) final String federatedId,
            @RequestParam(name = "revoked", required = false) final Boolean revoked,
            @RequestParam(name = "attrs", required = false) final String[] attrs,
            final Pageable pageable) {

        Page<Identity> page = this.identityService.page(emailAddress, federatedId, revoked, pageable, attrs);
        return ResponseEntity.ok(page);
    }

    @RequestMapping(value = "/identities/{id}", method = RequestMethod.GET)
    public ResponseEntity<Identity> identity(@PathVariable final String id) {
        return ResponseEntity.ok(this.identityService.identity(id));
    }

}
