# Framework Documentation Index

Welcome to the Cucumber Selenium Automation Framework documentation hub! 

## 📚 Documentation Files

### Getting Started
- **[README.md](README.md)** - Complete framework documentation
  - Overview and architecture
  - Setup and installation
  - Configuration guide
  - Running tests
  - Azure DevOps integration

- **[QUICKSTART.md](QUICKSTART.md)** - Quick start guide (5 minutes)
  - Fresh setup
  - First test creation
  - Common commands
  - Tips and tricks

### Development Guides
- **[BEST_PRACTICES.md](BEST_PRACTICES.md)** - Best practices and guidelines
  - Code style guidelines
  - POM best practices
  - Step definition best practices
  - Feature file best practices
  - Locator strategies
  - Wait strategies
  - Error handling

- **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** - Common issues and solutions
  - WebDriver issues
  - Timeout issues
  - Configuration issues
  - Cucumber issues
  - Reporting issues
  - Browser issues
  - Maven issues
  - Debugging tips

### Framework Reference
- **[FRAMEWORK_STRUCTURE.md](FRAMEWORK_STRUCTURE.md)** - Detailed framework structure (this file)

---

## 🏗️ Framework Structure Overview

```
CucumberSeleniumFramework/
│
├── 📄 Documentation (Start here!)
│   ├── README.md                    ← Complete documentation
│   ├── QUICKSTART.md                ← Get running in 5 minutes
│   ├── BEST_PRACTICES.md            ← Follow these guidelines
│   ├── TROUBLESHOOTING.md           ← When something goes wrong
│   └── INDEX.md                     ← This file
│
├── 🔧 Configuration
│   ├── pom.xml                      ← Maven configuration
│   ├── testng.xml                   ← TestNG configuration
│   └── .gitignore                   ← Git ignore rules
│
├── 📁 Source Code - src/test/java/com/automation/framework/
│   ├── base/
│   │   └── BaseClass.java           ← Driver setup/teardown
│   │
│   ├── hooks/
│   │   └── Hooks.java               ← Before/After hooks
│   │
│   ├── pages/                       ← Page Object Model
│   │   ├── BasePage.java            ← Base page with common methods
│   │   ├── LoginPage.java           ← Sample page object
│   │   ├── HomePage.java            ← Sample page object
│   │   └── TemplatePage.java        ← Template for new pages
│   │
│   ├── stepDefinitions/             ← Step implementations
│   │   ├── LoginStepDefinitions.java
│   │   ├── HomeStepDefinitions.java
│   │   └── TemplateStepDefinitions.java ← Template for new steps
│   │
│   ├── utilities/                   ← Helper utilities
│   │   ├── ConfigReader.java        ← Read configuration
│   │   ├── ScreenshotUtility.java   ← Capture screenshots
│   │   ├── WaitUtility.java         ← Explicit waits
│   │   ├── TestDataUtility.java     ← Excel/JSON handling
│   │   ├── RetryAnalyzer.java       ← Retry mechanism
│   │   └── CommonUtilities.java     ← Additional utilities
│   │
│   └── runners/
│       └── TestRunner.java          ← Test execution runner
│
├── 📁 Test Resources - src/test/resources/
│   │
│   ├── features/                    ← Gherkin feature files
│   │   ├── 01_Login.feature         ← Login feature
│   │   └── 02_HomePage.feature      ← Home page feature
│   │
│   ├── config/                      ← Configuration files
│   │   ├── application.properties   ← Main configuration
│   │   ├── log4j2.xml              ← Logging configuration
│   │   ├── cucumber.properties      ← Cucumber config
│   │   ├── extent-config.xml        ← Extent report config
│   │   └── extent-report.properties ← Report properties
│   │
│   └── testdata/                    ← Test data files
│       └── testdata.json            ← Sample test data
│
├── 📁 CI/CD Pipeline - .pipelines/
│   ├── azure-pipelines.yml          ← Full CI/CD pipeline
│   └── azure-pipelines-basic.yml    ← Basic CI/CD pipeline
│
├── 📁 Reports (Generated after test run)
│   ├── cucumber-report.html         ← HTML report
│   ├── cucumber.json                ← JSON report
│   ├── cucumber.xml                 ← JUnit XML report
│   ├── screenshots/                 ← Failed test screenshots
│   └── logs/
│       ├── automation_test.log      ← All logs
│       └── errors.log               ← Error logs only
│
└── 📁 Project Root Files
    ├── pom.xml                      ← Maven project config
    ├── testng.xml                   ← TestNG configuration
    ├── .gitignore                   ← Git ignore rules
    ├── README.md                    ← Main documentation
    ├── QUICKSTART.md                ← Quick start guide
    ├── BEST_PRACTICES.md            ← Best practices
    ├── TROUBLESHOOTING.md           ← Troubleshooting guide
    └── INDEX.md                     ← This file
```

