package com.rbc.iam.policy;

import com.rbc.iam.policy.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PolicyApplication {
	@Autowired
	PolicyRepository repository;

	public static void main(String[] args) {

		SpringApplication.run(PolicyApplication.class, args);

	}

}
