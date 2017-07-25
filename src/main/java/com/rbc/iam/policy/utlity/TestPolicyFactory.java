package com.rbc.iam.policy.utlity;

import com.rbc.iam.policy.model.Condition;
import com.rbc.iam.policy.model.EqualsSubjectCondition;
import com.rbc.iam.policy.model.Policy;
import com.rbc.iam.policy.model.StringEqualCondition;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by igor on 2017-07-22.
 */
public class TestPolicyFactory {
    public static Policy createMixedAllowTestPolicy(){
        Policy testPolicy = new Policy() {
            {
                id = "1";
                description =  "Mixed bag allow";
                subjects = Arrays.asList("users:<(peter|ken)>", "users:maria", "groups:admins");
                resources = Arrays.asList("resources:articles:<.*>", "resources:printer");
                actions=  Arrays.asList("delete", "<(create|update)>");
                effect = "allow";
            }
        };


        testPolicy.addCondition("isBankUser", new StringEqualCondition("true"));
        testPolicy.addCondition("resourceOwner", new EqualsSubjectCondition());

        return  testPolicy;
    }
}