---

## 🚀 Quick Links by Task

### I want to...

#### **Get started quickly**
👉 Read [QUICKSTART.md](QUICKSTART.md) - 5 minute setup

#### **Understand the full framework**
👉 Read [README.md](README.md) - Complete documentation

#### **Learn best practices**
👉 Read [BEST_PRACTICES.md](BEST_PRACTICES.md) - Code guidelines

#### **Fix a problem**
👉 Read [TROUBLESHOOTING.md](TROUBLESHOOTING.md) - Common issues

#### **Create a new test**
👉 Steps:
1. Create feature file in `src/test/resources/features/`
2. Create page object from `TemplatePage.java`
3. Create step definitions from `TemplateStepDefinitions.java`
4. Run: `mvn clean test`

#### **Create a new page object**
👉 Copy and modify `src/test/java/com/automation/framework/pages/TemplatePage.java`

#### **Create new step definitions**
👉 Copy and modify `src/test/java/com/automation/framework/stepDefinitions/TemplateStepDefinitions.java`

#### **Configure application settings**
👉 Edit `src/test/resources/config/application.properties`

#### **Add logging**
👉 `src/test/resources/config/log4j2.xml`

#### **Setup Azure DevOps pipeline**
👉 Use `.pipelines/azure-pipelines.yml`

#### **View test reports**
👉 After test run, open `reports/cucumber-report.html`

---

## 📊 File Organization by Category

### Configuration Files
```
application.properties     - Main configuration
log4j2.xml                - Logging setup
cucumber.properties       - Cucumber configuration
extent-config.xml         - Report configuration
extent-report.properties  - Report properties
```

### Page Objects
```
BasePage.java             - Base class with common methods
LoginPage.java            - Login page (example)
HomePage.java             - Home page (example)
TemplatePage.java         - Template for new pages ⭐
```

### Step Definitions
```
LoginStepDefinitions.java       - Login steps (example)
HomeStepDefinitions.java        - Home steps (example)
TemplateStepDefinitions.java    - Template for new steps ⭐
```

### Utility Classes
```
ConfigReader.java         - Read configuration
ScreenshotUtility.java    - Capture screenshots
WaitUtility.java          - Explicit waits
TestDataUtility.java      - Read Excel/JSON data
RetryAnalyzer.java        - Retry failed tests
CommonUtilities.java      - Additional utilities
```

### Feature Files (Test Scenarios)
```
01_Login.feature          - Login tests
02_HomePage.feature       - Home page tests
```

### Pipeline Configuration
```
azure-pipelines.yml           - Full CI/CD pipeline
azure-pipelines-basic.yml     - Simplified pipeline
```

---

## 🎯 Common Development Workflows

### Workflow 1: Add a New Test Feature

1. **Create Feature File**
   ```gherkin
   # src/test/resources/features/03_YourFeature.feature
   Feature: Your Feature
   
   @smoke
   Scenario: Your scenario
     Given User is on login page
     When User does something
     Then Something happens
   ```

2. **Create Page Object** (if needed)
   - Copy `TemplatePage.java`
   - Rename to `YourPage.java`
   - Update locators and methods

3. **Create Step Definitions**
   - Copy `TemplateStepDefinitions.java`
   - Rename to `YourStepDefinitions.java`
   - Implement the steps

4. **Run Tests**
   ```bash
   mvn clean test
   ```

5. **View Reports**
   ```bash
   # Open report
   reports/cucumber-report.html
   ```

### Workflow 2: Fix a Failing Test

1. **Review Test Failure**
   ```bash
   # Check logs
   reports/logs/automation_test.log
   
   # Check screenshot
   reports/screenshots/
   ```

2. **Identify Issue**
   - Locator problem?
   - Wait issue?
   - Logic error?

3. **Fix & Retry**
   ```bash
   # Use debugging approaches from TROUBLESHOOTING.md
   mvn clean test -Dcucumber.features=src/test/resources/features/03_YourFeature.feature
   ```

