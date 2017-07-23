package com.rbc.iam.policy.web;

import com.rbc.iam.policy.model.Policy;
import com.rbc.iam.policy.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * Created by igor on 2017-07-22.
 */
@RestController
@RequestMapping(path="/policies")

public class PolicyController {
    private final PolicyRepository repository;

    @Autowired
    public PolicyController(PolicyRepository repository){
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getList(){
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        List<Policy> list = repository.findAll();

        return new ResponseEntity<>(list, httpHeaders, HttpStatus.OK);

    }

    @RequestMapping( method = RequestMethod.POST )
    public ResponseEntity<?> create(@RequestBody Policy item ){
        HttpHeaders httpHeaders = new HttpHeaders();

        if (item == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }
        final Policy savedItem = repository.saveAndFlush(item);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(item.getId()).toUri());

        return new ResponseEntity<>(savedItem, httpHeaders, HttpStatus.CREATED);
    }
}
