package com.automation.framework.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtility: Handles explicit waits for WebDriver operations
 */
public class WaitUtility {

    private static final Logger LOGGER = LogManager.getLogger(WaitUtility.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private long defaultTimeout;

    public WaitUtility(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeout = Long.parseLong(ConfigReader.getProperty("implicity_wait", "10"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeout));
    }

    /**
     * Wait for element to be visible
     */
    public WebElement waitForElementToBeVisible(By locator) {
        try {
            LOGGER.info("Waiting for element to be visible: " + locator);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            LOGGER.error("Element not visible within timeout: " + locator, e);
            throw new RuntimeException("Element not visible: " + locator);
        }
    }

    /**
     * Wait for element to be visible with custom timeout
     */
    public WebElement waitForElementToBeVisible(By locator, int timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            LOGGER.info("Waiting for element to be visible with timeout " + timeout + "s: " + locator);
            return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            LOGGER.error("Element not visible within " + timeout + " seconds: " + locator, e);
            throw new RuntimeException("Element not visible: " + locator);
        }
    }

    /**
     * Wait for element to be clickable
     */
    public WebElement waitForElementToBeClickable(By locator) {
        try {
            LOGGER.info("Waiting for element to be clickable: " + locator);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            LOGGER.error("Element not clickable within timeout: " + locator, e);
            throw new RuntimeException("Element not clickable: " + locator);
        }
    }

    /**
     * Wait for element to be clickable with custom timeout
     */
    public WebElement waitForElementToBeClickable(By locator, int timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            LOGGER.info("Waiting for element to be clickable with timeout " + timeout + "s: " + locator);
            return customWait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            LOGGER.error("Element not clickable within " + timeout + " seconds: " + locator, e);
            throw new RuntimeException("Element not clickable: " + locator);
        }
    }

    /**
     * Wait for element to be present
     */
    public WebElement waitForElementToBePresent(By locator) {
        try {
            LOGGER.info("Waiting for element to be present: " + locator);
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            LOGGER.error("Element not present within timeout: " + locator, e);
            throw new RuntimeException("Element not present: " + locator);
        }
    }

    /**
     * Wait for element to be present with custom timeout
     */
    public WebElement waitForElementToBePresent(By locator, int timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            LOGGER.info("Waiting for element to be present with timeout " + timeout + "s: " + locator);
            return customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            LOGGER.error("Element not present within " + timeout + " seconds: " + locator, e);
            throw new RuntimeException("Element not present: " + locator);
        }
    }

    /**
     * Wait for element to disappear
     */
    public boolean waitForElementToDisappear(By locator) {
        try {
            LOGGER.info("Waiting for element to disappear: " + locator);
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            LOGGER.error("Element still visible after timeout: " + locator, e);
            return false;
        }
    }

    /**
     * Wait for element to disappear with custom timeout
     */
    public boolean waitForElementToDisappear(By locator, int timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            LOGGER.info("Waiting for element to disappear with timeout " + timeout + "s: " + locator);
            return customWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            LOGGER.error("Element still visible after " + timeout + " seconds: " + locator, e);
            return false;
        }
    }

    /**
     * Wait for attribute value
     */
    public boolean waitForAttributeValue(By locator, String attribute, String value) {
        try {
            LOGGER.info("Waiting for attribute " + attribute + " to have value: " + value);
            return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
        } catch (Exception e) {
            LOGGER.error("Attribute value not changed within timeout", e);
            return false;
        }
    }

    /**
     * Wait for text to be present in element
     */
    public boolean waitForTextInElement(By locator, String text) {
        try {
            LOGGER.info("Waiting for text '" + text + "' in element: " + locator);
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (Exception e) {
            LOGGER.error("Text not found in element within timeout", e);
            return false;
        }
    }
}
