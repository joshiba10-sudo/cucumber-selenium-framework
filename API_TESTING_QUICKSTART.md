# API Testing Quick Start Guide

## Overview
This framework now supports comprehensive REST API testing for the Shady Meadows B&B booking system using RestAssured and Cucumber.

## What's New

### New Files Created
1. **Feature File**: `src/test/resources/features/03_BookingAPI.feature`
   - 14 comprehensive test scenarios covering all booking API endpoints
   - Scenario outlines for data-driven testing
   - Tags for selective test execution

2. **Step Definitions**: `src/test/java/com/automation/framework/stepDefinitions/BookingAPIStepDefinitions.java`
   - Implements BDD steps for API testing
   - Handles request/response validation
   - Manages booking data throughout test execution

3. **API Utility**: `src/test/java/com/automation/framework/utilities/APIUtility.java`
   - Wrapper for RestAssured
   - Methods for GET, POST, PUT, PATCH, DELETE, HEAD requests
   - Response validation and extraction utilities
   - Comprehensive logging

### Updated Files
1. **Hooks**: `src/test/java/com/automation/framework/hooks/Hooks.java`
   - Modified to skip browser initialization for API tests
   - Detects `@api` tag and skips unnecessary setup
   
2. **Configuration**: `src/test/resources/config/application.properties`
   - Updated `api.base.url` to point to booking API
   - Configured timeout settings for API calls

## Quick Usage

### Example 1: Run All API Tests
```bash
mvn test -Dcucumber.options="--tags @api"
```

### Example 2: Run Specific Scenario
```bash
mvn test -Dcucumber.options="--name 'Create a new booking with valid details'"
```

### Example 3: Run Smoke Tests Only
```bash
mvn test -Dcucumber.options="--tags @smoke"
```

## Test Scenarios Summary

| Scenario | HTTP Method | Endpoint | Status |
|----------|-------------|----------|--------|
| List all rooms | GET | /api/room | ✅ |
| Get room by ID | GET | /api/room/{id} | ✅ |
| Check availability | GET | /api/room?checkin={d}&checkout={d} | ✅ |
| Create booking | POST | /api/booking | ✅ |
| Create with deposit | POST | /api/booking | ✅ |
| Get bookings by room | GET | /api/booking?roomid={id} | ✅ |
| Update booking | PUT | /api/booking/{id} | ✅ |
| Get report | GET | /api/report | ✅ |
| Admin login | POST | /api/auth/login | ✅ |
| Delete booking | DELETE | /api/booking/{id} | ✅ |
| Batch bookings | POST | /api/booking | ✅ |
| Date validation | POST | /api/booking | ✅ |
| Concurrent requests | POST | /api/booking | ✅ |
| Missing fields | POST | /api/booking | ✅ |

## Feature File Structure

The feature file follows standard Gherkin syntax:

```gherkin
Feature: Shady Meadows B&B Booking API Testing

  Background:
    Given Base API URL is set to "https://automationintesting.online/api"

  @smoke @api @regression
  Scenario: Get list of all available rooms
    When User requests to get list of all rooms
    Then API response status code should be 200
```

## Key Step Definitions

### Request Steps

```gherkin
# Simple GET requests
When User requests to get list of all rooms
When User requests to get room details for roomid "1"

# GET with query parameters
When User requests to check room availability with checkin "2025-07-17" and checkout "2025-07-18"

# POST requests with data table
When User creates a new booking with following details:
  | firstname   | John       |
  | lastname    | Doe        |
  | email       | john@email.com |
  | phone       | 07358480685    |
  | roomid      | 1          |
  | checkin     | 2025-07-17 |
  | checkout    | 2025-07-18 |
  | depositpaid | false      |

# Authentication
When User authenticates as admin with username "admin" and password "password"
```

### Response Validation Steps

```gherkin
# Status code validation
Then API response status code should be 200
Then API response status code should be 201 or 202

# Content validation
Then Response should contain list of rooms
Then Response should contain booking confirmation
Then Response should contain booking ID
Then Room details should contain "roomid"

# Data validation
Then Booking should show depositpaid as true
Then Available rooms list should not be empty
```

## APIUtility Methods

### GET Requests
```java
// Simple GET
Response response = apiUtility.get("/room");

// GET with query parameters
Response response = apiUtility.getWithQueryParams("/room", "checkin", "2025-07-17", "checkout", "2025-07-18");

// GET with custom headers
Response response = apiUtility.getWithHeader("/booking/1", "Authorization", "Bearer token");
```

### POST Requests
```java
// POST with JSON body
Map<String, Object> body = new HashMap<>();
body.put("firstname", "John");
Response response = apiUtility.post("/booking", body);

// POST with form data
Map<String, String> formData = new HashMap<>();
formData.put("username", "admin");
Response response = apiUtility.postFormData("/auth/login", formData);

// POST with custom headers
Response response = apiUtility.postWithHeader("/booking", body, "Referer", "https://...");
```

### PUT Requests
```java
// Simple PUT
Response response = apiUtility.put("/booking/1", updatedBody);

// PUT with custom headers
Response response = apiUtility.putWithHeader("/booking/1", updatedBody, "Authorization", "Bearer token");
```

### DELETE Requests
```java
// Simple DELETE
Response response = apiUtility.delete("/booking/1");

// DELETE with custom headers
Response response = apiUtility.deleteWithHeader("/booking/1", "Authorization", "Bearer token");
```