### Workflow 3: Add Test Data

**For Excel Data:**
```bash
# Create/update file
src/test/resources/testdata/data.xlsx

# Use in tests
List<Map<String, String>> data = TestDataUtility.readDataFromExcel(
    "src/test/resources/testdata/data.xlsx", "Sheet1"
);
```

**For JSON Data:**
```bash
# Create/update file
src/test/resources/testdata/testdata.json

# Use in tests
String json = new String(Files.readAllBytes(
    Paths.get("src/test/resources/testdata/testdata.json")
));
```

---

## 🔄 Development Best Practices

### Do's ✅
- Use Page Object Model
- Add proper logging
- Use explicit waits
- Keep steps simple
- DRY principle (Don't Repeat Yourself)
- Independent tests
- Meaningful assertions
- Test data externalization

### Don'ts ❌
- Hardcode values
- Use Thread.sleep()
- Generic exceptions
- Test dependencies
- Unreadable feature files
- Too many steps in one scenario
- No assertions
- Credentials in code

---

## 📞 Support Resources

### Documentation
- 📖 [README.md](README.md) - Full documentation
- ⚡ [QUICKSTART.md](QUICKSTART.md) - Quick setup
- 💡 [BEST_PRACTICES.md](BEST_PRACTICES.md) - Guidelines
- 🔧 [TROUBLESHOOTING.md](TROUBLESHOOTING.md) - Help

### External Resources
- 🌐 [Selenium Documentation](https://www.selenium.dev/documentation/)
- 🥒 [Cucumber Documentation](https://cucumber.io/)
- ☕ [TestNG Documentation](https://testng.org/)
- 📊 [Log4j2 Guide](https://logging.apache.org/log4j/2.x/)
- 🔵 [Azure Pipelines](https://docs.microsoft.com/en-us/azure/devops/pipelines/)

### Contact
- **Framework Lead**: [Your Name/Email]
- **QA Team**: [Team Email]
- **Slack Channel**: #test-automation

---

## 📝 Release Notes

### Version 1.0.0 (Initial Release)
- ✅ Complete framework setup
- ✅ Page Object Model implementation
- ✅ Cucumber integration
- ✅ Selenium WebDriver 4
- ✅ TestNG runner
- ✅ Extent Reports
- ✅ Azure DevOps integration
- ✅ Log4j2 logging
- ✅ Retry mechanism
- ✅ Comprehensive documentation

---

## 🎓 Learning Path Recommendation

### For New Users
1. Read [QUICKSTART.md](QUICKSTART.md) (5 minutes)
2. Run sample tests (5 minutes)
3. Create a simple test (15 minutes)
4. Review [BEST_PRACTICES.md](BEST_PRACTICES.md) (30 minutes)
5. Read [README.md](README.md) (1 hour)

### For Intermediate Users
1. Explore framework code
2. Create complex tests with data-driven scenarios
3. Implement custom utilities
4. Set up Azure Pipeline

### For Advanced Users
1. Extend framework with new features
2. Optimize performance
3. Integrate with other tools
4. Maintain and improve framework

---

## 🆘 Frequently Encountered Issues

| Issue | Solution |
|-------|----------|
| WebDriver not found | See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) → WebDriver Issues |
| Tests timeout | See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) → Timeout Issues |
| Steps not found | See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) → Cucumber Issues |
| No reports | See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) → Reporting Issues |
| Stale element | See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) → WebDriver Issues |

---

## 📋 Documentation Checklist

When creating new documentation, ensure:
- ✅ Clear and concise language
- ✅ Code examples provided
- ✅ Links to related docs
- ✅ Updated release notes
- ✅ TOC included
- ✅ Version number noted
- ✅ Last updated date

---

## 🔗 Quick Navigation

| Document | Purpose | Read Time |
|----------|---------|-----------|
| [README.md](README.md) | Complete guide | 45 min |
| [QUICKSTART.md](QUICKSTART.md) | Get started | 5 min |
| [BEST_PRACTICES.md](BEST_PRACTICES.md) | Guidelines | 30 min |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | Fix issues | 20 min |
| [INDEX.md](INDEX.md) | This file | 10 min |

---

**Last Updated:** March 2024  
**Framework Version:** 1.0.0  
**Documentation Status:** Complete ✅

---

Happy Testing! 🚀
