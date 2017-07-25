package com.rbc.iam.policy.service;

import com.rbc.iam.policy.model.Policy;
import com.rbc.iam.policy.model.Request;

import java.util.List;


public interface PolicyService {
    Policy create(Policy policy);
    List<Policy> findAll();
    Boolean isAllowed(Request request);
}
