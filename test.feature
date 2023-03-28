#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: Test functionality of shopping cart
  I want to test functionality of shopping cart

  @Setup @tag1 @CloseBrowser
  Scenario: Test functionality of shopping cart
    When I logged in to website with username "admin" and password "admin"
    And I added item to the shopping cart
    Then I validate that able to place an order

   @api
  Scenario: Test functionality of api
    When User hits end point
    Then User should be able to see the province list
    
    
    