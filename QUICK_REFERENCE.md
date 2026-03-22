# API Testing - Quick Reference Card

## 🚀 Get Started in 3 Steps

```bash
# 1. Navigate to project
cd CucumberSeleniumFramework

# 2. Run tests
mvn test -Dcucumber.options="--tags @api"

# 3. View report
open reports/cucumber-report.html  # Mac
start reports/cucumber-report.html # Windows
firefox reports/cucumber-report.html # Linux
```

---

## 📋 All Test Commands

| Goal | Command |
|------|---------|
| Run all tests | `mvn clean test` |
| Run API tests only | `mvn test -Dcucumber.options="--tags @api"` |
| Run smoke tests | `mvn test -Dcucumber.options="--tags @smoke"` |
| Run regression | `mvn test -Dcucumber.options="--tags @regression"` |
| Run one scenario | `mvn test -Dcucumber.options="--name 'Get list of all rooms'"` |
| Dry run (no execute) | `mvn test --dry-run` |
| Parallel (4 threads) | `mvn test -Dparallel=true -DthreadCount=4` |

---

## 📝 Gherkin Step Examples

### Request Steps
```gherkin
When User requests to get list of all rooms
When User requests to get room details for roomid "1"
When User requests to check room availability with checkin "2025-07-17" and checkout "2025-07-18"
When User authenticates as admin with username "admin" and password "password"
When User creates a new booking with following details:
  | firstname   | John         |
  | email       | john@test.com |
  | roomid      | 1            |
```

### Response Steps
```gherkin
Then API response status code should be 200
Then API response status code should be 201 or 202
Then Response should contain list of rooms
Then Response should contain booking ID
Then Room details should contain "roomid"
Then Booking should show depositpaid as true
```

---

## 🔧 API Methods Available

```java
// GET
apiUtility.get("/room");
apiUtility.getWithQueryParams("/room", "checkin", "2025-07-17", "checkout", "2025-07-18");
apiUtility.getWithHeader("/booking/1", "Authorization", "Bearer token");

// POST
apiUtility.post("/booking", body);
apiUtility.postFormData("/auth/login", formData);
apiUtility.postWithHeader("/booking", body, "Referer", "https://...");

// PUT
apiUtility.put("/booking/1", updatedBody);
apiUtility.putWithHeader("/booking/1", body, "Authorization", "Bearer token");

// DELETE
apiUtility.delete("/booking/1");
apiUtility.deleteWithBody("/booking/1", body);
apiUtility.deleteWithHeader("/booking/1", "Authorization", "Bearer token");

// PATCH
apiUtility.patch("/booking/1", body);

// HEAD
apiUtility.head("/room");

// Validation
apiUtility.validateStatusCode(response, 200);
apiUtility.extractValueFromResponse(response, "bookingid");
apiUtility.validateResponseContains(response, "booking");
```

---

## 📊 14 Test Scenarios at a Glance

| # | Scenario | Endpoint | Method | Tags |
|---|----------|----------|--------|------|
| 1 | Get rooms list | /api/room | GET | @smoke |
| 2 | Get room by ID | /api/room/1 | GET | @smoke |
| 3 | Check availability | /api/room?checkin={}&checkout={} | GET | @regression |
| 4 | Create booking | /api/booking | POST | @smoke |
| 5 | Create with deposit | /api/booking | POST | @regression |
| 6 | Get booking details | /api/booking?roomid=1 | GET | @regression |
| 7 | Update booking | /api/booking/{id} | PUT | @api |
| 8 | Get report | /api/report | GET | @regression |
| 9 | Admin auth | /api/auth/login | POST | @smoke |
| 10 | Delete booking | /api/booking/{id} | DELETE | @regression |
| 11 | Multi-room test | /api/booking | POST | @regression |
| 12 | Date validation | /api/booking | POST | @regression |
| 13 | Concurrent test | /api/booking | POST | @api |
| 14 | Missing fields | /api/booking | POST | @regression |

---

## 🎯 Key Endpoints

```
GET    /api/room           → List rooms
GET    /api/room/{id}      → Room details
GET    /api/room?checkin={}&checkout={}  → Availability
POST   /api/booking        → Create booking
GET    /api/booking?roomid={id}  → Booking details
PUT    /api/booking/{id}   → Update booking
DELETE /api/booking/{id}   → Delete booking
GET    /api/report         → Booking report
POST   /api/auth/login     → Admin login
```

---

## ⚙️ Configuration File Locations

