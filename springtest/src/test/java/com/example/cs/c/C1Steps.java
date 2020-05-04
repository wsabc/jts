package com.example.cs.c;

import io.cucumber.java8.En;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class C1Steps implements En {
    private int budget = 0;
    public C1Steps() {
        System.out.println("Test1111111111 -----------");

        Given("I have {int} in my wallet", (Integer money) -> budget = money);

        When("I buy milk with {int}", (Integer price) -> budget -= price);

        Then("I should have {int} in my wallet", (Integer finalBudget) ->
                assertEquals(budget, finalBudget.intValue()));
    }
}
