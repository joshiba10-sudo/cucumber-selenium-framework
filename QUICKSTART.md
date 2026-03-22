# Quick Start Guide

## Get Started in 5 Minutes

### 1. Fresh Setup
```bash
# Clone the repository
git clone <repository-url>
cd CucumberSeleniumFramework

# Install dependencies
mvn clean install
```

### 2. Configure Application
Edit `src/test/resources/config/application.properties`:
```properties
base.url=https://your-application-url.com
browser=chrome
headless=false
```

### 3. Create Your First Test

#### Step 1: Create Feature File
Create `src/test/resources/features/03_YourFeature.feature`:
```gherkin
Feature: Your Feature Description

  @smoke
  Scenario: Your first test scenario
    Given User is on the login page
    When User performs login with "testuser" and "password123"
    Then User should be logged in successfully
```

#### Step 2: Create Page Object
Create `src/test/java/com/automation/framework/pages/YourPage.java`:
```java
package com.automation.framework.pages;

import org.openqa.selenium.By;

public class YourPage extends BasePage {
    
    private By element = By.id("elementId");
    
    public void yourMethod() {
        LOGGER.info("Performing action");
        click(element);
    }
}
```

#### Step 3: Create Step Definition
Create `src/test/java/com/automation/framework/stepDefinitions/YourStepDefinitions.java`:
```java
package com.automation.framework.stepDefinitions;

import com.automation.framework.pages.YourPage;
import io.cucumber.java.en.*;

public class YourStepDefinitions {
    
    private YourPage yourPage = new YourPage();
    
    @When("Your step definition")
    public void your_step() {
        yourPage.yourMethod();
    }
}
```

### 4. Run Your Tests

#### Run All Tests
```bash
mvn clean test
```

#### Run Specific Feature
```bash
mvn test -Dcucumber.features=src/test/resources/features/03_YourFeature.feature
```

#### Run with Different Browser
```bash
mvn clean test -Dbrowser=firefox
```

#### Run Headless Mode
```bash
mvn clean test -Dheadless=true
```

### 5. View Results
- **HTML Report**: Open `reports/cucumber-report.html`
- **Screenshots**: Check `reports/screenshots/` directory
- **Logs**: Check `reports/logs/automation_test.log`

---

## Common Commands

### Maven Commands
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn clean test

# Run specific test class
mvn test -Dtest=TestRunner

# Skip tests during build
mvn clean install -DskipTests

# View dependencies
mvn dependency:tree

# Update dependencies
mvn dependency:resolve
```

### Cucumber Commands
```bash
# Run specific tag
mvn test -Dcucumber.filter.tags="@smoke"

# Run multiple tags
mvn test -Dcucumber.filter.tags="@smoke or @regression"

# Skip specific tag
mvn test -Dcucumber.filter.tags="not @skip"

# Dry run (validate steps only)
mvn test -Dcucumber.dryRun=true
```

### Grid/Parallel Execution
```bash
# Update testng.xml parallel attribute
# parallel="methods" or parallel="tests" or parallel="classes"

# Run with specific thread count
mvn test -Dparallel.threads=4
```

---

## Project Structure Quick Reference

```
src/test/java/com/automation/framework/
├── base/           → Driver setup/teardown
├── hooks/          → Before/After methods
├── pages/          → Page Object classes
├── stepDefinitions → Step implementations
├── utilities/      → Helper utilities
└── runners/        → Test execution runners

src/test/resources/
├── features/       → Gherkin test scenarios
├── config/         → Configuration files
└── testdata/       → Test data (Excel, JSON)
```

---

## Useful Tips

### 1. Using Configuration
```java
String url = ConfigReader.getProperty("base.url");
int timeout = ConfigReader.getIntProperty("implicity_wait", 10);
```

### 2. Explicit Waits
```java
WaitUtility wait = new WaitUtility(driver);
WebElement element = wait.waitForElementToBeClickable(By.id("element"));
```

### 3. Taking Screenshots
```java
ScreenshotUtility screenshot = new ScreenshotUtility();
screenshot.captureScreenshot("test_name");
```

### 4. Reading Excel Data
```java
List<Map<String, String>> data = TestDataUtility.readDataFromExcel(
    "src/test/resources/testdata/data.xlsx", "Sheet1"
);
```

### 5. Logging
```java
LOGGER.info("Information message");
LOGGER.warn("Warning message");
LOGGER.error("Error message", exception);
```

---

## Common Issues & Solutions

### WebDriver Not Found
```bash
mvn clean install -U  # Update dependencies
```

### Tests Not Running
```bash
# Check feature file path
# Verify glue path in TestRunner
# Ensure pom.xml has required plugins
```

### Screenshot Not Saving
```bash
# Create directory
mkdir -p reports/screenshots

# Check write permissions
ls -la reports/
```

### Cucumber Steps Not Found
```bash
# Verify package names match glue path
# Check step definition annotations (@Given, @When, @Then)
# Clear Maven cache: rm -rf ~/.m2/repository
```

---

## Next Steps

1. ✅ Understand Page Object Model
2. ✅ Write your first feature file
3. ✅ Create corresponding step definitions
4. ✅ Run tests and verify reports
5. ✅ Integrate with Azure DevOps
6. ✅ Set up CI/CD pipeline

---

## Resources

- 📖 [Full Documentation](README.md)
- 🧪 [Sample Features](src/test/resources/features/)
- 📦 [Example Page Objects](src/test/java/com/automation/framework/pages/)
- 🔧 [Configuration File](src/test/resources/config/application.properties)

---

**Happy Testing! 🚀**
