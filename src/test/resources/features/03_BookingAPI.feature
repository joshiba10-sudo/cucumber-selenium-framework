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

  @api @positive
  Scenario: Check room availability for single night stay
    When User requests to check room availability with checkin "2025-07-20" and checkout "2025-07-21"
    Then API response status code should be 200
    And Response should contain available rooms

  @api @positive
  Scenario: Check room availability for far future dates
    When User requests to check room availability with checkin "2026-12-15" and checkout "2026-12-20"
    Then API response status code should be 200
    And Response should contain available rooms

  @api @negative
  Scenario: Check room availability with checkout before checkin
    When User requests to check room availability with checkin "2025-07-25" and checkout "2025-07-20"
    Then API response status code should be 400
    And Response should indicate invalid date range

  @api @negative
  Scenario: Check room availability with same checkin and checkout date
    When User requests to check room availability with checkin "2025-07-20" and checkout "2025-07-20"
    Then API response status code should be 400
    And Response should indicate minimum stay requirement

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

  @api @positive
  Scenario: Create booking for extended stay (more than 7 days)
    When User creates a new booking with following details:
      | firstname   | Extended       |
      | lastname    | Stay           |
      | email       | extended@test.com |
      | phone       | 07358480691    |
      | roomid      | 1              |
      | checkin     | 2025-07-17     |
      | checkout    | 2025-07-31     |
      | depositpaid | true           |
    Then API response status code should be 201
    And Response should contain booking ID
    And Response should indicate extended booking

  @api @positive
  Scenario: Create multiple bookings sequentially and verify all exist
    When User creates a new booking with following details:
      | firstname   | First       |
      | lastname    | Booking     |
      | email       | first@test.com |
      | phone       | 07358480692 |
      | roomid      | 1          |
      | checkin     | 2025-10-01 |
      | checkout    | 2025-10-03 |
      | depositpaid | false      |
    And User stores the booking ID from response
    And User creates a new booking with following details:
      | firstname   | Second      |
      | lastname    | Booking     |
      | email       | second@test.com |
      | phone       | 07358480693 |
      | roomid      | 2          |
      | checkin     | 2025-10-05 |
      | checkout    | 2025-10-07 |
      | depositpaid | true       |
    Then API response status code should be 201
    And First booking should still exist in the system

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

  @api @negative
  Scenario: Create booking with invalid room ID
    When User creates a new booking with following details:
      | firstname   | John       |
      | lastname    | Doe        |
      | email       | john@email.com |
      | phone       | 07358480685    |
      | roomid      | 9999       |
      | checkin     | 2025-07-17 |
      | checkout    | 2025-07-18 |
      | depositpaid | false      |
    Then API response status code should be 400
    And Response should indicate invalid room ID

  @api @negative
  Scenario: Create booking with past dates
    When User creates a new booking with following details:
      | firstname   | John       |
      | lastname    | Doe        |
      | email       | john@email.com |
      | phone       | 07358480685    |
      | roomid      | 1          |
      | checkin     | 2020-01-01 |
      | checkout    | 2020-01-02 |
      | depositpaid | false      |
    Then API response status code should be 400
    And Response should indicate invalid date

  @api @negative
  Scenario: Create booking with invalid email format
    When User creates a new booking with following details:
      | firstname   | John       |
      | lastname    | Doe        |
      | email       | invalid-email |
      | phone       | 07358480685    |
      | roomid      | 1          |
      | checkin     | 2025-07-17 |
      | checkout    | 2025-07-18 |
      | depositpaid | false      |
    Then API response status code should be 400
    And Response should indicate invalid email format

  @api @negative
  Scenario: Create booking with invalid phone format
    When User creates a new booking with following details:
      | firstname   | John       |
      | lastname    | Doe        |
      | email       | john@email.com |
      | phone       | invalid    |
      | roomid      | 1          |
      | checkin     | 2025-07-17 |
      | checkout    | 2025-07-18 |
      | depositpaid | false      |
    Then API response status code should be 400
    And Response should indicate invalid phone format

  @api @negative
  Scenario: Admin authentication with invalid credentials
    When User authenticates as admin with username "admin" and password "wrongpassword"
    Then API response status code should be 401
    And Response should indicate unauthorized access
    And Response should not contain authentication token

  @api @negative
  Scenario: Update booking with invalid booking ID
    When User attempts to update booking with ID "99999" with following details:
      | firstname   | James      |
      | lastname    | Updated    |
      | depositpaid | true       |
    Then API response status code should be 404
    And Response should indicate booking not found

  @api @negative
  Scenario: Delete booking with invalid booking ID
    When User attempts to delete booking with ID "99999"
    Then API response status code should be 404
    And Response should indicate booking not found

  @api @negative
  Scenario: Get room details with invalid room ID
    When User requests to get room details for roomid "9999"
    Then API response status code should be 404
    And Response should indicate room not found

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

  @api @contact @negative
  Scenario: Contact form missing required field - email
    When User submits a contact form with the following details:
      | name    | John Doe          |
      | phone   | 07358480688       |
      | subject | Test Subject      |
      | message | Testing missing email |
    Then API response status code should be 400
    And Response should indicate email is required
    And Error message should contain "email"

  @api @contact @negative
  Scenario: Contact form missing required field - phone
    When User submits a contact form with the following details:
      | name    | John Doe          |
      | email   | john@example.com  |
      | subject | Test Subject      |
      | message | Testing missing phone |
    Then API response status code should be 400
    And Response should indicate phone is required
    And Error message should contain "phone"

  @api @contact @negative
  Scenario: Contact form with message less than minimum length
    When User submits a contact form with the following details:
      | name    | John Doe          |
      | email   | john@example.com  |
      | phone   | 07358480688       |
      | subject | Test Subject      |
      | message | short |
    Then API response status code should be 400
    And Response should indicate message is too short

  @api @contact @negative
  Scenario: Contact form with invalid email format
    When User submits a contact form with the following details:
      | name    | John Doe          |
      | email   | invalid.email     |
      | phone   | 07358480688       |
      | subject | Test Subject      |
      | message | This is a valid message with more than 20 characters |
    Then API response status code should be 400
    And Response should indicate invalid email format

  @api @contact @positive
  Scenario: Contact form with special characters in message
    When User submits a contact form with the following details:
      | name    | John Doe & Co.    |
      | email   | john@example.com  |
      | phone   | 07358480688       |
      | subject | Test @Subject#123 |
      | message | Testing with special chars: !@#$%^&*() and more text |
    Then API response status code should be 201
    And Response should contain success confirmation

  @api @contact @positive
  Scenario: Contact form with maximum length message
    When User submits a contact form with the following details:
      | name    | John Doe          |
      | email   | john@example.com  |
      | phone   | 07358480688       |
      | subject | Test Subject      |
      | message | This is a very long message that contains a lot of details about the inquiry being placed by the user and should be accepted as a valid contact form submission with complete information for follow up purposes |
    Then API response status code should be 201
    And Response should contain success confirmation

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

  @ui @api @integration @uiapi
  Scenario: Verify user is able to send message and data is consistent in UI and API
    Given User launches the application
    When User clicks the Contact tab
    And User enters contact details:
      | Name    | John Doe                                      |
      | Email   | john@test.com                                 |
      | Phone   | 09876543210                                   |
      | Subject | Test Subject 1234                             |
      | Message | Test Message should have above 20 characters |
    And User clicks on Submit button
    Then User should see the submitted details on UI

    When User retrieves the contact details using API
    Then API response should contain the same submitted data

    And UI data should match with API response
