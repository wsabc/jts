Feature: Shopping

  Scenario: Not enough money
    Given I have 90 in my wallet now
    When I want to buy milk which is 101
    Then I have not enough money, and have earn 11 more