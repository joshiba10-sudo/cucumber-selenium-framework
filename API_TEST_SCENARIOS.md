# Booking API Test Scenarios Documentation

## Overview
This document describes all the automated test scenarios created for the Shady Meadows B&B Booking API. The test scenarios correspond to the API endpoints defined in the Postman collection (BookinginTesting.postman_collection.json).

## Test Architecture
- **Feature File**: `src/test/resources/features/03_BookingAPI.feature`
- **Step Definitions**: `src/test/java/com/automation/framework/stepDefinitions/BookingAPIStepDefinitions.java`
- **API Utility**: `src/test/java/com/automation/framework/utilities/APIUtility.java`
- **Base URL**: `https://automationintesting.online/api`

## Test Scenarios

### 1. Get List of All Rooms
**Tag**: `@smoke @api @regression`
**Endpoint**: GET `/api/room`
**Description**: Retrieves all available rooms from the bed & breakfast
**Expected Outcome**: 
- Status Code: 200
- Response contains a list of rooms with details

---

### 2. Get Room Details by Room ID
**Tag**: `@smoke @api @regression`
**Endpoint**: GET `/api/room/{roomid}`
**Description**: Retrieves specific room details by room ID
**Test Data**:
- Room ID: 1
**Expected Outcome**:
- Status Code: 200
- Response contains room information including room ID

---

### 3. Check Room Availability for Specific Dates
**Tag**: `@api @regression`
**Endpoint**: GET `/api/room?checkin={date}&checkout={date}`
**Description**: Checks availability of rooms for given check-in and check-out dates
**Test Data**:
- Check-in: 2025-07-17
- Check-out: 2025-07-18
**Expected Outcome**:
- Status Code: 200
- Response contains list of available rooms

---

### 4. Create a New Booking with Valid Details
**Tag**: `@smoke @api`
**Endpoint**: POST `/api/booking`
**Description**: Creates a new booking with valid guest and room information
**Test Data**:
- First Name: John
- Last Name: Doe
- Email: john@email.com
- Phone: 07358480685
- Room ID: 1
- Check-in: 2025-07-17
- Check-out: 2025-07-18
- Deposit Paid: false
**Expected Outcome**:
- Status Code: 201
- Response contains booking confirmation
- Booking ID is provided

---

### 5. Create Booking with Deposit Paid
**Tag**: `@api @regression`
**Endpoint**: POST `/api/booking`
**Description**: Creates a new booking with deposit payment flag set to true
**Test Data**:
- First Name: Jane
- Last Name: Smith
- Email: jane@email.com
- Phone: 07358480686
- Room ID: 2
- Check-in: 2025-08-01
- Check-out: 2025-08-05
- Deposit Paid: true
**Expected Outcome**:
- Status Code: 201
- Response contains booking ID
- Booking shows depositpaid flag as true

---

### 6. Get Booking Details by Room ID
**Tag**: `@api @regression`
**Endpoint**: GET `/api/booking?roomid={id}`
**Description**: Retrieves booking details for a specific room
**Test Data**:
- Room ID: 1
**Expected Outcome**:
- Status Code: 200
- Response contains booking information with dates

---

### 7. Update Existing Booking
**Tag**: `@api`
**Endpoint**: PUT `/api/booking/{bookingid}`
**Description**: Updates an existing booking with new information
**Test Data**:
- First Name: James
- Last Name: Updated
- Deposit Paid: true
- Check-in: 2026-03-09
- Check-out: 2026-03-10
**Expected Outcome**:
- Status Code: 200
- Updated booking response received

---

### 8. Get Booking Report
**Tag**: `@api @regression`
**Endpoint**: GET `/api/report`
**Description**: Retrieves booking report with statistics
**Expected Outcome**:
- Status Code: 200
- Response contains booking statistics and report data

---

### 9. Admin Authentication
**Tag**: `@api @smoke`
**Endpoint**: POST `/api/auth/login`
**Description**: Authenticates admin user and receives authentication token
**Test Data**:
- Username: admin
- Password: password
**Expected Outcome**:
- Status Code: 200
- Response contains authentication token

---

### 10. Delete Existing Booking
**Tag**: `@api @regression`
**Endpoint**: DELETE `/api/booking/{bookingid}`
**Description**: Creates a booking and then deletes it
**Test Data**:
- Standard booking details provided
**Expected Outcome**:
- Booking Creation Status: 201
- Deletion Status: 201 or 202

---

### 11. Create Multiple Bookings with Different Room Types
**Tag**: `@api @regression`
**Type**: Scenario Outline
**Endpoint**: POST `/api/booking`
**Description**: Tests creation of multiple bookings across different room types
**Test Data**:
| First Name | Last Name | Email       | Room ID | Check-in   | Check-out  | Deposit |
|------------|----------|-------------|---------|------------|------------|---------|
| John       | Single   | john@test.com   | 1       | 2025-07-17 | 2025-07-18 | false   |
| Jane       | Double   | jane@test.com   | 2       | 2025-08-01 | 2025-08-05 | true    |
| Tom        | Suite    | tom@test.com    | 3       | 2025-09-10 | 2025-09-15 | false   |

**Expected Outcome**:
- All bookings should be created successfully (Status: 201)
- Each response contains a booking ID

---

