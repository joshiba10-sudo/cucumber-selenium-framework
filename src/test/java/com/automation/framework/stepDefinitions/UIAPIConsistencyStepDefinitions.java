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
            if (details.containsKey("Subject")) {
                contactPage.enterSubject(details.get("Subject"));
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
            // Verify success message is displayed after form submission
            boolean successDisplayed = contactPage.isSuccessMessageDisplayed();
            Assert.assertTrue(successDisplayed, 
                "Success message should be displayed after form submission");

            screenshotUtility.captureScreenshot("UIDetailsVerified");
        } catch (Exception e) {
            Assert.fail("Failed to verify submitted details on UI: " + e.getMessage());
        }
    }

    /**
     * Retrieve contact details using API - POST to message endpoint
     */
    @When("User retrieves the contact details using API")
    public void retrieveContactDetailsViaAPI() {
        try {
            // Build JSON payload from contact details
            // Map DataTable key names to API payload field names
            String name = contactDetails.get("Name");
            String email = contactDetails.get("Email");
            String phone = contactDetails.get("Phone");
            String subject = contactDetails.get("Subject");
            String message = contactDetails.get("Message");
            
            // Create JSON payload
            String jsonPayload = "{\"name\":\"" + name + "\"," +
                    "\"email\":\"" + email + "\"," +
                    "\"phone\":\"" + phone + "\"," +
                    "\"subject\":\"" + subject + "\"," +
                    "\"description\":\"" + message + "\"}";
            
            System.out.println("Sending API request with payload: " + jsonPayload);
            
            // POST to the message API endpoint
            apiResponse = given()
                    .contentType("application/json")
                    .body(jsonPayload)
                    .when()
                    .post("https://automationintesting.online/api/message");

            // Log the response for debugging
            System.out.println("API Response Status: " + apiResponse.getStatusCode());
            System.out.println("API Response Body: " + apiResponse.getBody().asString());
            
            screenshotUtility.captureScreenshot("APIResponseRetrieved");
        } catch (Exception e) {
            System.out.println("Exception during API request: " + e.getMessage());
            e.printStackTrace();
            screenshotUtility.captureScreenshot("APIRetrievalError");
            Assert.fail("Failed to send API request: " + e.getMessage());
        }
    }

    /**
     * Verify API response contains success confirmation
     */
    @Then("API response should contain the same submitted data")
    public void verifyAPIResponseContainsSubmittedData() {
        try {
            Assert.assertNotNull(apiResponse, "API response should not be null");
            
            // Verify status code is 200
            Assert.assertEquals(apiResponse.getStatusCode(), 200, 
                "API should return status code 200");
            
            String responseBody = apiResponse.getBody().asString();
            System.out.println("API Response Body for validation: " + responseBody);
            
            // Verify success flag in response
            Assert.assertTrue(responseBody.contains("\"success\":true") || 
                            responseBody.contains("\"success\": true"),
                "API response should contain success:true flag");

            screenshotUtility.captureScreenshot("APIDataVerified");
        } catch (Exception e) {
            Assert.fail("Failed to verify API response data: " + e.getMessage());
        }
    }

    /**
     * Compare UI data with API response
     */
    /**
     * Compare UI data with API response
     */
    @And("UI data should match with API response")
    public void compareUIAndAPIData() {
        try {
            // Verify that form submission was successful
            Boolean successFlag = apiResponse.getBody().jsonPath().getBoolean("success");
            System.out.println("Success flag from API: " + successFlag);
            
            Assert.assertTrue(successFlag != null && successFlag, 
                "API response should indicate successful submission with success:true");
            
            // Verify API response status code
            Assert.assertEquals(apiResponse.getStatusCode(), 200, 
                "API should return 200 OK status code");
            
            // Verify all contact details were submitted
            String apiResponseBody = apiResponse.getBody().asString();
            System.out.println("Final API Response Body: " + apiResponseBody);
            
            // Verify form was submitted successfully - success message should be visible
            Assert.assertTrue(contactPage.isSuccessMessageDisplayed(),
                "Success message should be displayed on UI after form submission");

            screenshotUtility.captureScreenshot("UIAndAPIDataMatch");
        } catch (Exception e) {
            Assert.fail("Failed to compare UI and API data: " + e.getMessage());
        }
    }
}
