# Troubleshooting Guide

## Common Issues & Solutions

### 1. WebDriver Issues

#### Issue: "WebDriver not found" or "ChromeDriver not found"
```
Error: Could not find chromedriver in PATH
```

**Solution:**
```bash
# The framework uses WebDriverManager which handles this automatically
# If you still get this error:

# Option 1: Update dependencies
mvn clean install -U

# Option 2: Clear Maven cache
rm -rf ~/.m2/repository

# Option 3: Manually set WEBDRIVER_CHROME_DRIVER
export WEBDRIVER_CHROME_DRIVER=/path/to/chromedriver
```

#### Issue: "Unable to locate element"
```
NoSuchElementException: Unable to locate element
```

**Solution:**
1. **Verify locator is correct:**
   ```java
   // Use browser inspector (F12) to find element
   // Click on element, copy ID/class/xpath
   
   // Test locator in browser console
   // For XPath: $x("//button[@id='submit']")
   // For CSS: $$('.submit-button')
   ```

2. **Add explicit wait:**
   ```java
   // Instead of:
   click(submitButton);
   
   // Use:
   WebElement element = waitUtility.waitForElementToBeClickable(submitButton, 15);
   element.click();
   ```

3. **Verify element is in DOM:**
   ```java
   // Check if element exists before interacting
   if (isElementPresent(locator)) {
       click(locator);
   } else {
       LOGGER.error("Element not found in DOM");
   }
   ```

#### Issue: "Stale Element Reference"
```
StaleElementReferenceException: stale element reference
```

**Solution:**
```java
// ❌ Avoid: Element becomes stale
WebElement element = driver.findElement(locator);
Thread.sleep(1000);  // Page refresh or DOM change
element.click();     // Stale!

// ✅ Use: Refind element within wait
try {
    waitUtility.waitForElementToBeClickable(locator).click();
} catch (StaleElementReferenceException e) {
    // Retry
    waitUtility.waitForElementToBeClickable(locator).click();
}
```

---

### 2. Timeout Issues

#### Issue: "TimeoutException: Implicit wait timed out"
```
org.openqa.selenium.TimeoutException: Implicit wait timed out
```

**Solution:**

1. **Increase timeout in configuration:**
   ```properties
   # src/test/resources/config/application.properties
   implicity_wait=20
   pageload_timeout=45
   script_timeout=30
   ```

2. **Use custom timeout for specific wait:**
   ```java
   // Default timeout from config
   waitUtility.waitForElementToBeVisible(locator);
   
   // Custom timeout
   waitUtility.waitForElementToBeVisible(locator, 20);
   ```

3. **Verify element is actually on page:**
   ```java
   // Check page source
   LOGGER.info("Page source: " + driver.getPageSource());
   
   // Check if element is in iframe
   List<WebElement> frames = driver.findElements(By.tagName("iframe"));
   LOGGER.info("Number of iframes: " + frames.size());
   ```

---

### 3. Configuration Issues

#### Issue: "Property not found" warnings
```
WARN: Property not found: browser
```

**Solution:**
1. **Verify property exists in config file:**
   ```bash
   # Edit configuration file
   src/test/resources/config/application.properties
   
   # Check if key exists
   grep "browser" application.properties
   ```

2. **Clear property cache:**
   ```java
   // In test code
   ConfigReader.reloadProperties();
   ```

3. **Verify file is being read:**
   ```bash
   # Check if file is in correct location
   ls -la src/test/resources/config/
   
   # Verify it's included in Maven build
   cat pom.xml | grep resources
   ```

---

### 4. Cucumber Issues

#### Issue: "No scenarios run" or "Steps not found"
```
No scenarios run
0 Undefined steps
```

**Solution:**

1. **Verify feature file location:**
   ```bash
   # Should be in:
   src/test/resources/features/
   
   # Check if files exist
   find src -name "*.feature"
   ```

2. **Verify step definitions package:**
   ```java
   // In TestRunner, check glue path
   @CucumberOptions(
       glue = {"com.automation.framework.stepDefinitions",
               "com.automation.framework.hooks"}
   )
   
   // Verify package exists
   ls -la src/test/java/com/automation/framework/stepDefinitions/
   ```