| Item | Location |
|------|----------|
| API URL | `src/test/resources/config/application.properties` |
| Feature Files | `src/test/resources/features/` |
| Step Definitions | `src/test/java/.../stepDefinitions/` |
| Reports | `reports/` |
| Logs | `reports/logs/` |

---

## 🔍 View Results

```bash
# HTML Report (Best view)
reports/cucumber-report.html

# JSON Report (Machine readable)
reports/cucumber.json

# Test Logs
reports/logs/test.log

# JUnit Report
reports/cucumber.xml
```

---

## ✅ Pre-Flight Checklist

Before running tests:
```
☐ Java 11+ installed: java -version
☐ Maven 3.6+ installed: mvn -version
☐ Internet connection active
☐ API accessible: curl https://automationintesting.online/api/room
☐ Navigate to project: cd CucumberSeleniumFramework
☐ Dependencies resolved: mvn dependency:resolve (first time only)
```

---

## 🐛 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| Connection refused | Check internet, verify API URL in config |
| 404 Not Found | Check endpoint path, verify ID exists |
| 400 Bad Request | Check request body format, verify dates |
| 401 Unauthorized | Verify credentials, check auth endpoint |
| 500 Error | Wait 30 seconds, retry, check server |
| Step not found | Check feature file syntax, rebuild project |
| No report generated | Check build completed, verify reports/ exists |
| Out of memory | Increase JVM: `mvn -Xmx1024m test` |

---

## 📚 Documentation Quick Links

| Document | Purpose |
|----------|---------|
| [API_TESTING_QUICKSTART.md](API_TESTING_QUICKSTART.md) | Quick reference & examples |
| [API_TEST_SCENARIOS.md](API_TEST_SCENARIOS.md) | Complete scenario documentation |
| [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) | Technical implementation details |
| [POSTMAN_TO_AUTOMATION_MAPPING.md](POSTMAN_TO_AUTOMATION_MAPPING.md) | Postman ↔ Test mapping |
| [TROUBLESHOOTING_FAQ.md](TROUBLESHOOTING_FAQ.md) | Issues & solutions |
| [README_API_IMPLEMENTATION.md](README_API_IMPLEMENTATION.md) | Complete setup guide |

---

## 📁 New Files Summary

```
✨ NEW FILES CREATED:
├── src/test/resources/features/03_BookingAPI.feature
├── src/test/java/.../stepDefinitions/BookingAPIStepDefinitions.java
├── src/test/java/.../utilities/APIUtility.java
├── API_TESTING_QUICKSTART.md
├── API_TEST_SCENARIOS.md
├── IMPLEMENTATION_SUMMARY.md
├── POSTMAN_TO_AUTOMATION_MAPPING.md
├── TROUBLESHOOTING_FAQ.md
└── README_API_IMPLEMENTATION.md

✏️ MODIFIED FILES:
├── src/test/java/.../hooks/Hooks.java
└── src/test/resources/config/application.properties
```

---

## 🎨 Test Data Reference

### Valid Booking Data
```
Room Types:
  ID 1 → Single (£100/night)
  ID 2 → Double (£150/night)
  ID 3 → Suite (£225/night)

Admin Credentials:
  Username: admin
  Password: password

Date Format:
  YYYY-MM-DD (e.g., 2025-07-17)

Email Format:
  valid@domain.com

Phone Format:
  10-11 digits (e.g., 07358480685)

Required Fields:
  ✓ roomid (integer)
  ✓ firstname (string)
  ✓ lastname (string)
  ✓ email (valid format)
  ✓ phone (digits)
  ✓ depositpaid (boolean)
  ✓ bookingdates.checkin (date)
  ✓ bookingdates.checkout (date)
```

---

## ⏱️ Performance Baseline

| Scenario | Avg Time |
|----------|----------|
| Single test | 2-5 seconds |
| All smoke tests | 30 seconds |
| All API tests | 60-90 seconds |
| Full suite | 2-3 minutes |

---

## 🚦 Execution Flow Diagram

```
┌─ Run Command
│
├─ Maven builds project
│
├─ Cucumber loads features
│
├─ For each @api scenario:
│  ├─ Hook.Before (skip browser init)
│  ├─ Execute steps
│  ├─ Validate responses
│  └─ Hook.After (no screenshots)
│
├─ Generate reports
│  ├─ HTML report
│  ├─ JSON report
│  └─ Logs
│
└─ BUILD SUCCESS ✅
```

---

## 🔐 API URL Configuration