### Response Validation
```java
// Validate status code
boolean isValid = apiUtility.validateStatusCode(response, 200);

// Extract value from response
String bookingId = apiUtility.extractValueFromResponse(response, "bookingid");

// Check if response contains text
boolean contains = apiUtility.validateResponseContains(response, "booking");

// Get response body
String body = apiUtility.getResponseBody(response);

// Get response header
String contentType = apiUtility.getResponseHeader(response, "Content-Type");
```

## Test Execution Examples

### Run All Tests
```bash
mvn clean test
```

### Run with Specific Tag
```bash
# Only smoke tests
mvn test -Dcucumber.options="--tags @smoke"

# Only regression tests
mvn test -Dcucumber.options="--tags @regression"

# API tests excluding regression
mvn test -Dcucumber.options="--tags @api and not @regression"
```

### Run Specific Feature
```bash
mvn test -Dcucumber.options="src/test/resources/features/03_BookingAPI.feature"
```

### Parallel Execution
Update `testng.xml`:
```xml
<suite name="Tests" parallel="true" thread-count="5">
```

Then run:
```bash
mvn test -Dparallel=true
```

## Configuration Properties

Edit `src/test/resources/config/application.properties`:

```properties
# API Configuration
api.base.url=https://automationintesting.online/api
api.timeout=5000
api.connection.timeout=5000

# Logging
log.level=INFO
log.path=./reports/logs/

# Report
report.path=./reports/
```

## Reports

After test execution, reports are generated in:

1. **HTML Report**: `reports/cucumber-report.html`
   - Open in browser for detailed results
   - Includes step execution timeline

2. **JSON Report**: `reports/cucumber.json`
   - Machine-readable format
   - Can be processed by other tools

3. **ExtentReport**: `reports/` directory
   - Advanced reporting with charts
   - Dashboard view of test results

4. **Logs**: `reports/logs/*.log`
   - Detailed API request/response logs
   - Debug information

## Common Tasks

### Add New API Test Scenario

1. **Add scenario to feature file** (03_BookingAPI.feature):
```gherkin
@api
Scenario: My new API test
  When User makes some API call
  Then API response status code should be 200
```

2. **Add step definition** (BookingAPIStepDefinitions.java):
```java
@When("User makes some API call")
public void user_makes_api_call() {
    response = apiUtility.get("/endpoint");
}
```

3. **Add assertion** (BookingAPIStepDefinitions.java):
```java
@Then("API does something")
public void verify_api_behavior() {
    Assert.assertTrue(response.getStatusCode() == 200);
}
```

4. **Run the new test**:
```bash
mvn test -Dcucumber.options="--name 'My new API test'"
```

### Extract Data from Response

```java
// In step definition
String bookingId = response.jsonPath().getString("bookingid");
Integer roomId = response.jsonPath().getInt("roomid");
Boolean depositPaid = response.jsonPath().getBoolean("depositpaid");
List<String> rooms = response.jsonPath().getList("$");
```

### Handle Dynamic Test Data

```gherkin
Scenario: Create booking and verify
  When User creates a new booking with following details:
    | firstname | John |
    | roomid    | 1    |
  And User stores the booking ID from response
  When User requests to get booking details with roomid "1"
  Then API response status code should be 200
```

## Troubleshooting

### Test Fails with Connection Error
```
Connection refused or API unreachable
```
**Solution**: 
- Check internet connection
- Verify API URL in application.properties
- Ensure https://automationintesting.online is accessible

### Test Fails with Invalid Response
```
Response status code mismatch. Expected: 201 Actual: 400
```
**Solution**:
- Check test data format
- Verify required fields are provided
- Review API logs for error details

### Missing Required Fields Error
**Solution**:
- Review BookingAPIStepDefinitions.java prepareBookingRequestBody() method
- Ensure all mandatory fields are included in test data

## Best Practices

1. **Use Appropriate Tags**
   - `@smoke` - Quick sanity tests
   - `@regression` - Complete test coverage
   - `@api` - All API tests

2. **Test Data**
   - Use valid email formats: user@example.com
   - Use valid phone numbers: 10-11 digits
   - Ensure dates are in YYYY-MM-DD format
   - Update dates to future dates if tests fail

3. **Assertions**
   - Always verify response status code
   - Check response contains expected data
   - Log responses for debugging

4. **Maintenance**
   - Keep step definitions simple and reusable
   - Update tests when API changes
   - Archive old test data periodically

## Next Steps

1. Run initial test suite: `mvn clean test`
2. Review HTML report: `reports/cucumber-report.html`
3. Add additional test scenarios as needed
4. Integrate with CI/CD pipeline
5. Set up scheduled test runs

## Support Files

- **Full Documentation**: [API_TEST_SCENARIOS.md](API_TEST_SCENARIOS.md)
- **Feature File**: [03_BookingAPI.feature](src/test/resources/features/03_BookingAPI.feature)
- **Step Definitions**: [BookingAPIStepDefinitions.java](src/test/java/com/automation/framework/stepDefinitions/BookingAPIStepDefinitions.java)
- **API Utility**: [APIUtility.java](src/test/java/com/automation/framework/utilities/APIUtility.java)

---

**Happy Testing! 🚀**
