package com.automation.framework.utilities;

import com.automation.framework.base.BaseClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

/**
 * CommonUtilities: Additional utility methods for common operations
 */
public class CommonUtilities {

    private static final Logger LOGGER = LogManager.getLogger(CommonUtilities.class);
    private static WebDriver driver = BaseClass.getDriver();

    /**
     * Switch to new window
     */
    public static void switchToNewWindow() {
        Set<String> handles = driver.getWindowHandles();
        String newWindow = handles.stream().skip(handles.size() - 1).findFirst().orElse(null);
        if (newWindow != null) {
            driver.switchTo().window(newWindow);
            LOGGER.info("Switched to new window");
        }
    }

    /**
     * Switch to window by title
     */
    public static void switchToWindowByTitle(String title) {
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            driver.switchTo().window(handle);
            if (driver.getTitle().equals(title)) {
                LOGGER.info("Switched to window with title: " + title);
                return;
            }
        }
        LOGGER.warn("Window with title not found: " + title);
    }

    /**
     * Close current window
     */
    public static void closeCurrentWindow() {
        driver.close();
        LOGGER.info("Current window closed");
    }

    /**
     * Accept alert
     */
    public static void acceptAlert() {
        Alert alert = driver.switchTo().alert();
        alert.accept();
        LOGGER.info("Alert accepted");
    }

    /**
     * Dismiss alert
     */
    public static void dismissAlert() {
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
        LOGGER.info("Alert dismissed");
    }

    /**
     * Get alert text
     */
    public static String getAlertText() {
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        LOGGER.info("Alert text: " + text);
        return text;
    }

    /**
     * Type in alert
     */
    public static void typeInAlert(String text) {
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(text);
        LOGGER.info("Typed in alert: " + text);
    }

    /**
     * Select by visible text
     */
    public static void selectByVisibleText(By locator, String visibleText) {
        Select select = new Select(driver.findElement(locator));
        select.selectByVisibleText(visibleText);
        LOGGER.info("Selected: " + visibleText);
    }

    /**
     * Select by value
     */
    public static void selectByValue(By locator, String value) {
        Select select = new Select(driver.findElement(locator));
        select.selectByValue(value);
        LOGGER.info("Selected value: " + value);
    }

    /**
     * Select by index
     */
    public static void selectByIndex(By locator, int index) {
        Select select = new Select(driver.findElement(locator));
        select.selectByIndex(index);
        LOGGER.info("Selected index: " + index);
    }

    /**
     * Get execute script result
     */
    public static Object executeJavaScript(String script) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        Object result = jsExecutor.executeScript(script);
        LOGGER.info("JavaScript executed successfully");
        return result;
    }

    /**
     * Check if file is downloaded
     */
    public static boolean isFileDownloaded(String downloadPath, String fileName) {
        File dir = new File(downloadPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(fileName)) {
                    LOGGER.info("File found: " + fileName);
                    return true;
                }
            }
        }
        LOGGER.warn("File not found: " + fileName);
        return false;
    }

    /**
     * Delete downloaded file
     */
    public static void deleteDownloadedFile(String downloadPath, String fileName) {
        try {
            File dir = new File(downloadPath);
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().contains(fileName)) {
                        Files.delete(Paths.get(file.getAbsolutePath()));
                        LOGGER.info("File deleted: " + fileName);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting file: " + e.getMessage(), e);
        }
    }

    /**
     * Wait for URL change
     */
    public static void waitForURLChange(String expectedURL, int timeout) {
        long endTime = System.currentTimeMillis() + (timeout * 1000L);
        while (System.currentTimeMillis() < endTime) {
            if (driver.getCurrentUrl().equals(expectedURL)) {
                LOGGER.info("URL changed to: " + expectedURL);
                return;
            }
            sleep(500);
        }
        LOGGER.warn("URL did not change to: " + expectedURL);
    }

    /**
     * Sleep for specified milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            LOGGER.error("Sleep interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Refresh page
     */
    public static void refreshPage() {
        driver.navigate().refresh();
        LOGGER.info("Page refreshed");
    }

    /**
     * Go back
     */
    public static void goBack() {
        driver.navigate().back();
        LOGGER.info("Navigated back");
    }

    /**
     * Go forward
     */
    public static void goForward() {
        driver.navigate().forward();
        LOGGER.info("Navigated forward");
    }

    /**
     * Get all cookies
     */
    public static Set<org.openqa.selenium.Cookie> getAllCookies() {
        Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
        LOGGER.info("Retrieved " + cookies.size() + " cookies");
        return cookies;
    }

    /**
     * Get specific cookie
     */
    public static org.openqa.selenium.Cookie getCookie(String cookieName) {
        org.openqa.selenium.Cookie cookie = driver.manage().getCookieNamed(cookieName);
        LOGGER.info("Retrieved cookie: " + cookieName);
        return cookie;
    }

    /**
     * Add cookie
     */
    public static void addCookie(String name, String value) {
        driver.manage().addCookie(new org.openqa.selenium.Cookie(name, value));
        LOGGER.info("Cookie added: " + name);
    }

    /**
     * Delete all cookies
     */
    public static void deleteAllCookies() {
        driver.manage().deleteAllCookies();
        LOGGER.info("All cookies deleted");
    }

    /**
     * Delete specific cookie
     */
    public static void deleteCookie(String cookieName) {
        driver.manage().deleteCookie(driver.manage().getCookieNamed(cookieName));
        LOGGER.info("Cookie deleted: " + cookieName);
    }
}
