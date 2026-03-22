package com.automation.framework.pages;

import com.automation.framework.base.BaseClass;
import com.automation.framework.utilities.WaitUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

/**
 * BasePage: Base class for all page objects
 * Contains common methods used across all page classes
 */
public class BasePage {

    protected static final Logger LOGGER = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WaitUtility waitUtility;
    protected Actions actions;
    protected JavascriptExecutor jsExecutor;

    public BasePage() {
        this.driver = BaseClass.getDriver();
        this.waitUtility = new WaitUtility(driver);
        this.actions = new Actions(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Click on element
     */
    protected void click(By locator) {
        try {
            WebElement element = waitUtility.waitForElementToBeClickable(locator);
            element.click();
            LOGGER.info("Clicked on element: " + locator);
        } catch (Exception e) {
            LOGGER.error("Error clicking on element: " + locator, e);
            throw e;
        }
    }

    /**
     * Type text in element
     */
    protected void type(By locator, String text) {
        try {
            WebElement element = waitUtility.waitForElementToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
            LOGGER.info("Typed text in element: " + locator + " - Text: " + text);
        } catch (Exception e) {
            LOGGER.error("Error typing text in element: " + locator, e);
            throw e;
        }
    }

    /**
     * Get text from element
     */
    protected String getText(By locator) {
        try {
            WebElement element = waitUtility.waitForElementToBeVisible(locator);
            String text = element.getText();
            LOGGER.info("Retrieved text from element: " + locator + " - Text: " + text);
            return text;
        } catch (Exception e) {
            LOGGER.error("Error getting text from element: " + locator, e);
            throw e;
        }
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            WebElement element = waitUtility.waitForElementToBeVisible(locator);
            boolean isDisplayed = element.isDisplayed();
            LOGGER.info("Element is displayed: " + locator + " - " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            LOGGER.warn("Element not displayed: " + locator);
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(By locator) {
        try {
            WebElement element = waitUtility.waitForElementToBePresent(locator);
            boolean isEnabled = element.isEnabled();
            LOGGER.info("Element is enabled: " + locator + " - " + isEnabled);
            return isEnabled;
        } catch (Exception e) {
            LOGGER.warn("Error checking if element is enabled: " + locator);
            return false;
        }
    }

    /**
     * Get attribute value
     */
    protected String getAttribute(By locator, String attributeName) {
        try {
            WebElement element = waitUtility.waitForElementToBeVisible(locator);
            String attributeValue = element.getAttribute(attributeName);
            LOGGER.info("Retrieved attribute value from element: " + locator + " - Attribute: " + attributeName + " - Value: " + attributeValue);
            return attributeValue;
        } catch (Exception e) {
            LOGGER.error("Error getting attribute from element: " + locator, e);
            throw e;
        }
    }

    /**
     * Scroll to element
     */
    protected void scrollToElement(By locator) {
        try {
            WebElement element = waitUtility.waitForElementToBePresent(locator);
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
            LOGGER.info("Scrolled to element: " + locator);
        } catch (Exception e) {
            LOGGER.error("Error scrolling to element: " + locator, e);
            throw e;
        }
    }

    /**
     * Hover over element
     */
    protected void hoverOverElement(By locator) {
        try {
            WebElement element = waitUtility.waitForElementToBeVisible(locator);
            actions.moveToElement(element).perform();
            LOGGER.info("Hovered over element: " + locator);
        } catch (Exception e) {
            LOGGER.error("Error hovering over element: " + locator, e);
            throw e;
        }
    }

    /**
     * Double click on element
     */
    protected void doubleClick(By locator) {
        try {
            WebElement element = waitUtility.waitForElementToBeClickable(locator);
            actions.doubleClick(element).perform();
            LOGGER.info("Double clicked on element: " + locator);
        } catch (Exception e) {
            LOGGER.error("Error double clicking on element: " + locator, e);
            throw e;
        }
    }

    /**
     * Right click on element
     */
    protected void rightClick(By locator) {
        try {
            WebElement element = waitUtility.waitForElementToBeClickable(locator);
            actions.contextClick(element).perform();
            LOGGER.info("Right clicked on element: " + locator);
        } catch (Exception e) {
            LOGGER.error("Error right clicking on element: " + locator, e);
            throw e;
        }
    }

    /**
     * Switch to frame
     */
    protected void switchToFrame(By frameLocator) {
        try {
            WebElement frameElement = waitUtility.waitForElementToBePresent(frameLocator);
            driver.switchTo().frame(frameElement);
            LOGGER.info("Switched to frame: " + frameLocator);
        } catch (Exception e) {
            LOGGER.error("Error switching to frame: " + frameLocator, e);
            throw e;
        }
    }

    /**
     * Switch to default content
     */
    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        LOGGER.info("Switched to default content");
    }

    /**
     * Get page title
     */
    protected String getPageTitle() {
        String title = driver.getTitle();
        LOGGER.info("Current page title: " + title);
        return title;
    }

    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        LOGGER.info("Current URL: " + url);
        return url;
    }

    /**
     * Wait for page to load
     */
    protected void waitForPageLoad() {
        jsExecutor.executeScript("return document.readyState").equals("complete");
        LOGGER.info("Page loaded successfully");
    }

    /**
     * Check if element exists
     */
    protected boolean isElementPresent(By locator) {
        try {
            waitUtility.waitForElementToBePresent(locator, 5);
            LOGGER.info("Element is present: " + locator);
            return true;
        } catch (Exception e) {
            LOGGER.info("Element not present: " + locator);
            return false;
        }
    }
}
