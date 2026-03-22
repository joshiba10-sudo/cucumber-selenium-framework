package com.automation.framework.pages;

import org.openqa.selenium.By;

/**
 * LoginPage: Page Object for Login Page
 */
public class LoginPage extends BasePage {

    // Page Locators
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    private By errorMessage = By.className("error-message");
    private By rememberMeCheckbox = By.id("rememberMe");
    private By forgotPasswordLink = By.linkText("Forgot Password?");

    /**
     * Enter username
     */
    public void enterUsername(String username) {
        LOGGER.info("Entering username: " + username);
        type(usernameField, username);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        LOGGER.info("Entering password");
        type(passwordField, password);
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        LOGGER.info("Clicking login button");
        click(loginButton);
    }

    /**
     * Login with credentials
     */
    public void login(String username, String password) {
        LOGGER.info("Performing login with username: " + username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        LOGGER.info("Retrieving error message");
        return getText(errorMessage);
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        LOGGER.info("Checking if error message is displayed");
        return isElementDisplayed(errorMessage);
    }

    /**
     * Click remember me checkbox
     */
    public void clickRememberMeCheckbox() {
        LOGGER.info("Clicking remember me checkbox");
        click(rememberMeCheckbox);
    }

    /**
     * Click forgot password link
     */
    public void clickForgotPasswordLink() {
        LOGGER.info("Clicking forgot password link");
        click(forgotPasswordLink);
    }

    /**
     * Check if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        LOGGER.info("Checking if login button is enabled");
        return isElementEnabled(loginButton);
    }
}
