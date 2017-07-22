package com.rbc.iam.policy.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class Policy implements Serializable {



    @Id
    protected String id;

    protected String description;

    @ElementCollection
    protected List<String> subjects = new ArrayList<>();

    protected String effect;

    @ElementCollection
    protected List<String> resources = new ArrayList<>();

    @ElementCollection
    protected List<String> actions = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    protected Map<String,Condition> conditions;

    // GetID returns the policies id.
    public String getId() {
        return this.id;
    }

    // GetDescription returns the policies description.
    public String getDescription() {
        return this.description;
    }

    // GetSubjects returns the policies subjects.
    public List<String> getSubjects(){
        return this.subjects;
    }

    // AllowAccess returns true if the policy effect is allow, otherwise false.
    public Boolean allowAccess(){
        return false;
    }

    // GetEffect returns the policies effect which might be 'allow' or 'deny'.
    public String getEffect() {
        return this.effect;
    }

    // GetResources returns the policies resources.
    public List<String> getResources() {
        return this.resources;
    }

    // GetActions returns the policies actions.
    public List<String> getActions() {
        return this.actions;
    }

    // GetConditions returns the policies conditions.
    public Map<String,Condition> getConditions() {
        return this.conditions;
    }

    // GetStartDelimiter returns the delimiter which identifies the beginning of a regular expression.
    @JsonIgnore()
    public char getStartDelimiter() {
        return '<';
    }

    // GetEndDelimiter returns the delimiter which identifies the end of a regular expression.
    @JsonIgnore
    public char getEndDelimiter() {
        return '>';
    }


    public void addCondition(String name, Condition condition) {
        conditions.put(name, condition);
    }
}
