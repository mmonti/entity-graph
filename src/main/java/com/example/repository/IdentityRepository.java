package com.example.repository;

import com.example.model.Identity;
import org.springframework.stereotype.Repository;

/**
 * Created by mmonti on 12/3/15.
 */
@Repository
public interface IdentityRepository extends CustomGenericRepository<Identity, String> {

}