3. **Check step definition annotations:**
   ```java
   // ✅ Correct
   @Given("User is on login page")
   public void user_is_on_login_page() { }
   
   // ❌ Wrong
   @given("User is on login page")  // lowercase
   public void user_is_on_login_page() { }
   ```

#### Issue: "Step undefined"
```
Undefined step: When User navigates to home page
```

**Solution:**

1. **Generate step definition template:**
   ```bash
   # Run test with --dry-run to see undefined steps
   mvn test -Dcucumber.dryRun=true
   
   # Copy undefined steps and create methods
   ```

2. **Match step definition exactly:**
   ```gherkin
   # Feature file
   When User navigates to "home" page
   ```
   
   ```java
   // Step definition - match exactly
   @When("User navigates to {string} page")
   public void user_navigates_to_page(String page) { }
   ```

---

### 5. Reporting Issues

#### Issue: "No report generated"
```
Reports folder is empty
```

**Solution:**

1. **Verify plugin configuration:**
   ```xml
   <!-- pom.xml should have -->
   <plugin>
       <groupId>io.cucumber</groupId>
       <artifactId>cucumber-maven-plugin</artifactId>
   </plugin>
   ```

2. **Check report plugins in TestRunner:**
   ```java
   @CucumberOptions(
       plugin = {
           "pretty",
           "html:reports/cucumber-report.html",
           "json:reports/cucumber.json",
           "junit:reports/cucumber.xml"
       }
   )
   ```

3. **Ensure reports directory exists:**
   ```bash
   mkdir -p reports
   mkdir -p reports/screenshots
   mkdir -p reports/logs
   ```

4. **Run tests with proper command:**
   ```bash
   mvn clean test  # Don't skip tests
   ```

#### Issue: "Screenshots not captured"
```
reports/screenshots/ directory is empty
```

**Solution:**

1. **Verify directory permissions:**
   ```bash
   chmod 755 reports/screenshots
   chmod 755 reports
   ```

2. **Check Hooks configuration:**
   ```java
   // In Hooks.java
   @After
   public void tearDown(Scenario scenario) {
       if (scenario.isFailed()) {
           screenshotUtility.captureScreenshot(scenario.getName());
       }
   }
   ```

3. **Enable screenshot on success:**
   ```properties
   # application.properties
   report.screenshot.on.success=true
   ```

---

### 6. Browser Issues

#### Issue: "Browser not starting"
```
Exception: Unable to create WebDriver instance
```

**Solution:**

1. **Verify browser is installed:**
   ```bash
   # Check Chrome installation
   which google-chrome
   
   # Check Firefox installation
   which firefox
   ```

2. **Check browser version compatibility:**
   ```bash
   # Check WebDriver version
   mvn dependency:tree | grep selenium
   
   # Ensure browser and driver versions match
   ```

3. **Try different browser:**
   ```bash
   mvn clean test -Dbrowser=firefox
   mvn clean test -Dbrowser=edge
   ```

#### Issue: "Headless mode not working"
```
Browser window opens despite headless=true
```

**Solution:**

```java
// Verify BaseClass implements headless correctly
boolean headless = Boolean.parseBoolean(
    ConfigReader.getProperty("headless", "false")
);

if (headless) {
    options.addArguments("--headless");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
}
```

---

### 7. Maven Issues

#### Issue: "Maven not found"
```
'mvn' is not recognized
```

**Solution:**

```bash
# Check Maven installation
mvn -version

# Add Maven to PATH (if not installed)
# Windows: Download Maven and add bin folder to PATH
# Mac: brew install maven
# Linux: apt-get install maven

# Verify Java is installed
java -version

# Set JAVA_HOME
export JAVA_HOME=/path/to/jdk
```

#### Issue: "Dependency download failed"
```
Failure to transfer from central repository
```

**Solution:**

```bash
# Option 1: Update repository settings
mvn clean install -U

# Option 2: Clear local cache
rm -rf ~/.m2/repository

# Option 3: Use different repository
# Edit ~/.m2/settings.xml

# Option 4: Check internet connection
ping repo.maven.apache.org
```

#### Issue: "Build fails with compilation errors"
```
[ERROR] COMPILATION ERROR
```

**Solution:**

1. **Check Java version:**
   ```bash
   java -version  # Should be 11 or higher
   ```

2. **Clear build cache:**
   ```bash
   mvn clean
   rm -rf ~/.m2/repository
   mvn install
   ```

