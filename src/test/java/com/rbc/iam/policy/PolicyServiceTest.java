package com.rbc.iam.policy;

import com.rbc.iam.policy.model.*;
import com.rbc.iam.policy.service.PolicyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional()
public class PolicyServiceTest {

    private PolicyService policyService;

@Autowired
    public void PolicyServiceImpl(PolicyService service){
        this.policyService = service;
    }
    @Before
    public void setup(){
        Policy testPolicy = Policy.Create(
                "1",
                "Allow - no conditions",
                Arrays.asList("users:<peter|ken>", "users:maria", "groups:admins"),
                Arrays.asList("resources:articles:<.*>", "resources:printer"),
                Arrays.asList("delete", "<create|update>"),
                "allow");

        policyService.create(testPolicy);

        testPolicy = Policy.Create("2",
                "My profile - all",
                Arrays.asList("users:<.*>"),
                Arrays.asList("resources:profile:<.*>"),
                Arrays.asList("update","create"),
                "allow");


        testPolicy.addCondition("resourceOwner", new EqualsSubjectCondition());

        policyService.create(testPolicy);

        testPolicy = Policy.Create("3",
                "Deny my profile change approval",
                Arrays.asList("users:<.*>"),
                Arrays.asList("resources:profile:<.*>"),
                Arrays.asList("approve"),
                "deny");

        testPolicy.addCondition("resourceOwner", new EqualsSubjectCondition());

        policyService.create(testPolicy);

        testPolicy = Policy.Create("4",
                 "Admin profile change approval",
                Arrays.asList("groups:admins"),
                Arrays.asList("resources:profile:<.*>"),
                Arrays.asList("approve"),
                 "allow");

        testPolicy.addCondition("isBankUser", new StringEqualCondition("true"));
        testPolicy.addCondition("isWorkingHours", new StringEqualCondition("true"));

        policyService.create(testPolicy);

    }

    @Test
    public void peter_can_create_articles_returns_true() {
        Request request = new Request() {
            {
                subject ="users:peter";
                resource="resources:articles:";
                action="create";
            }
        };

        assertThat(policyService.isAllowed(request), equalTo(true));
    }

    @Test
    public void george_can_create_articles_returns_false() {
        Request request = new Request() {
            {
                subject ="users:george";
                resource="resources:articles:";
                action="create";
            }
        };

        assertThat(policyService.isAllowed(request), equalTo(false));
    }

    @Test
    public void admins_can_delete_printers_returns_true() {
        Request request = new Request() {
            {
                subject ="groups:admins";
                resource="resources:printer";
                action="delete";
            }
        };

        assertThat(policyService.isAllowed(request), equalTo(true));
    }

    @Test
    public void any_user_can_update_his_address() {
        Request request = new Request() {
            {
                subject ="users:anon";
                resource="resources:profile:address";
                action="update";
            }
        };

        request.getContext().setProperty("resourceOwner","users:anon");

        assertThat(policyService.isAllowed(request), equalTo(true));
    }

    @Test
    public void any_user_auhorizing_changes_to_profile_returns_false() {
        Request request = new Request() {
            {
                subject ="users:anon";
                resource="resources:profile:";
                action="approve";
            }
        };

        request.getContext().setProperty("resourceOwner","users:anon");

        assertThat(policyService.isAllowed(request), equalTo(false));
    }

    @Test
    public void admin_user_authorizing_changes_to_profile_returns_true() {
        Request request = new Request() {
            {
                subject ="groups:admins";
                resource="resources:profile:";
                action="approve";
            }
        };

        request.getContext().setProperty("isBankUser","true");
        request.getContext().setProperty("isWorkingHours","true");

        assertThat(policyService.isAllowed(request), equalTo(true));
    }
}

