package com.rbc.iam.policy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbc.iam.policy.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by igor on 2017-07-21.
 */
//@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class PolicyTest {



    @Test
    public void createAndReadPolicy(){

        Policy testPolicy = new Policy() {
            {
                id = "1";
                description =  "One policy to rule them all.";
                subjects = Arrays.asList("users:<[peter|ken]>", "users:maria", "groups:admins");
                resources = Arrays.asList("resources:articles:<.*>", "resources:printer");
                actions=  Arrays.asList("delete", "<[create|update]>");
                effect = "allow";
                conditions = new HashMap<String,Condition>(){
                    {
                        put("isBankUser", new StringEqualCondition("true"));
                        put("resourceOwner", new EqualsSubjectCondition());
                    }
                };
            }
        };

        ObjectMapper mapper = new ObjectMapper();

        String json;
        try {
             json = mapper.writeValueAsString(testPolicy);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
