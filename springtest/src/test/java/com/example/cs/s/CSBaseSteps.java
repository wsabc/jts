package com.example.cs.s;

import com.example.cs.CsApplication;
import com.example.cs.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {CsApplication.class, TestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class CSBaseSteps {

}
