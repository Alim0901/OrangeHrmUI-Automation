@Logout @tearDown @setUp
Feature: OrangeHRM Logout Feature

  @Logout_happy_path @smoke @regression @jiraID-03
  Scenario: Logout flow
    Given User navigates to the login page
    When User enters "valid1" credentials on login page
    And User clicks on login Btn on login page
    When User clicks on logout Btn on the page
    Then User should be on the login page