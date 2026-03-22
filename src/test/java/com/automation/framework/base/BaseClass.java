package com.automation.framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import com.automation.framework.utilities.ConfigReader;

import java.time.Duration;

/**
 * BaseClass: Contains common setup and teardown logic
 * This class manages WebDriver initialization and cleanup
 */
public class BaseClass {

    public static WebDriver driver;
    public static final Logger LOGGER = LogManager.getLogger(BaseClass.class);
    
    /**
     * Initialize WebDriver based on browser configuration
     */
    public static void initializeDriver() {
        if (driver == null) {
            String browser = ConfigReader.getProperty("browser", "chrome").toLowerCase();
            LOGGER.info("Initializing driver for browser: " + browser);
            
            switch (browser) {
                case "chrome":
                    initializeChromeDriver();
                    break;
                case "firefox":
                    initializeFirefoxDriver();
                    break;
                case "edge":
                    initializeEdgeDriver();
                    break;
                default:
                    LOGGER.error("Invalid browser specified: " + browser);
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            
            setImplicitWait();
            setPageLoadTimeout();
            setScriptTimeout();
        }
    }

    /**
     * Initialize Chrome WebDriver
     */
    private static void initializeChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        driver = new ChromeDriver(options);
        LOGGER.info("Chrome WebDriver initialized successfully");
    }

    /**
     * Initialize Firefox WebDriver
     */
    private static void initializeFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--start-maximized");
        driver = new FirefoxDriver(options);
        LOGGER.info("Firefox WebDriver initialized successfully");
    }

    /**
     * Initialize Edge WebDriver
     */
    private static void initializeEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--start-maximized");
        driver = new EdgeDriver(options);
        LOGGER.info("Edge WebDriver initialized successfully");
    }

    /**
     * Set implicit wait
     */
    private static void setImplicitWait() {
        long implicitWait = Long.parseLong(ConfigReader.getProperty("implicity_wait", "10"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        LOGGER.info("Implicit wait set to: " + implicitWait + " seconds");
    }

    /**
     * Set page load timeout
     */
    private static void setPageLoadTimeout() {
        long pageLoadTimeout = Long.parseLong(ConfigReader.getProperty("pageload_timeout", "30"));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        LOGGER.info("Page load timeout set to: " + pageLoadTimeout + " seconds");
    }

    /**
     * Set script timeout
     */
    private static void setScriptTimeout() {
        long scriptTimeout = Long.parseLong(ConfigReader.getProperty("script_timeout", "20"));
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(scriptTimeout));
        LOGGER.info("Script timeout set to: " + scriptTimeout + " seconds");
    }

    /**
     * Navigate to application URL
     */
    public static void navigateToApplication() {
        String baseUrl = ConfigReader.getProperty("base.url");
        driver.navigate().to(baseUrl);
        LOGGER.info("Navigated to: " + baseUrl);
    }

    /**
     * Close WebDriver
     */
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
            LOGGER.info("WebDriver closed successfully");
        }
    }

    /**
     * Get current WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver;
    }
}
