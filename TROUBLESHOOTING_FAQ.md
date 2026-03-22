# API Testing - Troubleshooting & FAQ Guide

## Table of Contents
1. [Quick Setup Verification](#quick-setup-verification)
2. [Common Issues & Solutions](#common-issues--solutions)
3. [Frequently Asked Questions](#frequently-asked-questions)
4. [Verification Checklist](#verification-checklist)

---

## Quick Setup Verification

### Verify Installation
Run this command to verify everything is set up correctly:

```bash
mvn clean test -Dcucumber.options="--dry-run --tags @api" --version
```

This will:
- Download ALL dependencies
- Compile all test code
- Verify Cucumber feature file syntax
- Show Maven version

### First Time Setup
```bash
# 1. Clean any previous build
mvn clean

# 2. Download all dependencies
mvn dependency:resolve

# 3. Compile code
mvn compile

# 4. Run a single simple test
mvn test -Dcucumber.options="--name 'Get list of all available rooms'"
```

### Check Project Structure
Verify these files exist:
```
✓ src/test/resources/features/03_BookingAPI.feature
✓ src/test/java/com/automation/framework/stepDefinitions/BookingAPIStepDefinitions.java
✓ src/test/java/com/automation/framework/utilities/APIUtility.java
✓ src/test/java/com/automation/framework/hooks/Hooks.java (MODIFIED)
✓ src/test/resources/config/application.properties (MODIFIED)
```

---

## Common Issues & Solutions

### Issue 1: "API Unreachable / Connection Refused"

**Error Message**:
```
Connection refused: connect
Or
Unable to connect to automationintesting.online
```

**Causes**:
- Internet connection is down
- API URL is incorrect
- Firewall/Proxy blocking
- API server is down

**Solutions**:

1. **Check Internet Connection**
   ```bash
   ping automationintesting.online
   ```
   If `ping` fails, your internet is down.

2. **Verify API URL**
   Check in `src/test/resources/config/application.properties`:
   ```properties
   api.base.url=https://automationintesting.online/api
   ```
   
   Should NOT have:
   - Extra slashes: `https://automationintesting.online/api/`
   - Http instead of Https: `http://...`
   - Wrong domain: example.com, etc.

3. **Test URL Manually**
   ```bash
   # On Windows PowerShell
   Invoke-WebRequest -Uri "https://automationintesting.online/api/room" -UseBasicParsing
   
   # On Mac/Linux
   curl -i https://automationintesting.online/api/room
   ```
   
   Should return status 200-299

4. **Check Firewall/Proxy**
   - If on company network, check if HTTPS sites are blocked
   - Try disabling antivirus temporarily
   - Check VPN settings

5. **Wait for API Recovery**
   - The test API may occasionally go down for maintenance
   - Wait 5-10 minutes and retry

---

### Issue 2: "401 Unauthorized" or "403 Forbidden"

**Error Message**:
```
Status code:401
Or
Status code: 403
```

**Causes**:
- Invalid authentication credentials
- Missing authorization header
- Expired token
- Insufficient permissions

**Solutions**:

1. **Verify Credentials**
   Check admin login test:
   ```bash
   mvn test -Dcucumber.options="--name 'Admin authentication'"
   ```
   
   Credentials should be:
   - Username: `admin`
   - Password: `password`

2. **Check Authentication Step**
   In `BookingAPIStepDefinitions.java`:
   ```java
   @When("User authenticates as admin with username {string} and password {string}")
   public void user_authenticates_as_admin(String username, String password) {
       LOGGER.info("Authenticating with username: " + username);
       // Should succeed
   }
   ```

3. **Update Token if Needed**
   Some APIs require token in headers:
   ```java
   Response response = apiUtility.getWithHeader("/endpoint", "Authorization", "Bearer " + token);
   ```

---

### Issue 3: "400 Bad Request" or "422 Unprocessable Entity"

**Error Message**:
```
Status code: 400
Or
Status code: 422
```

**Causes**:
- Invalid request body format
- Missing required fields
- Invalid data types
- Invalid date format

**Solutions**:

1. **Check Request Body**
   For booking creation, must include:
   ```json
   {
     "roomid": 1,           // Integer, required
     "firstname": "string",  // String, required
     "lastname": "string",   // String, required
     "email": "valid@email", // Valid email, required
     "phone": "1234567890",  // String with digits, required
     "depositpaid": false,   // Boolean, required
     "bookingdates": {       // Object, required
       "checkin": "YYYY-MM-DD",   // Date format
       "checkout": "YYYY-MM-DD"   // Date format
     }
   }
   ```

2. **Verify Date Format**
   - Must be: `YYYY-MM-DD`
   - NOT: `MM/DD/YYYY` or `DD-MM-YYYY`
   - Date must be future: `booking checkout > booking checkin`

3. **Check Data Types**
   ```java
   // CORRECT:
   requestBody.put("roomid", 1);              // Integer
   requestBody.put("depositpaid", false);     // Boolean
   requestBody.put("firstname", "John");      // String
   
   // WRONG:
   requestBody.put("roomid", "1");            // String instead of Integer
   requestBody.put("depositpaid", "true");    // String instead of Boolean
   ```

4. **Sample Valid Request**
   ```gherkin
   When User creates a new booking with following details:
     | firstname   | John             |
     | lastname    | Doe              |
     | email       | john@example.com |  ← Valid email format
     | phone       | 07358480685      |  ← Numbers only
     | roomid      | 1                |  ← Valid room
     | checkin     | 2025-07-17       |  ← Future date
     | checkout    | 2025-07-18       |  ← After checkin
     | depositpaid | false            |  ← Boolean
   ```

---

### Issue 4: "404 Not Found"

**Error Message**:
```
Status code: 404
```

**Causes**:
- Wrong endpoint path
- BookingID doesn't exist
- RoomID doesn't exist
- Endpoint deprecated

**Solutions**:

1. **Verify Endpoint Path**
   Correct endpoints:
   ```
   ✓ /api/room          (GET)
   ✓ /api/room/1        (GET with ID)
   ✓ /api/booking       (POST, GET)
   ✓ /api/booking/1     (GET, PUT, DELETE with ID)
   ✓ /api/report        (GET)
   ✓ /api/auth/login    (POST)
   
   ✗ /booking          (missing /api)
   ✗ /api/bookings     (plural - wrong)
   ✗ /api/room/abc     (invalid ID)
   ```

2. **Check BookingID/RoomID**
   - IDs must be numeric
   - ID must exist in database
   - Check if ID was deleted in previous test

3. **Verify Endpoint Syntax**
   ```java
   // CORRECT:
   response = apiUtility.get("/api/room");         // Full path from root
   response = apiUtility.get("/room");             // Relative (uses baseURL)
   
   // WRONG:
   response = apiUtility.get("api/room");          // Missing leading slash
   response = apiUtility.get("/api//room");        // Double slash
   response = apiUtility.get("/api/room/");        // Trailing slash
   ```

---

### Issue 5: "500 Internal Server Error"

**Error Message**:
```
Status code: 500
```

**Causes**:
- Server-side error/bug
- Database connection issue
- Invalid query on server side
- Server timeout

**Solutions**:

1. **Check Server Status**
   ```bash
   # Visit in browser
   https://automationintesting.online/
   
   # Should show: "Welcome to Shady Meadows B&B"
   ```

2. **Wait and Retry**
   - Server might be restarting
   - Wait 30 seconds and retry
   ```bash
   mvn test -Dcucumber.options="--name 'Get list of all available rooms'"
   ```

3. **Check Request for Issues**
   Even if request seems valid, try with minimal data:
   ```java
   // Simplify request
   Map<String, Object> minimalRequest = new HashMap<>();
   minimalRequest.put("roomid", 1);
   minimalRequest.put("firstname", "Test");
   minimalRequest.put("lastname", "User");
   minimalRequest.put("email", "test@test.com");
   minimalRequest.put("phone", "1234567890");
   minimalRequest.put("depositpaid", false);
   
   Map<String, String> dates = new HashMap<>();
   dates.put("checkin", "2025-07-17");
   dates.put("checkout", "2025-07-18");
   minimalRequest.put("bookingdates", dates);
   ```

4. **Review Server Logs**
   If you have access to server logs, check for errors

---

### Issue 6: "Timeout / Response Takes Too Long"

**Error Message**:
```
Read timed out
Or
Connection timeout
```

**Causes**:
- Network latency
- Server is slow
- Timeout setting too low
- Too many concurrent requests

**Solutions**:

1. **Increase Timeout**
   Edit `src/test/resources/config/application.properties`:
   ```properties
   # Default 5000ms (5 seconds)
   api.timeout=10000           # Increase to 10 seconds
   api.connection.timeout=10000
   ```

2. **Check Network Speed**
   ```bash
   ping automationintesting.online
   # Should be < 100ms response time
   ```

3. **Reduce Concurrent Requests**
   Modify concurrency settings in `testng.xml`:
   ```xml
   <!-- From: -->
   <suite name="Tests" parallel="true" thread-count="10">
   
   <!-- To: -->
   <suite name="Tests" parallel="true" thread-count="2">
   ```

4. **Run Single Test**
   ```bash
   mvn test -Dcucumber.options="--name 'Get list of all available rooms'"
   # Single test should be faster
   ```

---

### Issue 7: "Step Definition Not Found"

**Error Message**:
```
Could not find glue code for step
You can implement this step using the annotation below
@When("...")
```

**Causes**:
- Feature file step doesn't match step definition
- Step definition file not in correct package
- Step definition method not public

**Solutions**:

1. **Check Glue Path**
   Verify in `TestRunner.java`:
   ```java
   @CucumberOptions(
       glue = {"com.automation.framework.stepDefinitions", "com.automation.framework.hooks"},
       // ...
   )
   ```

2. **Compare Step Text Exactly**
   In feature file:
   ```gherkin
   When User requests to get list of all rooms
   ```
   
   In step definition:
   ```java
   @When("User requests to get list of all rooms")
   public void user_requests_list_of_all_rooms() {
   ```
   
   Must match exactly (case-sensitive!)

3. **Check Step Definition Location**
   Must be in: `src/test/java/com/automation/framework/stepDefinitions/`
   
   File: `BookingAPIStepDefinitions.java`

4. **Verify Method is Public**
   ```java
   // CORRECT:
   @When("...")
   public void method_name() { }
   
   // WRONG:
   @When("...")
   private void method_name() { }  // Private - won't be found!
   ```

---

### Issue 8: "Feature File Not Loaded"

**Error Message**:
```
No scenarios found in feature file
Or
Feature file not detected
```

**Causes**:
- Feature file in wrong location
- Wrong file extension
- Syntax error in Gherkin

**Solutions**:

1. **Check File Location**
   Must be: `src/test/resources/features/03_BookingAPI.feature`
   
   Not:
   - Wrong path
   - Wrong extension (not .txt or .gherkin)
   - Wrong directory

2. **Verify File Syntax**
   Use Gherkin validator:
   ```bash
   # Check syntax is valid
   mvn test -Dcucumber.options="--dry-run"
   ```

3. **Check Feature File Starts Correctly**
   ```gherkin
   Feature: Shady Meadows B&B Booking API Testing
   
   Background:
       Given Base API URL is set to "https://automationintesting.online/api"
   
   @smoke @api @regression
   Scenario: Get list of all available rooms
       When User requests to get list of all rooms
       Then API response status code should be 200
   ```
   
   First line must be: `Feature: ...`

4. **Check Scenario Syntax**
   Every scenario must have:
   ```gherkin
   @tags (optional)
   Scenario: Scenario name
       Given/When/Then statements
   ```

---

### Issue 9: "Tests Pass but No Report Generated"

**Error Message**:
```
Tests run successfully but reports folder is empty
Or
Cannot open report
```

**Solutions**:

1. **Check Report Path**
   Reports should be in: `reports/`
   
   Should contain:
   - `cucumber-report.html`
   - `cucumber.json`
   - `cucumber.xml`

2. **Generate Report Explicitly**
   ```bash
   mvn clean test
   # Reports generated automatically
   ```

3. **Check Report Plugin in pom.xml**
   Must have:
   ```xml
   <plugin>
       <groupId>net.masterthought</groupId>
       <artifactId>maven-cucumber-reporting</artifactId>
   </plugin>
   ```

4. **Open HTML Report**
   ```bash
   # Windows:
   start reports/cucumber-report.html
   
   # Mac:
   open reports/cucumber-report.html
   
   # Linux:
   firefox reports/cucumber-report.html
   ```

---

### Issue 10: "Memory Error / Out of Memory"

**Error Message**:
```
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
```

**Solutions**:

1. **Increase JVM Memory**
   ```bash
   mvn -Xmx1024m -Xms512m test
   ```

2. **Or Set MAVEN_OPTS**
   ```bash
   # Windows PowerShell:
   $env:MAVEN_OPTS = "-Xmx1024m -Xms512m"
   mvn test
   
   # Mac/Linux:
   export MAVEN_OPTS="-Xmx1024m -Xms512m"
   mvn test
   ```

3. **Reduce Parallel Threads**
   In `testng.xml`:
   ```xml
   <suite ... thread-count="2" parallel="true">
   ```

---

## Frequently Asked Questions

### Q1: How do I run just one test scenario?
**A**: Use the `--name` filter:
```bash
mvn test -Dcucumber.options="--name 'Create a new booking'"
```

### Q2: How do I run tests with specific tags?
**A**: Use tags filter:
```bash
mvn test -Dcucumber.options="--tags @smoke"
mvn test -Dcucumber.options="--tags @api and @regression"
mvn test -Dcucumber.options="--tags @api and not @smoke"
```

### Q3: How do I run tests in parallel?
**A**: Edit `testng.xml`:
```xml
<suite name="Tests" parallel="true" thread-count="5" verbose="2">
```

### Q4: Where are the test reports?
**A**: In the `reports/` directory:
- HTML: `reports/cucumber-report.html`
- JSON: `reports/cucumber.json`
- Logs: `reports/logs/`

### Q5: How do I add a new test scenario?
**A**: 
1. Add scenario to `03_BookingAPI.feature`
2. Add step definition to `BookingAPIStepDefinitions.java`
3. Add assertion method in same file
4. Run: `mvn test -Dcucumber.options="--name 'Your scenario name'"`

### Q6: Can I run UI tests and API tests together?
**A**: Yes! All tests run:
```bash
mvn clean test  # Runs all tests
```

API tests have `@api` tag so they skip browser initialization automatically.

### Q7: How do I debug a failing test?
**A**: 
1. Check logs in: `reports/logs/`
2. Look at HTML report: `reports/cucumber-report.html`
3. Check response in test report
4. Run single test with verbose logging:
   ```bash
   mvn test -Dcucumber.options="--name 'Scenario name'" -e -X
   ```

### Q8: Can I use custom data in tests?
**A**: Yes, use scenario outlines or data drives in step definitions:
```gherkin
Scenario Outline: Test multiple bookings
  When User creates a new booking with following details:
    | firstname | <firstname> |
    | roomid    | <roomid>    |
  Then API response status code should be 201

  Examples:
    | firstname | roomid |
    | John      | 1      |
    | Jane      | 2      |
```

### Q9: How do I extend timeout for slow connections?
**A**: Edit properties:
```properties
api.timeout=10000           # 10 seconds
api.connection.timeout=10000
```

### Q10: What if API credentials change?
**A**: Update in step definition:
```java
@When("User authenticates as admin with username {string} and password {string}")
public void user_authenticates_as_admin(String username, String password) {
    // Update credentials here
```

Or use external test data file.

### Q11: How do I integrate with Jenkins?
**A**: Create Jenkinsfile:
```groovy
stage('API Tests') {
    steps {
        sh 'mvn clean test -Dcucumber.options="--tags @api"'
        publishHTML([
            reportDir: 'reports',
            reportFiles: 'cucumber-report.html',
            reportName: 'API Test Report'
        ])
    }
}
```

### Q12: Can I run tests without internet (offline)?
**A**: No. These tests require live API connectivity. For offline testing:
- Use WireMock to mock API
- Create standalone java server
- Use recorded responses

---

## Verification Checklist

Before running tests, verify:

### Pre-Requisites
- [ ] Java 11+ installed: `java -version`
- [ ] Maven 3.6+ installed: `mvn -version`
- [ ] Git installed: `git --version`
- [ ] Internet connection active

### Project Setup
- [ ] Project cloned/extracted
- [ ] `pom.xml` exists in root
- [ ] `src/test/resources/features/03_BookingAPI.feature` exists
- [ ] `src/test/java/com/automation/framework/stepDefinitions/BookingAPIStepDefinitions.java` exists
- [ ] `src/test/java/com/automation/framework/utilities/APIUtility.java` exists

### Configuration
- [ ] `src/test/resources/config/application.properties` has correct API URL
- [ ] `api.base.url=https://automationintesting.online/api` (NOT with trailing slash)
- [ ] Timeout values reasonable: 5000-10000 ms

### API Accessibility
- [ ] Can ping: `ping automationintesting.online`
- [ ] Can access in browser: https://automationintesting.online/
- [ ] Shows "Welcome to Shady Meadows B&B"

### Build Verification
- [ ] Dependencies resolve: `mvn dependency:resolve`
- [ ] Code compiles: `mvn compile`
- [ ] Tests load: `mvn test --dry-run`

### Test Execution
- [ ] Run sample test: `mvn test -Dcucumber.options="--name 'Get list of all available rooms'"`
- [ ] Test completes: "BUILD SUCCESS"
- [ ] Reports generated: `ls reports/`

---

## Getting Help

### If Something Doesn't Work:

1. **Check Logs First**
   ```bash
   cat reports/logs/*.log
   ```

2. **Run Diagnostic Test**
   ```bash
   mvn test -Dcucumber.options="--name 'Get list of all available rooms'" -e -X
   ```

3. **Verify Connectivity**
   ```bash
   curl -i https://automationintesting.online/api/room
   ```

4. **Check Documentation**
   - [API_TESTING_QUICKSTART.md](API_TESTING_QUICKSTART.md)
   - [API_TEST_SCENARIOS.md](API_TEST_SCENARIOS.md)
   - [POSTMAN_TO_AUTOMATION_MAPPING.md](POSTMAN_TO_AUTOMATION_MAPPING.md)

5. **Review Source Code**
   - BookingAPIStepDefinitions.java
   - APIUtility.java
   - 03_BookingAPI.feature

---

**Still stuck? Check the logs for specific error messages and refer to relevant solution above!**
