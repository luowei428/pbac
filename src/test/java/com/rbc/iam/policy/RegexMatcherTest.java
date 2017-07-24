package com.rbc.iam.policy;

import com.rbc.iam.policy.model.RegexMatcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

public class RegexMatcherTest {

    RegexMatcher matcher = new RegexMatcher();
    List<String> actions=  Arrays.asList("delete", "<create|update>");
    List<String> subjects = Arrays.asList("users:<peter|ken>", "users:maria", "groups:admins");

    @Test
    public void testStripDelimiters_withDelimiters(){

        String expression = matcher.formatRegEx("<create|update>");

        assertThat(expression, equalTo("^(create|update)$"));
    }


    @Test
    public void testMatches_nonRegex(){
        Boolean matches = matcher.Matches(actions, "delete");

        assertThat(matches, equalTo(true));
    }

    @Test
    public void testMatches_nonRegexNotEqual(){
        Boolean matches = matcher.Matches(actions, "Delete");

        assertThat(matches, equalTo(false));
    }

    @Test
    public void testMatches_simpleRegex1(){
        Boolean matches = matcher.Matches(actions, "create");

        assertThat(matches, equalTo(true));
    }

    @Test
    public void testMatches_simpleRegex2(){
        Boolean matches = matcher.Matches(actions, "update");

        assertThat(matches, equalTo(true));
    }

    @Test
    public void testMatches_compleRegex1(){
        Boolean matches = matcher.Matches(subjects, "users:peter");

        assertThat(matches, equalTo(true));
    }



}
