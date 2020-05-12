package com.example.mongodb.spring.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    // use MongoTemplate in the backend
    Customer findByFirstName(String firstName);
    List<Customer> findByLastName(String lastName);

}
