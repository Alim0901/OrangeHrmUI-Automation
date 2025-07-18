@Login @tearDown @setUp
Feature: OrangeHRM Login Feature

  @login_happy_path @smoke @regression @jiraID-01
  Scenario: Successful login(Happy path)
    Given User navigates to the login page
    When User enters "valid" credentials on login page
    And User clicks on login Btn on login page
    Then User should be navigated to the dashboard page

  @login_unhappy_path @smoke @jiraID-02
  Scenario: Invalid login attempt
    Given User navigates to the login page
    When User enters "invalid" credentials on login page
    And User clicks on login Btn on login page
    Then User should see 'unable to login' error message