package com.rbc.iam.policy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatcher implements ExpressionMatcher {

    private static final char regexStart = '<';
    private static final char regexEnd = '>';

    private Map<String, Pattern> regexCache  = new HashMap();

    private ArrayList<Integer> getDelimiterIndices(String expression){


        ArrayList<Integer> idxs = new ArrayList<>();

        int level=0;
        int idx =0;

        for (int i=0;i < expression.length(); i++){
             Character c = expression.charAt(i);
            switch (expression.charAt(i))
            {
                case regexStart:
                    level++;
                    if(level==1){
                        idx=i;
                    }
                    break;
                case regexEnd:
                    level--;
                    if(level==0){
                        idxs.add(idx);
                        idxs.add(i+1);
                    }
                    else if (level<0){
                        throw new IllegalArgumentException(String.format("Unbalanced braces in '%s'", expression));
                    }
                    break;
                default:
            }
        }

        if (level != 0) {
            throw new IllegalArgumentException(String.format("Unbalanced braces in '%s'", expression));
        }

        return idxs;
    }

    public Pattern getPattern(String regEx){
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

           Pattern pattern = getPattern(formatRegEx(expression));

           Matcher matcher = pattern.matcher(searchString);

           if (matcher.matches()) {
               return true;
           }

        }

        return false;
    }

    public String formatRegEx(String expression){
        String result = expression;

        ArrayList<Integer> delimiters = getDelimiterIndices(expression);

        if(delimiters.size()==2)
        {
            StringBuilder builder = new StringBuilder();
            Integer start = delimiters.get(0);
            Integer end = delimiters.get(1);

            //match beginning of the line (string)
            builder.append("^");

            //take part before regEx literally
            if(start >0 ) {
                builder.append(Pattern.quote(expression.substring(0, start)));
            }
            //wrap regEx in () brackets
            builder.append("(");
            builder.append(expression.substring(start+1,end-1));
            builder.append(")");

            //take part after regEx literally
            if(end < expression.length()){
                builder.append(Pattern.quote( expression.substring(end)));
            }


            //match end of line (string)
            builder.append("$");

            result = builder.toString();
        }
        else
        {
            //no regex, match literally
            result = String.format("^%s$", Pattern.quote(expression));
        }


        return result;
    }

    private Boolean isRegEx(String expression){
        //contains both regExStart and regExEnd, and start is before the end
        return (expression.indexOf(regexStart)< expression.indexOf(regexEnd));
    }

}
