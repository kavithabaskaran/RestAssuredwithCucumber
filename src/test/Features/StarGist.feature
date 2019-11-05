Feature: Verify Star Gist API functionality

  Scenario Outline:Star a gist and verify the gist is starred
    Given I am able to access the url
    When I get a single gist for gistId "<gistId>"
    Then I star the gist with gistId "<gistId>"
    Then I verify the gist with gistId "<gistId>" is starred

    Examples:
      |gistId|
      |546880a3d39b8e094e45860fd9563201|

  Scenario Outline:Unstar a gist
    Given I am able to access the url
    When I get a single gist for gistId "<gistId>"
    Then I star the gist with gistId "<gistId>"
    Then I verify the gist with gistId "<gistId>" is starred
    Then I unstar the gist with gistId "<gistId>"

    Examples:
      |gistId|
      |9ca47a942954aac2dd5ef1289ffec3b7|

  Scenario Outline:Star a invalid gist
    Given I am able to access the url
    Then I star the invalid gist "<gistId>"

    Examples:
      |gistId|
      |9ca47a942954aac2dd5ef1289ffec3xy|
