package com.automation.framework.hooks;

import com.automation.framework.base.BaseClass;
import com.automation.framework.context.ScenarioContext;
import com.automation.framework.utilities.ScreenshotUtility;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hooks: Contains Cucumber hooks for setup and teardown
 * Executed before and after each scenario
 * Includes ScenarioContext initialization for data sharing between steps
 */
public class Hooks {

    private static final Logger LOGGER = LogManager.getLogger(Hooks.class);
    private ScreenshotUtility screenshotUtility;

    public Hooks() {
        this.screenshotUtility = new ScreenshotUtility();
    }

    /**
     * This method runs before every scenario
     * Initializes ScenarioContext for data sharing between UI and API steps
     */
    @Before
    public void setUp(Scenario scenario) {
        LOGGER.info("===============================================");
        LOGGER.info("Starting Scenario: " + scenario.getName());
        LOGGER.info("===============================================");
        
        try {
            // Initialize ScenarioContext for data sharing
            ScenarioContext context = ScenarioContext.getInstance();
            LOGGER.info("ScenarioContext initialized for: " + scenario.getName());
            
            // Check if scenario has @ui tag - initialize browser if needed for UI interactions
            // Skip browser initialization only for pure API tests (API without UI)
            if (scenario.getSourceTagNames().contains("@ui") || 
                (!scenario.getSourceTagNames().contains("@api") && !scenario.getSourceTagNames().contains("@ui"))) {
                BaseClass.initializeDriver();
                BaseClass.navigateToApplication();
                LOGGER.info("Browser and Application initialized successfully");
            } else {
                LOGGER.info("API test detected - skipping browser initialization");
            }
        } catch (Exception e) {
            LOGGER.error("Error during setup: " + e.getMessage(), e);
            throw new RuntimeException("Setup failed: " + e.getMessage());
        }
    }

    /**
     * This method runs after every scenario
     * Cleans up ScenarioContext after scenario execution
     */
    @After
    public void tearDown(Scenario scenario) {
        LOGGER.info("Scenario: " + scenario.getName() + " - Status: " + scenario.getStatus());
        
        try {
            // Skip screenshot and teardown for API tests
            if (!scenario.getSourceTagNames().contains("@api")) {
                // Capture screenshot on failure
                if (scenario.isFailed()) {
                    LOGGER.warn("Test failed! Capturing screenshot...");
                    String screenshotName = scenario.getName().replace(" ", "_") + "_FAILED";
                    screenshotUtility.captureScreenshot(screenshotName);
                    LOGGER.info("Screenshot captured: " + screenshotName);
                } else if (Boolean.parseBoolean(com.automation.framework.utilities.ConfigReader.getProperty("report.screenshot.on.success", "false"))) {
                    String screenshotName = scenario.getName().replace(" ", "_") + "_PASSED";
                    screenshotUtility.captureScreenshot(screenshotName);
                    LOGGER.info("Screenshot captured: " + screenshotName);
                }
                
                BaseClass.tearDown();
            } else {
                LOGGER.info("API test teardown - no browser operations");
            }
        } catch (Exception e) {
            LOGGER.error("Error while tearing down: " + e.getMessage(), e);
        } finally {
            // Clear ScenarioContext after scenario
            ScenarioContext.clear();
            LOGGER.info("ScenarioContext cleared for: " + scenario.getName());
            
            LOGGER.info("===============================================");
            LOGGER.info("Scenario Completed: " + scenario.getName());
            LOGGER.info("===============================================");
        }
    }
}
