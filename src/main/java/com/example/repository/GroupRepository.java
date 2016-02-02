package com.example.repository;

import com.example.model.Group;
import org.springframework.stereotype.Repository;

/**
 * Created by mmonti on 12/3/15.
 */
@Repository
public interface GroupRepository extends CustomGenericRepository<Group, String> {
}
