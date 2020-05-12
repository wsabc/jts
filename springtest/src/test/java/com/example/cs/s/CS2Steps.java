package com.example.cs.s;

import io.cucumber.java8.En;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

public class CS2Steps extends CSBaseSteps implements En {

    private Actor user;

    public CS2Steps() {
        System.out.println("Test CS2Steps -----------");
        Given("The system is up", () -> {

        });

        When("I call the {word} api", (String url) -> {
            OnStage.theActorInTheSpotlight().attemptsTo(Get.resource(url));
        });

        Then("I got response which is {word}", (String result) -> {
            OnStage.theActorInTheSpotlight().should(seeThatResponse(res -> {
                res.statusCode(200).root("$").equals(result);
            }));
        });
    }

//    class Login implements Task {
//        User user;
//        public Login(User user) {
//            this.user = user;
//        }
//
//        @Override
//        public <T extends Actor> void performAs(T theActor) {
//            theActor.attemptsTo(
//                    Post.to(CREATE_USER.toString())
//                            .with(request -> request.header("Content-Type", "application/json")
//                                    .body(user)
//                            )
//            );
//        }
//
//        static Login loginSystem(User user) {
//            return Tasks.instrumented(Login.class, user);
//        }
//    }

}
