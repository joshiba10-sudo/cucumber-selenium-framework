# Cucumber Selenium Automation Framework

## Overview

This is a **Production-Ready BDD Automation Framework** built using:
- **Language**: Java 11
- **Build Tool**: Maven
- **Test Framework**: Cucumber (Behavior-Driven Development)
- **UI Automation**: Selenium WebDriver 4
- **Design Pattern**: Page Object Model (POM)
- **Test Runner**: TestNG
- **Reporting**: Extent Reports + Cucumber Reports
- **CI/CD**: Azure DevOps Pipelines
- **Logging**: Log4j2
- **Data Handling**: Excel (Apache POI) & JSON

---

## 📁 Project Structure

```
CucumberSeleniumFramework/
├── src/
│   ├── test/
│   │   ├── java/
│   │   │   └── com/automation/framework/
│   │   │       ├── base/                     # Base classes
│   │   │       │   └── BaseClass.java       
│   │   │       ├── hooks/                    # Cucumber hooks
│   │   │       │   └── Hooks.java           
│   │   │       ├── pages/                    # Page Object Model
│   │   │       │   ├── BasePage.java        
│   │   │       │   ├── LoginPage.java       
│   │   │       │   └── HomePage.java        
│   │   │       ├── stepDefinitions/         # Step implementations
│   │   │       │   ├── LoginStepDefinitions.java
│   │   │       │   └── HomeStepDefinitions.java
│   │   │       ├── utilities/               # Utility classes
│   │   │       │   ├── ConfigReader.java    
│   │   │       │   ├── ScreenshotUtility.java
│   │   │       │   ├── WaitUtility.java     
│   │   │       │   ├── TestDataUtility.java 
│   │   │       │   └── RetryAnalyzer.java   
│   │   │       └── runners/                 # Test runners
│   │   │           └── TestRunner.java      
│   │   └── resources/
│   │       ├── features/                    # Gherkin feature files
│   │       │   ├── 01_Login.feature        
│   │       │   └── 02_HomePage.feature     
│   │       ├── config/                      # Configuration files
│   │       │   ├── application.properties   
│   │       │   ├── log4j2.xml              
│   │       │   ├── cucumber.properties      
│   │       │   ├── extent-config.xml        
│   │       │   └── extent-report.properties
│   │       └── testdata/                    # Test data files
│   │           └── (Excel, JSON files)      
│   └── main/java/                           # (Optional) Source code
├── .pipelines/
│   ├── azure-pipelines.yml                 # Full CI/CD pipeline
│   └── azure-pipelines-basic.yml           # Basic CI/CD pipeline
├── reports/                                 # Generated test reports
│   ├── screenshots/                         # Captured screenshots
│   └── logs/                                # Test logs
├── pom.xml                                  # Maven configuration
├── testng.xml                               # TestNG configuration
└── README.md                                # This file
```

---

## 🚀 Prerequisites

### System Requirements
- **Java**: JDK 11 or higher
- **Maven**: 3.6.0 or higher
- **Git**: For version control
- **Browser Drivers**: Handled automatically by WebDriverManager

### Installation Steps

#### 1. Install Java JDK
```bash
# Verify Java installation
java -version

# Verify Maven installation
mvn -version
```

#### 2. Clone Repository
```bash
git clone <repository-url>
cd CucumberSeleniumFramework
```

#### 3. Install Dependencies
```bash
# Download all required dependencies
mvn clean install

# Or just compile without running tests
mvn clean compile
```

---

## ⚙️ Configuration

### Application Properties
Edit `src/test/resources/config/application.properties`:

```properties
# Browser Configuration
browser=chrome                    # chrome, firefox, edge
headless=false                    # true for headless mode
implicity_wait=10                 # Implicit wait in seconds
pageload_timeout=30               # Page load timeout
script_timeout=20                 # JavaScript timeout

# URLs
base.url=https://www.example.com
api.base.url=https://api.example.com
environment=qa                    # qa, staging, production

# Screenshot & Reports
screenshot.path=./reports/screenshots/
report.path=./reports/

# Test Data
testdata.path=src/test/resources/testdata/

# Retry Configuration
retry.count=2                      # Number of retries for failed tests
retry.delay=1000                   # Delay in milliseconds
```

---

## 🏗️ Framework Architecture

### 1. **Base Class** (`BaseClass.java`)
- Initializes WebDriver instances
- Manages driver lifecycle (setup/teardown)
- Provides common properties (implicit wait, timeouts)
- Supports multiple browsers (Chrome, Firefox, Edge)

**Example**:
```java
// Initialize driver
BaseClass.initializeDriver();

// Get driver instance
WebDriver driver = BaseClass.getDriver();

// Teardown
BaseClass.tearDown();
```

