package com.example.repository;

import com.example.model.Policy;
import org.springframework.stereotype.Repository;

/**
 * Created by mmonti on 12/3/15.
 */
@Repository
public interface PolicyRepository extends CustomGenericRepository<Policy, String> {
}
