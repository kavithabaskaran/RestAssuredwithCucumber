Feature: Verify Fork Gist API functionality

  Scenario Outline:Fork a gist and verify the gist is forked
    Given I am able to access the url
    When I get a single gist for gistId "<gistId>"
    Then I fork the gist with gistId "<gistId>"
    Then I verify the gist with gistId "<gistId>" is forked

    Examples:
      |gistId|
      |574558390aaa237de531e07b06bbecec|

  Scenario Outline:Unfork the gist
    Given I am able to access the url
    When I get a single gist for gistId "<gistId>"
    And I fork the gist with gistId "<gistId>"
    Then I verify the gist with gistId "<gistId>" is forked
    Then I remove fork for gistId "<gistId>"

    Examples:
      |gistId|
      |574558390aaa237de531e07b06bbecec|