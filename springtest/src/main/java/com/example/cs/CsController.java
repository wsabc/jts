package com.example.cs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CsController {

    @Autowired
    CsService csService;

    @GetMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
    public String demo() {
        return csService.demo();
    }

    @GetMapping("/employees")
    public List<Employee> employees() {
        return csService.employees();
    }
}