### 2. **Hooks** (`Hooks.java`)
- **@Before**: Runs before each scenario
  - Initializes WebDriver
  - Navigates to base URL
  
- **@After**: Runs after each scenario
  - Captures screenshots on failure
  - Closes WebDriver
  - Logs scenario status

**Example**:
```java
@Before
public void setUp(Scenario scenario) {
    BaseClass.initializeDriver();
    BaseClass.navigateToApplication();
}

@After
public void tearDown(Scenario scenario) {
    if (scenario.isFailed()) {
        screenshotUtility.captureScreenshot(scenario.getName());
    }
    BaseClass.tearDown();
}
```

### 3. **Page Object Model** (POM)
- **BasePage**: Contains common page methods
  - Click, type, getText, getAttribute, scrollToElement, etc.
  - Uses WaitUtility for explicit waits
  
- **Specific Pages**: LoginPage, HomePage, etc.
  - Define locators for page elements
  - Implement page-specific methods

**Example**:
```java
public class LoginPage extends BasePage {
    
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    
    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }
}
```

### 4. **Step Definitions**
- Implements Cucumber steps (Given, When, Then)
- Uses page objects for interactions
- Contains assertions and logging

**Example**:
```java
@When("User performs login with {string} and {string}")
public void user_performs_login(String username, String password) {
    loginPage.login(username, password);
}

@Then("User should be logged in successfully")
public void user_should_be_logged_in_successfully() {
    Assert.assertTrue(homePage.isHomePageLoaded());
}
```

### 5. **Utilities**

#### ConfigReader
```java
String url = ConfigReader.getProperty("base.url");
int timeout = ConfigReader.getIntProperty("implicity_wait", 10);
boolean headless = ConfigReader.getBooleanProperty("headless", false);
```

#### WaitUtility
```java
WaitUtility wait = new WaitUtility(driver);
WebElement element = wait.waitForElementToBeVisible(locator);
wait.waitForElementToBeClickable(locator, 15);  // Custom timeout
```

#### ScreenshotUtility
```java
ScreenshotUtility screenshot = new ScreenshotUtility();
String path = screenshot.captureScreenshot("test_name");
```

#### TestDataUtility
```java
List<Map<String, String>> data = TestDataUtility.readDataFromExcel(
    "src/test/resources/testdata/data.xlsx", "Sheet1"
);
```

#### RetryAnalyzer
```java
// Applied via @Test annotation
@Test(retryAnalyzer = RetryAnalyzer.class)
public void testLoginFunctionality() {
    // Test code
}
```

---

## 📝 Feature Files & Step Definitions

### Writing Feature Files
```gherkin
Feature: User Login Functionality

  Background:
    Given User is on the login page

  @smoke @regression
  Scenario: User should login successfully with valid credentials
    When User enters "testuser" username
    And User enters "password123" password
    And User clicks the login button
    Then User should be logged in successfully

  @regression
  Scenario Outline: Multiple user login scenarios
    When User performs login with "<username>" and "<password>"
    Then User should be logged in successfully

    Examples:
      | username  | password     |
      | user1     | pass123      |
      | user2     | pass456      |
```

### Tags Usage
- `@smoke` - Smoke test suite
- `@regression` - Regression test suite
- `@sanity` - Sanity checks
- `@critical` - Critical tests

---

## 🧪 Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run with Specific Browser
```bash
mvn clean test -Dbrowser=firefox -Dheadless=false
```

### Run Specific Feature
```bash
mvn test -Dcucumber.features=src/test/resources/features/01_Login.feature
```

### Run Tests with Tags
```bash
# Smoke tests
mvn test -Dcucumber.features=src/test/resources/features --cucumber.filter.tags="@smoke"

# Regression tests
mvn test -Dcucumber.filter.tags="@regression"

# Run only not @skip
mvn test -Dcucumber.filter.tags="not @skip"
```

### Run Tests in Parallel
```bash
mvn test -Dgroups="smoke" -Dparallel.threads=4
```

## 📊 Reports & Screenshots

### Generated Reports Location
```
reports/
├── cucumber-report.html         # Cucumber HTML Report
├── cucumber.json                # JSON Report
├── cucumber.xml                 # XML Report (for CI/CD)
├── screenshots/                 # Failed test screenshots
└── logs/
    ├── automation_test.log      # All logs
    └── errors.log               # Error logs only
```

### View Reports
1. **Cucumber HTML Report**: Open `reports/cucumber-report.html` in browser
2. **Extent Report**: Check generated HTML files in reports directory
3. **TestNG Report**: Located in `test-output/` directory

---

## 🔧 Azure DevOps Integration

### Setup Azure Pipeline

