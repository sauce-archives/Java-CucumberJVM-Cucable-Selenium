Feature: Guinea Pig comment

  Scenario: Can submit comment
    Given I am on the Guinea Pig homepage
    When I submit a comment
    Then I should see that comment displayed

  Scenario: Can submit comment 2
    Given I am on the Guinea Pig homepage
    When I submit a comment
    Then I should see that comment displayed