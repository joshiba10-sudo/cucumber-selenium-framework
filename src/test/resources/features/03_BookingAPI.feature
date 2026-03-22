Feature: Shady Meadows B&B Booking API Testing

  # Background:
  #   Given Base API URL is set to "https://automationintesting.online/api"

  @smoke @api @regression
  Scenario: Get list of all available rooms
    When User requests to get list of all rooms
    Then API response status code should be 200
    And Response should contain list of rooms
    And Each room should have room details

  @smoke @api @regression
  Scenario: Get room details by room ID
    When User requests to get room details for roomid "1"
    Then API response status code should be 200
    And Response should contain room information
    And Room details should contain "roomid"

  @api @regression
  Scenario: Check room availability for specific dates
    When User requests to check room availability with checkin "2025-07-17" and checkout "2025-07-18"
    Then API response status code should be 200
    And Response should contain available rooms
    And Available rooms list should not be empty

  @smoke @api
  Scenario: Create a new booking with valid details
    When User creates a new booking with following details:
      | firstname   | John       |
      | lastname    | Doe        |
      | email       | john@email.com |
      | phone       | 07358480685    |
      | roomid      | 1          |
      | checkin     | 2025-07-17 |
      | checkout    | 2025-07-18 |
      | depositpaid | false      |
    Then API response status code should be 201
    And Response should contain booking confirmation
    And Response should contain booking ID

  @api @regression
  Scenario: Create booking with deposit paid
    When User creates a new booking with following details:
      | firstname   | Jane       |
      | lastname    | Smith      |
      | email       | jane@email.com |
      | phone       | 07358480686    |
      | roomid      | 2          |
      | checkin     | 2025-08-01 |
      | checkout    | 2025-08-05 |
      | depositpaid | true       |
    Then API response status code should be 201
    And Response should contain booking ID
    And Booking should show depositpaid as true

  @api @regression
  Scenario: Get booking details by room ID
    When User requests to get booking details with roomid "1"
    Then API response status code should be 200
    And Response should contain booking information
    And Response should contain booking dates

  @api
  Scenario: Update existing booking
    When User gets current booking with roomid "1"
    And User updates the booking with following details:
      | firstname   | James      |
      | lastname    | Updated    |
      | depositpaid | true       |
      | checkin     | 2026-03-09 |
      | checkout    | 2026-03-10 |
    Then API response status code should be 200
    And Updated booking response should be received

  @api @regression
  Scenario: Get booking report
    When User requests booking report
    Then API response status code should be 200
    And Response should contain booking statistics
    And Response should have report data

  @api @smoke
  Scenario: Admin authentication
    When User authenticates as admin with username "admin" and password "password"
    Then API response status code should be 200
    And Response should contain authentication token
    And Token should not be empty

  @api @regression
  Scenario: Delete existing booking
    When User creates a new booking with following details:
      | firstname   | Delete     |
      | lastname    | Test       |
      | email       | delete@email.com |
      | phone       | 07358480687    |
      | roomid      | 1          |
      | checkin     | 2025-09-01 |
      | checkout    | 2025-09-02 |
      | depositpaid | false      |
    And User stores the booking ID from response
    And User deletes the booking using stored booking ID
    Then API response status code should be 201 or 202

  @api @regression
  Scenario Outline: Create multiple bookings with different room types
    When User creates a new booking with following details:
      | firstname   | <firstname> |
      | lastname    | <lastname>  |
      | email       | <email>     |
      | phone       | 07358480690 |
      | roomid      | <roomid>    |
      | checkin     | <checkin>   |
      | checkout    | <checkout>  |
      | depositpaid | <deposit>   |
    Then API response status code should be 201
    And Response should contain booking ID

    Examples:
      | firstname | lastname | email           | roomid | checkin    | checkout   | deposit |
      | John      | Single   | john@test.com   | 1      | 2025-07-17 | 2025-07-18 | false   |
      | Jane      | Double   | jane@test.com   | 2      | 2025-08-01 | 2025-08-05 | true    |
      | Tom       | Suite    | tom@test.com    | 3      | 2025-09-10 | 2025-09-15 | false   |

  @api @regression
  Scenario: Verify booking dates validation
    When User creates a new booking with checkout date "2025-07-17" and checkin date "2025-07-18"
    Then User should receive validation response from API
    And Response should indicate checkout date is after checkin date

  @api
  Scenario: Handle concurrent booking requests
    When User creates multiple bookings with same room simultaneously
    Then API should process requests appropriately
    And Response status codes should be validated

  @api @regression
  Scenario: Partial booking information - Missing required fields
    When User creates a booking with incomplete information:
      | firstname | John    |
      | lastname  | Doe     |
      | roomid    | 1       |
    Then API response should indicate missing required fields
    And Error response should be returned

  # ==================== Contact API Scenarios ====================

  @api @contact @regression
  Scenario: Contact form missing required field - name
    When User submits a contact form with the following details:
      | email   | test@example.com  |
      | phone   | 07358480688       |
      | subject | Test             |
      | message | Testing missing name |
    Then API response status code should be 400
    And Response should indicate name is required
    And Error message should contain "name"

  @api @contact @regression
  Scenario Outline: Submit contact forms with different data variations
    When User submits a contact form with the following details:
      | name    | <name>      |
      | email   | <email>     |
      | phone   | <phone>     |
      | subject | <subject>   |
      | message | <message>   |
    Then API response status code should be <statusCode>
    And Response should be processed accordingly

    Examples:
      | name          | email                | phone       | subject         | message              | statusCode |
      | John Doe      | john@example.com     | 07358480685 | General Inquiry | Question about rooms | 201        |
      | Jane Smith    | jane@example.com     | 07358480686 | Booking Help    | Need assistance      | 201        |
      | Bob Johnson   | invalid.email        | 07358480687 | Contact Test    | Testing invalid      | 400        |
      | Alice Brown   | alice@example.com    | 123         | Phone Test      | Invalid phone        | 400        |

  @api @contact
  Scenario: Delete contact submission
    When User submits a contact form with the following details:
      | name    | Delete Test User  |
      | email   | delete@example.com |
      | phone   | 07358480695       |
      | subject | Delete Test       |
      | message | Testing deletion   |
    And User stores the contact submission ID
    And User deletes the contact submission using stored ID
    Then API response status code should be 200 or 204
    And Contact submission should be removed

  @api @contact @regression
  Scenario: Update contact submission status
    When User submits a contact form with the following details:
      | name    | Status Update User |
      | email   | status@example.com |
      | phone   | 07358480696       |
      | subject | Status Test       |
      | message | Testing status update |
    And User stores the contact submission ID
    And User updates contact submission status to "reviewed"
    Then API response status code should be 200
    And Contact submission status should be updated to "reviewed"

  # ==================== UI and API Consistency Testing ====================

  @ui @api @integration
  Scenario: Verify user is able to send message and data is consistent in UI and API
    Given User launches the application
    When User clicks the Contact tab
    And User enters contact details:
      | Name    | John Doe      |
      | Email   | john@test.com |
      | Phone   | 9876543210    |
      | Message | Hello Test    |
    And User clicks on Submit button
    Then User should see the submitted details on UI

    When User retrieves the contact details using API
    Then API response should contain the same submitted data

    And UI data should match with API response
