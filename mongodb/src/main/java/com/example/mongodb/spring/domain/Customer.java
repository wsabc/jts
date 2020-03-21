package com.example.mongodb.spring.domain;

import org.springframework.data.annotation.Id;

public class Customer {

    // no @Document annotation, then use class name to name collection

    @Id
    public String id; // for _id

    public String firstName; // unannotated since share the same name as the properties
    public String lastName;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%s, firstName='%s', lastName='%s']", id, firstName, lastName);
    }
}
