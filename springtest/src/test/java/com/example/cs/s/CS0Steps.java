package com.example.cs.s;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java8.En;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.core.util.EnvironmentVariables;
import org.testcontainers.containers.RabbitMQContainer;

public class CS0Steps extends CSBaseSteps implements En {

    static RabbitMQContainer rabbitMQContainer;

    static {
        System.out.println("Test0 static: prepare env ------------");

        // prepare docker
        rabbitMQContainer = new RabbitMQContainer("rabbitmq")
                .withExposedPorts(5672)
                .withVhost("/")
                .withUser("admin", "admin")
                .withPermission("/", "admin", ".*", ".*", ".*");
        rabbitMQContainer.start();
        System.setProperty("x", rabbitMQContainer.getAmqpUrl());
        System.setProperty("y", String.valueOf(rabbitMQContainer.getAmqpPort()));
    }

    private EnvironmentVariables environmentVariables;
    private String theRestApiBaseUrl;

    public CS0Steps() {
        System.out.println("Test0 -----------");
    }

    @Before
    public void before() {
        System.out.println("bbbbbbbbbbbbbbbbbbefore>>>>>>");
        theRestApiBaseUrl = environmentVariables.optionalProperty("restapi.baseurl")
                .orElse("https://reqres.in/api");

        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("cesar").whoCan(CallAnApi.at(theRestApiBaseUrl));
    }

    @After
    public void after() {
        System.out.println("<<<<<aaaaaaaaaaaaaaaaaaafter");
    }
}
