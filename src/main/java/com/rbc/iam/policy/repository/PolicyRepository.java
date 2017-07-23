package com.rbc.iam.policy.repository;

import com.rbc.iam.policy.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by igor on 2017-07-21.
 */

public interface PolicyRepository extends JpaRepository<Policy,String> {
}
