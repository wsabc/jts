Feature: Demo api call

  Scenario: call the demo api
    Given The system is up
    When I call the /demo api
    Then I got response which is helloCS