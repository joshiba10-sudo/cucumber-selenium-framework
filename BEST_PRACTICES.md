# Best Practices & Framework Guidelines

## Table of Contents
1. [Code Style Guidelines](#code-style-guidelines)
2. [Page Object Model Best Practices](#page-object-model-best-practices)
3. [Step Definition Best Practices](#step-definition-best-practices)
4. [Feature File Best Practices](#feature-file-best-practices)
5. [Locator Strategies](#locator-strategies)
6. [Wait Strategies](#wait-strategies)
7. [Error Handling](#error-handling)
8. [Performance Optimization](#performance-optimization)
9. [Security Best Practices](#security-best-practices)
10. [Testing Best Practices](#testing-best-practices)

---

## Code Style Guidelines

### Naming Conventions

#### Classes
```java
// ✅ Good
public class LoginPage extends BasePage { }
public class HomePageStepDefinitions { }
public class ConfigReader { }
public class ScreenshotUtility { }

// ❌ Avoid
public class LoginPagePOM { }
public class Login_page { }
public class loginpage { }
public class tests { }
```

#### Methods
```java
// ✅ Good
public void login(String username, String password) { }
public boolean isHomePageLoaded() { }
public String getUserName() { }
public void clickLoginButton() { }

// ❌ Avoid
public void logIn() { }
public void Login() { }
public void test() { }
public void click_login_button() { }
```

#### Variables & Constants
```java
// ✅ Good
private By usernameField = By.id("username");
private static final int TIMEOUT = 10;
private String userData = "test";

// ❌ Avoid
private By username_field = By.id("username");
private static final int timeout = 10;
private String UD = "test";
```

---

## Page Object Model Best Practices

### Structure
```java
public class LoginPage extends BasePage {
    
    // 1. Locators - Group by section
    // Header locators
    private By logo = By.className("logo");
    
    // Login form locators
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    
    // Message locators
    private By errorMessage = By.className("error");
    
    // 2. Methods - Organized by functionality
    public void enterUsername(String username) {
        type(usernameField, username);
    }
    
    public void enterPassword(String password) {
        type(passwordField, password);
    }
    
    public void clickLogin() {
        click(loginButton);
    }
    
    public String getErrorMessage() {
        return getText(errorMessage);
    }
    
    public boolean isErrorDisplayed() {
        return isElementDisplayed(errorMessage);
    }
}
```

### Anti-Patterns
```java
// ❌ Avoid: Too many parameters in method
public void login(String u, String p, boolean rememberMe, String role) { }

// ❌ Avoid: Generic methods
public void fillForm(String[] fields, String[] values) { }

// ❌ Avoid: Mixing test logic with page objects
if (scenario.isFailed()) {
    captureScreenshot();  // This belongs in hooks!
}

// ❌ Avoid: Direct WebDriver usage in steps
driver.findElement(By.id("username")).sendKeys("test");  // Use page objects!
```

---

## Step Definition Best Practices

### Good Examples
```java
@Given("User is on the login page")
public void user_is_on_login_page() {
    Assert.assertTrue(loginPage.isLoginPageLoaded(), 
        "Login page failed to load");
}

@When("User enters {string} as username and {string} as password")
public void user_enters_credentials(String username, String password) {
    loginPage.enterUsername(username);
    loginPage.enterPassword(password);
}

@Then("User should see {string} message")
public void user_should_see_message(String expectedMessage) {
    String actualMessage = loginPage.getMessage();
    Assert.assertEquals(actualMessage, expectedMessage);
}

@And("Home page title should contain {string}")
public void home_page_title_should_contain(String text) {
    String title = homePage.getPageTitle();
    Assert.assertTrue(title.contains(text));
}
```

### Anti-Patterns
```java
// ❌ Avoid: Multiple assertions in one step
@Then("User should be logged in")
public void user_logged_in() {
    Assert.assertTrue(homePage.isDisplayed());  // ✅
    Assert.assertTrue(userMenu.isDisplayed());  // ❌ Second assertion
    Assert.assertTrue(logout.isDisplayed());    // ❌ Third assertion
}

// ❌ Avoid: Hard waits
@When("Page loads")
public void page_loads() {
    Thread.sleep(5000);  // Never do this!
}

// ❌ Avoid: Unclear step names
@When("Something happens")
public void something_happens() { }

// ❌ Avoid: Step logic
@When("User logs in")
public void user_logs_in() {
    driver.findElement(By.id("user")).sendKeys("test");  // Too detailed!
}
```

---

## Feature File Best Practices

### Structure
```gherkin
# Feature file naming: FeatureName.feature
# Store in: src/test/resources/features/

Feature: User Authentication
  User should be able to login with valid credentials
  
  # Background runs before each scenario
  Background:
    Given User is on the login page
  
  # Use descriptive scenario names
  @smoke @critical
  Scenario: User login with valid credentials
    When User enters "testuser" username
    And User enters "password123" password
    And User clicks login button
    Then User should be logged in successfully
  
  # Scenario Outline for data-driven testing
  @regression
  Scenario Outline: Multiple user logins
    When User enters "<username>" username
    And User enters "<password>" password
    And User clicks login button
    Then User should see "<result>" message
    
    Examples:
      | username  | password   | result          |
      | testuser  | password   | Login Successful|
      | invalid   | wrongpass  | Invalid Login   |
```

### Best Practices
```gherkin
# ✅ Good: Clear and concise
Scenario: User can reset password with valid email
  When User clicks forgot password link
  And User enters valid email
  Then Reset link should be sent

# ❌ Avoid: Too much detail
Scenario: User navigates to forgot password page clicking link in login form
  When User scrolls to bottom of login form
  And User clicks the "Forgot Password?" link
  And Browser navigates to password reset page

# ✅ Good: Use data examples
Scenario Outline: Login with different user types
  When User logs in with "<userType>" user
  Then User should see correct "<dashboard>"
  
  Examples:
    | userType | dashboard |
    | admin    | Admin     |
    | user     | User      |
```

---

## Locator Strategies

### Recommended Locator Priority
```java
// 1. ✅ ID (Most Stable)
By.id("username")

// 2. ✅ Class Name (Stable)
By.className("login-button")

// 3. ✅ Name Attribute
By.name("email")

// 4. ✅ Link Text
By.linkText("Sign Out")

// 5. ⚠️ XPath (Last Resort - More Brittle)
By.xpath("//button[@type='submit']")

// 6. ❌ CSS Selector (In this framework, prefer XPath for consistency)
By.cssSelector("button.submit")
```

### XPath Best Practices
```java
// ✅ Absolute XPath (Simple)
private By element = By.xpath("//div[@id='container']");

// ✅ Relative XPath (Preferred)
private By button = By.xpath("//button[contains(text(), 'Login')]");

// ✅ Using multiple conditions
private By input = By.xpath("//input[@type='text' and @name='username']");

// ✅ Using text()
private By message = By.xpath("//span[text()='Welcome']");

// ❌ Fragile XPath (Avoid)
private By element = By.xpath("//div[1]/div[2]/div[3]/button");

// ❌ Too complex
private By element = By.xpath("//div//div//div//button");
```

---

## Wait Strategies

### Explicit Waits (Preferred)
```java
// ✅ Good: Explicit wait for visibility
WebElement element = waitUtility.waitForElementToBeVisible(By.id("element"));

// ✅ Good: Explicit wait for clickability
WebElement button = waitUtility.waitForElementToBeClickable(By.id("button"));

// ✅ Good: Custom timeout
WebElement element = waitUtility.waitForElementToBeVisible(By.id("element"), 15);
```

### Never Use Hard Waits
```java
// ❌ Avoid Hard Waits
Thread.sleep(5000);

// ❌ Avoid in loops
while (true) {
    try {
        driver.findElement(By.id("element")).click();
        break;
    } catch (Exception e) {
        Thread.sleep(1000);
    }
}
```

---

## Error Handling

### Proper Exception Handling
```java
// ✅ Good: Catch specific exceptions
public void click(By locator) {
    try {
        WebElement element = waitUtility.waitForElementToBeClickable(locator);
        element.click();
        LOGGER.info("Clicked: " + locator);
    } catch (TimeoutException e) {
        LOGGER.error("Element not clickable within timeout: " + locator, e);
        throw new RuntimeException("Element not clickable: " + locator);
    } catch (StaleElementReferenceException e) {
        LOGGER.warn("Stale element, retrying...", e);
        // Retry logic
    }
}

// ❌ Avoid: Generic exception catching
try {
    // code
} catch (Exception e) {
    // Too broad!
}

// ❌ Avoid: Swallowing exceptions
try {
    driver.findElement(locator).click();
} catch (Exception e) {
    // Silent failure!
}
```

---

## Performance Optimization

### Reduce Unnecessary Waits
```java
// ❌ Slow: Multiple implicit waits
WebElement element = driver.findElement(By.id("element"));  // Waits 10s
WebElement element2 = driver.findElement(By.id("element2")); // Waits 10s

// ✅ Fast: Use explicit waits only when needed
WebElement element = waitUtility.waitForElementToBeClickable(By.id("element"));
```

### Batch Operations
```java
// ❌ Slow: Multiple operations
enterUsername("user");
Thread.sleep(1000);
enterPassword("pass");
Thread.sleep(1000);
clickLogin();

// ✅ Fast: Direct operations
enterUsername("user");
enterPassword("pass");
clickLogin();
```

---

## Security Best Practices

### Never Hardcode Credentials
```java
// ❌ Bad: Hardcoded credentials
public void login() {
    enterUsername("admin");
    enterPassword("admin123");
}

// ✅ Good: Use configuration or environment
public void login() {
    String username = ConfigReader.getProperty("test.username");
    String password = ConfigReader.getProperty("test.password");
    enterUsername(username);
    enterPassword(password);
}
```

### Protect Sensitive Data in Logs
```java
// ❌ Bad: Logging sensitive data
LOGGER.info("Username: " + username + ", Password: " + password);

// ✅ Good: Don't log sensitive data
LOGGER.info("Logging in with provided credentials");
LOGGER.debug("User: " + username);  // Debug level, not in production logs
```

---

## Testing Best Practices

### Test Independence
```java
// ✅ Good: Each test is independent
@Test
public void testLoginWithValidCredentials() {
    loginPage.login("user1", "pass1");
    Assert.assertTrue(homePage.isDisplayed());
}

@Test
public void testLoginWithInvalidCredentials() {
    loginPage.login("invalid", "wrong");
    Assert.assertTrue(loginPage.isErrorDisplayed());
}

// ❌ Avoid: Test dependency
@Test(priority = 1)
public void testLogin() {
    // Depends on testLogout()
}

@Test(priority = 2)
public void testLogout() {
    // Depends on testLogin()
}
```

### Meaningful Assertions
```java
// ✅ Good: Clear assertion messages
Assert.assertTrue(homePage.isDisplayed(), 
    "Home page should be displayed after successful login");

// ❌ Avoid: Vague assertions
Assert.assertTrue(homePage.isDisplayed());
```

### Test Organization
```java
// ✅ Good: Organized with Background
Feature: Login Functionality
  Background:
    Given User is on the login page
  
  Scenario: Valid login
    When User logs in with valid credentials
    Then Home page should be displayed
  
  Scenario: Invalid login
    When User logs in with invalid credentials
    Then Error message should be displayed
```

---

## Code Review Checklist

- ✅ Naming conventions followed
- ✅ POM structure maintained
- ✅ No hardcoded values
- ✅ Proper logging implemented
- ✅ Exception handling present
- ✅ Wait strategies used (explicit waits)
- ✅ No thread sleeps
- ✅ Feature files are readable
- ✅ Steps are not too detailed
- ✅ Assertions have messages
- ✅ No sensitive data in code
- ✅ Tests are independent
- ✅ Code is DRY (Don't Repeat Yourself)
- ✅ No commented code
- ✅ Methods are focused and small

---

## Resources

- 📖 [Quick Start Guide](QUICKSTART.md)
- 📚 [Main README](README.md)
- 🔍 [Selenium Best Practices](https://www.selenium.dev/documentation/webdriver/best_practices/)
- 🥒 [Cucumber Best Practices](https://cucumber.io/docs/bdd/)

---

**Happy Testing! Follow these guidelines for a maintainable and scalable test automation framework.**
