package com.automation.framework.utilities;

import com.automation.framework.base.BaseClass;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtility: Handles screenshot capture functionality
 */
public class ScreenshotUtility {

    private static final Logger LOGGER = LogManager.getLogger(ScreenshotUtility.class);
    private String screenshotPath;

    public ScreenshotUtility() {
        this.screenshotPath = ConfigReader.getProperty("screenshot.path", "./reports/screenshots/");
        createScreenshotDirectory();
    }

    /**
     * Create screenshot directory if it doesn't exist
     */
    private void createScreenshotDirectory() {
        File dir = new File(screenshotPath);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                LOGGER.info("Screenshot directory created: " + screenshotPath);
            } else {
                LOGGER.warn("Failed to create screenshot directory");
            }
        }
    }

    /**
     * Capture screenshot with timestamp
     */
    public String captureScreenshot(String screenshotName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) BaseClass.getDriver();
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";
            String destinationPath = screenshotPath + fileName;
            
            File destinationFile = new File(destinationPath);
            FileUtils.copyFile(sourceFile, destinationFile);
            
            LOGGER.info("Screenshot captured: " + destinationPath);
            return destinationPath;
        } catch (Exception e) {
            LOGGER.error("Error capturing screenshot: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Capture screenshot without name (auto-generated)
     */
    public String captureScreenshot() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
        return captureScreenshot("screenshot_" + timestamp);
    }

    /**
     * Get screenshot path
     */
    public String getScreenshotPath() {
        return screenshotPath;
    }

    /**
     * Set custom screenshot path
     */
    public void setScreenshotPath(String path) {
        this.screenshotPath = path;
        createScreenshotDirectory();
    }
}