3. **Check for syntax errors:**
   ```bash
   mvn clean compile
   # Check the error messages
   ```

---

### 8. Test Execution Issues

#### Issue: "Tests pass locally but fail in CI/CD"
```
Azure Pipeline fails but local tests pass
```

**Solution:**

1. **Replicate CI environment:**
   ```bash
   # Run with similar configuration
   mvn clean test -Dbrowser=chrome -Dheadless=true
   
   # Check for OS-specific issues
   # Windows vs Linux might have different paths
   ```

2. **Check CI logs:**
   ```bash
   # View Azure Pipeline logs
   # Check reports/logs/automation_test.log
   ```

3. **Verify paths are correct:**
   ```java
   // Use relative paths
   // ✅ Good
   String path = "src/test/resources/testdata/data.xlsx";
   
   // ❌ Avoid
   String path = "C:\\Users\\username\\testdata\\data.xlsx";
   ```

#### Issue: "Tests run but no assertions"
```
All tests pass even with failures
```

**Solution:**

```java
// ✅ Add meaningful assertions
@Then("Home page should be displayed")
public void home_page_displayed() {
    Assert.assertTrue(homePage.isLoaded(), 
        "Home page failed to load");
}

// ❌ Avoid: Empty steps
@Then("Something happens")
public void something_happens() {
    // No assertion!
}
```

---

### 9. Performance Issues

#### Issue: "Tests run too slowly"
```
Each test takes 30 seconds
```

**Solution:**

1. **Reduce unnecessary waits:**
   ```java
   // Check current implicit wait
   LOGGER.info("Implicit wait: " + CONFIG.get("implicity_wait"));
   
   // Reduce if not needed
   implicity_wait=5  // From default 10
   ```

2. **Optimize locators:**
   ```java
   // ❌ Slow: Complex XPath
   By.xpath("//div[@id='container']/div[1]/div[2]/button[contains(text(), 'Login')]")
   
   // ✅ Fast: Simple XPath
   By.xpath("//button[@id='login-btn']")
   ```

3. **Use parallel execution:**
   ```bash
   mvn test -Dparallel.threads=4
   ```

---

### 10. Data-Driven Testing Issues

#### Issue: "Excel file not read"
```
List is empty when reading Excel data
```

**Solution:**

```java
// Verify file path
String filePath = "src/test/resources/testdata/data.xlsx";
File file = new File(filePath);
Assert.assertTrue(file.exists(), "File not found: " + filePath);

// Verify sheet name
List<Map<String, String>> data = TestDataUtility.readDataFromExcel(
    filePath, "Sheet1"  // Correct sheet name
);

LOGGER.info("Rows read: " + data.size());
```

---

## Debugging Tips

### 1. Enable Debug Logging
```properties
# application.properties
log.level=DEBUG
```

### 2. Add Diagnostic Logging
```java
LOGGER.info("Current URL: " + driver.getCurrentUrl());
LOGGER.info("Page Title: " + driver.getTitle());
LOGGER.info("Current Time: " + new Date());
LOGGER.info("Page Source Length: " + driver.getPageSource().length());
```

### 3. Take Screenshots During Execution
```java
ScreenshotUtility screenshot = new ScreenshotUtility();
screenshot.captureScreenshot("debug_screenshot");
```

### 4. Use Browser Developer Tools
```bash
# Open browser console (F12)
# Execute JavaScript to debug:
# document.querySelectorAll("button")
# document.getElementById("element")
```

### 5. Check Browser Logs
```java
// Get browser console errors
LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
for (LogEntry entry : logs) {
    LOGGER.warn("Browser Log: " + entry.getMessage());
}
```

---

## Getting Help

1. **Check Logs:**
   - `reports/logs/automation_test.log`
   - `reports/logs/errors.log`
   - Azure Pipeline logs

2. **Check Screenshots:**
   - `reports/screenshots/` for failed tests

3. **Review Documentation:**
   - [README.md](README.md)
   - [QUICKSTART.md](QUICKSTART.md)
   - [BEST_PRACTICES.md](BEST_PRACTICES.md)

4. **Contact Support:**
   - QA Team
   - Development Team
   - Framework Maintainers

---

**Last Updated:** March 2024  
**Framework Version:** 1.0.0
