Feature: Verify Get Gist api call

  Scenario Outline:Verify successful response for a single gist using gistId
    Given I am able to access the url
    When I get a single gist for gistId "<gistId>"

    Examples:
      |gistId|
      |546880a3d39b8e094e45860fd9563201|
      |9ca47a942954aac2dd5ef1289ffec3b7|

  Scenario Outline:Verify error status for invalid gistId
    Given I am able to access the url
    When I get a single gist for invalid "<gistId>"

    Examples:
      |gistId|                            statusId|
      |546880a3d39b8e094e45860fd95632fe| 404      |