### 12. Verify Booking Dates Validation
**Tag**: `@api @regression`
**Endpoint**: POST `/api/booking`
**Description**: Validates that check-out date cannot be before check-in date
**Test Data**:
- Invalid dates: Check-out (2025-07-17) and Check-in (2025-07-18)
**Expected Outcome**:
- API should return validation error
- Response should indicate checkout date must be after checkin date

---

### 13. Handle Concurrent Booking Requests
**Tag**: `@api`
**Endpoint**: POST `/api/booking`
**Description**: Tests API behavior when receiving multiple concurrent booking requests
**Expected Outcome**:
- API processes requests appropriately
- Status codes are valid

---

### 14. Partial Booking Information - Missing Required Fields
**Tag**: `@api @regression`
**Endpoint**: POST `/api/booking`
**Description**: Tests API validation when required fields are missing
**Test Data**:
- First Name: John
- Last Name: Doe
- Room ID: 1
- Email: (missing)
- Phone: (missing)
**Expected Outcome**:
- Status Code: 400+ (Error)
- Response indicates missing required fields

---

## Running the Tests

### Run All Tests
```bash
mvn test
```

### Run Only API Tests
```bash
mvn test -Dcucumber.options="--tags @api"
```

### Run Only Smoke Tests
```bash
mvn test -Dcucumber.options="--tags @smoke"
```

### Run Only Regression Tests
```bash
mvn test -Dcucumber.options="--tags @regression"
```

### Run Specific Feature File
```bash
mvn test -Dcucumber.options="src/test/resources/features/03_BookingAPI.feature"
```

### Run Tests in Parallel
Update the `testng.xml` to enable parallel execution:
```xml
<suite name="Cucumber Automation Test Suite" verbose="2" parallel="true" thread-count="4">
```

---

## Configuration

### API Configuration Properties
Located in: `src/test/resources/config/application.properties`

```properties
# API Configuration  
api.base.url=https://automationintesting.online/api
api.timeout=5000
api.connection.timeout=5000
```

### Logging Configuration
- Log Level: INFO (configurable in `log4j2.xml`)
- Log Path: `./reports/logs/`
- All API requests and responses are logged

---

## Test Execution Flow

1. **Before Each Scenario**:
   - Hooks.java checks if scenario is tagged with `@api`
   - Skips browser initialization for API tests
   - Initializes RestAssured with base URL

2. **During Test Execution**:
   - APIUtility handles HTTP requests
   - Step definitions parse requests and build payloads
   - Responses are validated against expected outcomes

3. **After Each Scenario**:
   - API tests skip screenshot capture
   - Reports are generated in HTML and JSON format
   - Logs are archived

---

## Test Reporting

### Report Location
- HTML Report: `reports/cucumber-report.html`
- JSON Report: `reports/cucumber.json`
- JUnit Report: `reports/cucumber.xml`
- ExtentReport: `reports/` (generated automatically)

### Report Contents
- Test execution summary
- Pass/Fail status
- API response codes and payloads
- Detailed logs for each step
- Execution time

---

## API Endpoints Reference

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/room` | List all rooms |
| GET | `/api/room/{id}` | Get room details |
| GET | `/api/room?checkin=&checkout=` | Check availability |
| POST | `/api/booking` | Create booking |
| GET | `/api/booking?roomid={id}` | Get booking details |
| PUT | `/api/booking/{id}` | Update booking |
| DELETE | `/api/booking/{id}` | Delete booking |
| GET | `/api/report` | Get booking report |
| POST | `/api/auth/login` | Admin authentication |

---

## Common Test Data

### Valid Credentials
- Username: admin
- Password: password

### Sample Booking Data
- Room Types: Single (ID: 1), Double (ID: 2), Suite (ID: 3)
- Email Format: firstname@domain.com
- Phone Format: 10-11 digit numbers

---

## Troubleshooting

### Common Issues

1. **Connection Timeout**
   - Verify internet connection
   - Check API base URL in application.properties
   - Increase timeout in configuration if needed

2. **Authentication Failures**
   - Verify credentials in test data
   - Check admin panel is accessible

3. **Booking Validation Errors**
   - Ensure check-out date is after check-in date
   - Verify all required fields are provided
   - Check email and phone format

4. **Test Failures**
   - Review logs in `./reports/logs/`
   - Check API response in test report
   - Verify test data hasn't expired

---

## Best Practices

1. **Use Appropriate Tags**
   - Use `@smoke` for quick validation tests
   - Use `@regression` for comprehensive testing
   - Use `@api` to differentiate from UI tests

2. **Test Data Management**
   - Update date ranges regularly
   - Use valid email formats
   - Maintain consistent phone number formats

3. **Maintenance**
   - Review and update APIs if endpoint structure changes
   - Keep test scenarios aligned with business requirements
   - Archive old test reports regularly

---

## Integration with CI/CD

The test suite is configured to run in CI/CD pipelines:

### Jenkins Configuration
```groovy
stage('API Tests') {
    steps {
        sh 'mvn test -Dcucumber.options="--tags @api"'
        publishHTML([reportDir: 'reports', reportFiles: 'cucumber-report.html', reportName: 'API Test Report'])
    }
}
```

---

## Contact & Support

For issues or questions regarding these test scenarios:
1. Check the logs in `./reports/logs/`
2. Review the full test report in `reports/`
3. Verify API endpoints are accessible at https://automationintesting.online/api
4. Contact the automation team for further assistance
