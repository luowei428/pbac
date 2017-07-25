package com.rbc.iam.policy;

import com.rbc.iam.policy.model.*;
import com.rbc.iam.policy.repository.PolicyRepository;
import com.rbc.iam.policy.service.PolicyService;
import com.rbc.iam.policy.service.PolicyServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
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
                 "approve");

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

}