#### Option 1: Basic Pipeline (Recommended for beginners)
```yaml
# File: azure-pipelines.yml
trigger:
  - main

pool:
  vmImage: 'windows-latest'

steps:
  - task: Maven@4
    inputs:
      mavenPomFile: 'pom.xml'
      mavenGoals: 'clean test'

  - task: PublishTestResults@2
    inputs:
      testResultsFormat: 'JUnit'
      testResultsFiles: '**/TEST-*.xml'
```

#### Option 2: Advanced Pipeline (Full CI/CD)
Use `.pipelines/azure-pipelines.yml` which includes:
- Build stage
- Test stage with parallel execution
- Report generation
- Artifact publishing
- Email notifications

### Configure in Azure DevOps

1. **Create Pipeline**:
   - Go to Azure DevOps → Pipelines → New Pipeline
   - Select your repository
   - Choose "Existing Azure Pipelines YAML file"
   - Select `.pipelines/azure-pipelines.yml`

2. **Set Pipeline Variables**:
   - Go to Pipeline → Edit → Variables
   - Add: `browserType` = `chrome`
   - Add: `environment` = `qa`

3. **Publish Test Results**:
   - Results automatically published to "Tests" section
   - Screenshots available in pipeline artifacts

---

## ✅ Best Practices

### 1. Page Object Model
```java
// ✅ Good
public class LoginPage extends BasePage {
    private By usernameField = By.id("username");
    
    public void enterUsername(String username) {
        type(usernameField, username);
    }
}

// ❌ Avoid
public void loginDirectly() {
    driver.findElement(By.id("username")).sendKeys("user");
    driver.findElement(By.id("password")).sendKeys("pass");
}
```

### 2. Explicit Waits
```java
// ✅ Good
WebElement element = waitUtility.waitForElementToBeClickable(locator);
element.click();

// ❌ Avoid
Thread.sleep(5000);  // Hard waits
driver.findElement(locator).click();
```

### 3. Logging
```java
// ✅ Good
LOGGER.info("Entering username: " + username);
LOGGER.error("Error clicking element", exception);

// ❌ Avoid
System.out.println("Username entered");
```

### 4. Feature File Format
```gherkin
// ✅ Good
Scenario: User can login with valid credentials
  When User enters valid username
  And User enters valid password
  Then User should see dashboard

// ❌ Avoid
Scenario: Test login
  When I click on username
  And I type text
  Then something happens
```

---

## 🐛 Troubleshooting

### Issue: WebDriver Not Found
**Solution**:
```bash
# WebDriverManager handles this automatically
# If still failing, run:
mvn clean compile
mvn dependency:resolve
```

### Issue: Tests Timeout
**Solution**: Update `application.properties`:
```properties
implicity_wait=15
pageload_timeout=45
script_timeout=30
```

### Issue: Screenshot Not Captured
**Solution**: Ensure directory exists and permissions are set:
```bash
mkdir -p reports/screenshots
chmod 755 reports/screenshots
```

### Issue: Cucumber Steps Not Found
**Solution**: Verify glue path in TestRunner:
```java
@CucumberOptions(
    glue = {"com.automation.framework.stepDefinitions", "com.automation.framework.hooks"}
)
```

---

## 📚 Additional Resources

### Key Dependencies
- **Selenium WebDriver**: UI automation
- **Cucumber**: BDD framework
- **TestNG**: Test management
- **Log4j2**: Logging
- **Apache POI**: Excel operations
- **REST Assured**: API testing
- **Extent Reports**: Enhanced reporting

### External Links
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Cucumber.io](https://cucumber.io/)
- [TestNG Documentation](https://testng.org/doc/)
- [Log4j2 Guide](https://logging.apache.org/log4j/2.x/)
- [Azure Pipelines Documentation](https://docs.microsoft.com/azure/devops/pipelines)

---

## 🤝 Contributing

### Development Guidelines
1. Create feature branch: `git checkout -b feature/new-feature`
2. Follow naming conventions:
   - Classes: PascalCase (e.g., `LoginPage.java`)
   - Methods: camelCase (e.g., `performLogin()`)
   - Constants: ALL_CAPS (e.g., `TIMEOUT_VALUE`)
3. Write meaningful commit messages
4. Create Pull Request with description

### Code Review Checklist
- ✅ Code follows naming conventions
- ✅ POM structure is maintained
- ✅ Logging is implemented
- ✅ Exception handling is proper
- ✅ No hardcoded values
- ✅ Tests pass locally

---

## 📞 Support

For issues or questions:
1. Check troubleshooting section
2. Review logs in `reports/logs/`
3. Check screenshots in `reports/screenshots/`
4. Contact QA Team

---

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2024 | Initial framework release with POM, Cucumber, Selenium |

---

**Last Updated**: March 2024  
**Framework Version**: 1.0.0  
**Maintained By**: QA Automation Team