```properties
# CORRECT:
api.base.url=https://automationintesting.online/api

# WRONG (will fail):
api.base.url=https://automationintesting.online/api/  # trailing slash
api.base.url=http://automationintesting.online/api     # http not https
api.base.url=https://example.com/api                   # wrong domain
```

---

## 💡 Pro Tips

1. **Use Tags for Focused Testing**
   ```bash
   mvn test -Dcucumber.options="--tags @smoke"  # Quick validation
   ```

2. **Store Data for Reuse**
   ```java
   // In step definitions
   this.bookingId = response.jsonPath().getString("bookingid");
   // Use in next step
   ```

3. **Extract Complex Values**
   ```java
   List<String> rooms = response.jsonPath().getList("$");
   String nested = response.jsonPath().getString("data[0].value");
   ```

4. **Add Custom Headers**
   ```java
   apiUtility.getWithHeader("/endpoint", "Custom-Header", "value");
   ```

5. **Increase Timeout for Slow Networks**
   ```properties
   api.timeout=10000  # 10 seconds instead of 5
   ```

---

## 🆘 Emergency Commands

```bash
# Clean everything and rebuild
mvn clean install

# Run with maximum verbosity (troubleshooting)
mvn test -e -X -Dcucumber.options="--name 'Get list'"

# Skip tests but compile
mvn clean compile -DskipTests

# Check dependencies are downloaded
mvn dependency:resolve

# View dependency tree
mvn dependency:tree
```

---

## 📞 Support Resources

### Online Documentation
- Cucumber: https://cucumber.io/docs/cucumber/
- RestAssured: https://rest-assured.io/
- Maven: https://maven.apache.org/

### Local Resources
1. Read documentation files (see links above)
2. Check test logs: `reports/logs/`
3. Review feature files: `src/test/resources/features/`
4. Examine step definitions: `BookingAPIStepDefinitions.java`

### Diagnostic Steps
```bash
# 1. Verify connectivity
curl -i https://automationintesting.online/api/room

# 2. Check configuration
cat src/test/resources/config/application.properties | grep api

# 3. Run single test with verbose output
mvn test -Dcucumber.options="--name 'Get list of all rooms'" -e -X

# 4. Check recent logs
tail -50 reports/logs/test.log
```

---

## ✨ Quick Feature Overview

| Feature | Status | Details |
|---------|--------|---------|
| GET requests | ✅ | With query params, headers |
| POST requests | ✅ | JSON body, form data |
| PUT/PATCH requests | ✅ | With custom headers |
| DELETE requests | ✅ | With/without body |
| Response validation | ✅ | Status codes, body content |
| JSON extraction | ✅ | JSONPath expressions |
| Error handling | ✅ | Comprehensive logging |
| Data-driven tests | ✅ | Scenario outlines, tables |
| Parallel execution | ✅ | Configurable threads |
| HTML reports | ✅ | Auto-generated after run |

---

## 🎯 Success Indicators

- ✅ All tests compile without errors
- ✅ Tests run and complete successfully
- ✅ HTML report generated in 60 seconds
- ✅ 200 status code for room endpoints
- ✅ 201 status code for booking creation
- ✅ 404 for invalid endpoints (expected)
- ✅ Detailed logs visible in reports/logs/

---

## 🔄 Regular Maintenance

```bash
# Weekly
mvn clean test -Dcucumber.options="--tags @smoke"

# Monthly
mvn clean test && mvn dependency:update-properties

# Before release
mvn clean test
mvn dependency:tree
mvn verify
```

---

## 📊 Expected Test Results

```
Running: mvn test -Dcucumber.options="--tags @smoke"

Results:
  Scenarios:  5 passed
  Steps:     25 passed
  Skipped:    0
  Duration:  ~30 seconds
  Status:    BUILD SUCCESS ✅
```

---

## 🎓 Learning Path

1. **Beginner**: Run smoke tests, view HTML report
2. **Intermediate**: Add new test scenario to feature file
3. **Advanced**: Create custom step definitions, use data tables
4. **Expert**: Integrate with CI/CD, configure parallel execution

---

**Ready to Test? Run this now:**
```bash
cd CucumberSeleniumFramework && mvn test -Dcucumber.options="--tags @api"
```

**Questions? Check the documentation files or review test logs!** 📖

---

*Last Updated: 2026-03-20*
*API Base URL: https://automationintesting.online/api*
*Framework: Cucumber 7.14.0 + RestAssured 5.3.2*
