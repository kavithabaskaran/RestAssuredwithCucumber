Feature: Verify successful api call for all public gists

  Scenario:Verify successful api for all public gists
    Given I am able to access the url
    When I create a new gist
    Then I verify newly created gist
