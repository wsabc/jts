package com.example.cs.s;

import io.cucumber.java8.En;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CS1Steps extends CSBaseSteps implements En {
    private int budget = 0;
    public CS1Steps() {
        System.out.println("Test CS1Steps -----------");
        Given("I have {int} in my wallet now", (Integer money) -> budget = money);

        When("I want to buy milk which is {int}", (Integer price) -> budget -= price);

        Then("I have not enough money, and have earn {int} more", (Integer finalBudget) ->
                assertEquals(-budget, finalBudget.intValue()));
    }
}
