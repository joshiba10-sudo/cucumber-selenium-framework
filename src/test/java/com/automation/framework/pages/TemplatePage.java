package com.automation.framework.pages;

import org.openqa.selenium.By;

/**
 * TEMPLATE: Use this file as a template for creating new page objects
 * 
 * Naming Convention: [PageName]Page.java
 * Example: SettingsPage.java, ProfilePage.java, DashboardPage.java
 * 
 * Steps to create a new Page Object:
 * 1. Identify all locators for the page elements
 * 2. Extend BasePage to inherit common methods
 * 3. Create methods for page-specific interactions
 * 4. Add proper logging
 */
public class TemplatePage extends BasePage {

    // ==================== Page Locators ====================
    // Group locators by section for better readability
    
    // Example locators - Replace with actual page elements
    private By pageTitle = By.xpath("//h1[@class='page-title']");
    private By headerMenu = By.id("header-menu");
    private By searchBox = By.id("search-input");
    private By submitButton = By.xpath("//button[@type='submit']");
    private By successMessage = By.className("success-message");
    private By errorMessage = By.className("error-message");

    // ==================== Page Methods ====================

    /**
     * Verify that page is loaded
     * This method should check for key elements that indicate the page has loaded
     */
    public boolean isPageLoaded() {
        LOGGER.info("Checking if TemplatePage is loaded");
        return isElementDisplayed(pageTitle);
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        LOGGER.info("Getting page title");
        return getText(pageTitle);
    }

    /**
     * Perform search
     * @param searchTerm - The search term to enter
     */
    public void performSearch(String searchTerm) {
        LOGGER.info("Performing search with term: " + searchTerm);
        type(searchBox, searchTerm);
        click(submitButton);
    }

    /**
     * Check if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        LOGGER.info("Checking if success message is displayed");
        return isElementDisplayed(successMessage);
    }

    /**
     * Get success message text
     */
    public String getSuccessMessage() {
        LOGGER.info("Getting success message");
        return getText(successMessage);
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        LOGGER.info("Checking if error message is displayed");
        return isElementDisplayed(errorMessage);
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        LOGGER.info("Getting error message");
        return getText(errorMessage);
    }

    
}
