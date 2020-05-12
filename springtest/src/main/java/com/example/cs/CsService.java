package com.example.cs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsService {

    @Autowired
    EmployeeRepository employeeRepository;

    public String demo() {
        return "helloCS";
    }

    public Long getId(String name) {
        Employee employee = employeeRepository.findByName(name);
        return employee.getId();
    }

    public List<Employee> employees() {
        return employeeRepository.findAll();
    }

}
