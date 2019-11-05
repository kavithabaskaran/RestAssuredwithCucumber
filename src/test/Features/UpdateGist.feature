Feature: Verify successful api call for all public gists

  Scenario Outline:Verify successful api for all public gists
    Given I am able to access the url
    When I create a new gist
    And I verify newly created gist
    Then I update details for gist description "<newDescription>"

    Examples:
      | newDescription |
      | updated new description for gist test|