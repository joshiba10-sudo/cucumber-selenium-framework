package com.automation.framework.stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;

import com.automation.framework.base.BaseClass;
import com.automation.framework.pages.ContactPage;
import com.automation.framework.utilities.ScreenshotUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Step definitions for UI and API consistency testing
 * Validates that data submitted through UI matches API responses
 */
public class UIAPIConsistencyStepDefinitions {

    private WebDriver driver;
    private ContactPage contactPage;
    private Map<String, String> contactDetails;
    private Response apiResponse;
    private String baseURL;
    private String baseAppURL;
    private ScreenshotUtility screenshotUtility;
    private BaseClass baseClass;

    /**
     * Initialize dependencies through constructor injection
     */
    public UIAPIConsistencyStepDefinitions(BaseClass baseClass) {
        this.baseClass = baseClass;
        this.baseURL = System.getProperty("base.api.url", "https://automationintesting.online/api");
        this.baseAppURL = System.getProperty("base.app.url", "https://automationintesting.online");
        this.screenshotUtility = new ScreenshotUtility();
        this.contactDetails = new HashMap<>();
    }

    /**
     * Lazy initialize driver and contactPage
     */
    private void initializePageObjects() {
        if (this.driver == null) {
            this.driver = baseClass.getDriver();
        }
        if (this.contactPage == null) {
            this.contactPage = new ContactPage();
        }
    }

    /**
     * Launch the application
     */
    @Given("User launches the application")
    public void launchApplication() {
        initializePageObjects();
        contactPage.openContactPage();
        screenshotUtility.captureScreenshot("ApplicationLaunched");
    }

    /**
     * Enter contact details in the UI form
     */
    @When("User enters contact details:")
    public void enterContactDetails(DataTable dataTable) {
        initializePageObjects();
        // Use asMap() to treat first column as keys and second column as values
        Map<String, String> details = dataTable.asMap(String.class, String.class);

        // Store the details for later comparison
        for (Map.Entry<String, String> entry : details.entrySet()) {
            contactDetails.put(entry.getKey(), entry.getValue());
        }

        // Enter details in the contact form
        try {
            // Assuming ContactPage has methods to enter these details
            if (details.containsKey("Name")) {
                contactPage.enterName(details.get("Name"));
            }
            if (details.containsKey("Email")) {
                contactPage.enterEmail(details.get("Email"));
            }
            if (details.containsKey("Phone")) {
                contactPage.enterPhone(details.get("Phone"));
            }
            if (details.containsKey("Message")) {
                contactPage.enterMessage(details.get("Message"));
            }

            screenshotUtility.captureScreenshot("ContactDetailsEntered");
        } catch (Exception e) {
            Assert.fail("Failed to enter contact details: " + e.getMessage());
        }
    }

    @And("User clicks on Submit button")
    public void clickSubmitButton() {
        initializePageObjects();
        try {
            contactPage.submitForm();
            screenshotUtility.captureScreenshot("FormSubmitted");
        } catch (Exception e) {
            Assert.fail("Failed to click submit button: " + e.getMessage());
        }
    }

    /**
     * Verify submitted details are displayed on UI
     */
    @Then("User should see the submitted details on UI")
    public void verifySubmittedDetailsOnUI() {
        initializePageObjects();
        try {
            // Verify success message is displayed
            Assert.assertTrue(contactPage.isSuccessMessageDisplayed(), 
                "Success message should be displayed");

            // Verify submitted details are shown
            if (contactDetails.containsKey("Name")) {
                String displayedName = contactPage.getDisplayedName();
                Assert.assertEquals(displayedName, contactDetails.get("Name"), 
                    "Displayed name should match submitted name");
            }

            screenshotUtility.captureScreenshot("UIDetailsVerified");
        } catch (Exception e) {
            Assert.fail("Failed to verify submitted details on UI: " + e.getMessage());
        }
    }

    /**
     * Retrieve contact details using API
     */
    @When("User retrieves the contact details using API")
    public void retrieveContactDetailsViaAPI() {
        try {
            // Using the most recent contact's email to retrieve data
            String userEmail = contactDetails.get("Email");
            
            apiResponse = given()
                    .baseUri(baseURL)
                    .when()
                    .get("/contact");

            Assert.assertEquals(apiResponse.getStatusCode(), 200, 
                "API should return status code 200");

            screenshotUtility.captureScreenshot("APIResponseRetrieved");
        } catch (Exception e) {
            Assert.fail("Failed to retrieve contact details via API: " + e.getMessage());
        }
    }

    /**
     * Verify API response contains submitted data
     */
    @Then("API response should contain the same submitted data")
    public void verifyAPIResponseContainsSubmittedData() {
        try {
            Assert.assertNotNull(apiResponse, "API response should not be null");
            
            String responseBody = apiResponse.getBody().asString();
            
            // Verify that the submitted email is in the response
            if (contactDetails.containsKey("Email")) {
                Assert.assertTrue(responseBody.contains(contactDetails.get("Email")),
                    "API response should contain the submitted email");
            }

            // Verify other submitted details are in response
            if (contactDetails.containsKey("Name")) {
                Assert.assertTrue(responseBody.contains(contactDetails.get("Name")),
                    "API response should contain the submitted name");
            }

            if (contactDetails.containsKey("Phone")) {
                Assert.assertTrue(responseBody.contains(contactDetails.get("Phone")),
                    "API response should contain the submitted phone");
            }

            if (contactDetails.containsKey("Message")) {
                Assert.assertTrue(responseBody.contains(contactDetails.get("Message")),
                    "API response should contain the submitted message");
            }

            screenshotUtility.captureScreenshot("APIDataVerified");
        } catch (Exception e) {
            Assert.fail("Failed to verify API response data: " + e.getMessage());
        }
    }

    /**
     * Compare UI data with API response
     */
    @And("UI data should match with API response")
    public void compareUIAndAPIData() {
        try {
            // Get data from API response
            String apiResponseBody = apiResponse.getBody().asString();
            
            // Get data from UI
            Map<String, String> uiData = new HashMap<>();
            if (contactDetails.containsKey("Name")) {
                uiData.put("Name", contactPage.getDisplayedName());
            }
            if (contactDetails.containsKey("Email")) {
                uiData.put("Email", contactPage.getDisplayedEmail());
            }
            if (contactDetails.containsKey("Phone")) {
                uiData.put("Phone", contactPage.getDisplayedPhone());
            }
            if (contactDetails.containsKey("Message")) {
                uiData.put("Message", contactPage.getDisplayedMessage());
            }

            // Compare UI data with submitted data
            for (Map.Entry<String, String> entry : contactDetails.entrySet()) {
                String key = entry.getKey();
                String submittedValue = entry.getValue();
                String displayedValue = uiData.get(key);

                Assert.assertEquals(displayedValue, submittedValue,
                    "UI displayed " + key + " should match submitted " + key);

                // Also verify in API response
                Assert.assertTrue(apiResponseBody.contains(submittedValue),
                    "API response should contain " + key + ": " + submittedValue);
            }

            screenshotUtility.captureScreenshot("UIAndAPIDataMatch");
        } catch (Exception e) {
            Assert.fail("Failed to compare UI and API data: " + e.getMessage());
        }
    }
}
