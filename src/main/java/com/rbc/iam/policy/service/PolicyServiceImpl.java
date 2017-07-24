package com.rbc.iam.policy.service;

import com.rbc.iam.policy.model.ExpressionMatcher;
import com.rbc.iam.policy.model.Policy;
import com.rbc.iam.policy.model.Request;
import com.rbc.iam.policy.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository repository;
    private final ExpressionMatcher matcher;

    @Autowired
    public PolicyServiceImpl(PolicyRepository repository, ExpressionMatcher matcher){
        this.repository = repository;
        this.matcher = matcher;
    }


    @Override
    public Boolean isAllowed(Request request) {

        Boolean allowed = false;

        //TODO: filter policies on repository level
        List<Policy> policies= repository.findAll();

        // Iterate through all policies
        for (Policy policy: policies) {

            // Does the action match with one of the policy actions?
            // This is the first check because usually actions are a superset of get|update|delete|set
            // and thus match faster.
            if(!matcher.Matches(policy.getActions(), request.getAction())){
                // no, continue to next policy
                continue;
            }

            // Does the subject match with one of the policy subjects?
            // There are usually less subjects than resources which is why this is checked
            // before checking for resources.
            if(!matcher.Matches(policy.getSubjects(),request.getSubject())){
                // no, continue to next policy
                continue;
            }

            // Does the resource match with one of the policy resources?
            if(!matcher.Matches(policy.getResources(), request.getResource())){
                // no, continue to next policy
                continue;
            }

            // Are the policies conditions met?
            // This is checked first because it usually has a small complexity.
            if (!policy.passesConditions(request)){
                continue;
            }

            // Is the policies effect deny? If yes, this overrides all allow policies -> access denied.

            if(!policy.allowAccess()){
                return false;
            }

            //we found at least one policy that allows this request
            allowed = true;
        }

        return  allowed;
    }
}
