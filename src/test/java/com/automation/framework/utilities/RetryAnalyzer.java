package com.automation.framework.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer: Implements retry mechanism for failed tests
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOGGER = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private int maxRetryCount;

    public RetryAnalyzer() {
        this.maxRetryCount = ConfigReader.getIntProperty("retry.count", 2);
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            LOGGER.info("Test '" + result.getName() + "' failed. Retrying... Attempt " + retryCount + " of " + maxRetryCount);
            
            // Wait before retrying
            int retryDelay = ConfigReader.getIntProperty("retry.delay", 1000);
            try {
                Thread.sleep(retryDelay);
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted while waiting for retry", e);
            }
            
            return true;
        }
        return false;
    }

    /**
     * Reset retry count
     */
    public void resetRetryCount() {
        retryCount = 0;
    }

    /**
     * Get current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Set max retry count
     */
    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }
}
