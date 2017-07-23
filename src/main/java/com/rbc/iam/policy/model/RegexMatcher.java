package com.rbc.iam.policy.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatcher implements ExpressionMatcher {

    private static String regexStart = "<";
    private static String regexEnd = ">";

    private Map<String, Pattern> regexCache  = new HashMap();

    private Pattern getPattern(String regEx){
        if (regexCache.containsKey(regEx)) {
            return regexCache.get(regEx);
        }

        Pattern pattern = Pattern.compile(regEx);

        regexCache.put(regEx, pattern);

        return  pattern;
    }

    @Override
    public Boolean Matches(List<String> expressions, String searchString) {


        for(String expression : expressions){
            // This means that the current expression item does not contain a regular expression
           if (!isRegEx(expression))
           {
               if (expression.equals(searchString)) {
                   return true;
               }

               // Not string match, but also no regexp, continue with next expression item
               continue;
           }

           Pattern pattern = getPattern(stripDelimiters(expression));

           Matcher matcher = pattern.matcher(searchString);

           if (matcher.matches()) {
               return true;
           }

        }

        return false;
    }

    public String stripDelimiters(String expression){
        String result = expression;

        if (expression.startsWith(regexStart))
            result = expression.substring(regexStart.length());

        if (expression.endsWith(regexEnd))
            result = result.substring(0,result.length()- regexEnd.length() );

        return result;
    }

    private Boolean isRegEx(String expression){
        return expression.startsWith(regexStart) && expression.endsWith(regexEnd);
    }
}
