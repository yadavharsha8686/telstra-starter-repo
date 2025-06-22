Feature: SIM Card Activation

  Scenario: Successfully activate a SIM card
    Given the SIM card ICCID is "1255789453849037777" and the email is "success@example.com"
    When the user activates the SIM card
    Then the activation should be successful for ID 1

  Scenario: Fail to activate a SIM card
    Given the SIM card ICCID is "8944500102198304826" and the email is "fail@example.com"
    When the user activates the SIM card
    Then the activation should fail for ID 2
