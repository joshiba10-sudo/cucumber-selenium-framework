package com.automation.framework.pages;

import org.openqa.selenium.By;

/**
 * HomePage: Page Object for Home Page
 */
public class HomePage extends BasePage {

    // Page Locators
    private By welcomeMessage = By.xpath("//h1[contains(text(), 'Welcome')]");
    private By userProfile = By.id("userProfile");
    private By logoutButton = By.xpath("//button[contains(text(), 'Logout')]");
    private By dashboardMenu = By.linkText("Dashboard");
    private By settingsMenu = By.linkText("Settings");
    private By notificationBell = By.className("notification-bell");
    private By notificationCount = By.className("notification-count");

    /**
     * Get welcome message
     */
    public String getWelcomeMessage() {
        LOGGER.info("Retrieving welcome message");
        return getText(welcomeMessage);
    }

    /**
     * Check if home page is loaded
     */
    public boolean isHomePageLoaded() {
        LOGGER.info("Checking if home page is loaded");
        return isElementDisplayed(welcomeMessage);
    }

    /**
     * Click on user profile
     */
    public void clickUserProfile() {
        LOGGER.info("Clicking on user profile");
        click(userProfile);
    }

    /**
     * Logout from application
     */
    public void logout() {
        LOGGER.info("Logging out from application");
        click(logoutButton);
    }

    /**
     * Navigate to dashboard
     */
    public void navigateToDashboard() {
        LOGGER.info("Navigating to dashboard");
        click(dashboardMenu);
    }

    /**
     * Navigate to settings
     */
    public void navigateToSettings() {
        LOGGER.info("Navigating to settings");
        click(settingsMenu);
    }

    /**
     * Click notification bell
     */
    public void clickNotificationBell() {
        LOGGER.info("Clicking notification bell");
        click(notificationBell);
    }

    /**
     * Get notification count
     */
    public String getNotificationCount() {
        LOGGER.info("Getting notification count");
        return getText(notificationCount);
    }

    /**
     * Check if logout button is displayed
     */
    public boolean isLogoutButtonDisplayed() {
        LOGGER.info("Checking if logout button is displayed");
        return isElementDisplayed(logoutButton);
    }
}
