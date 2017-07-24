package com.rbc.iam.policy.service;

import com.rbc.iam.policy.model.Request;

public interface PolicyService {
    Boolean isAllowed(Request request);
}
