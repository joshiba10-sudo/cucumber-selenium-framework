package com.automation.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * TestRunner: Cucumber test runner for TestNG
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.automation.framework.stepDefinitions", "com.automation.framework.hooks"},
        plugin = {
                "pretty",
                "html:reports/cucumber-report.html",
                "json:reports/cucumber.json",
                "junit:reports/cucumber.xml"
        },
        tags = "@integration",
        dryRun = false,
        monochrome = true,
        publish = false
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @Test(dataProvider = "scenarios")
    public void runScenario(Object[] scenario) {
        // This method is invoked by TestNG using the scenarios DataProvider
        // The actual execution is handled by AbstractTestNGCucumberTests
    }
}
