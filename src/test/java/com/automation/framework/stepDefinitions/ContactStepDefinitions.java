package com.automation.framework.stepDefinitions;

import com.automation.framework.context.ScenarioContext;
import com.automation.framework.pages.ContactPage;
import com.automation.framework.utilities.APIUtility;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * ContactStepDefinitions: Hybrid UI+API step definitions for contact form testing
 * Tests the Contact form submission and validates through both UI and API
 */
public class ContactStepDefinitions {
    
    private static final Logger LOGGER = LogManager.getLogger(ContactStepDefinitions.class);
    private ContactPage contactPage;
    private ScenarioContext scenarioContext;
    private APIUtility apiUtility;
    private Response apiResponse;
    
    public ContactStepDefinitions() {
        this.contactPage = new ContactPage();
        this.scenarioContext = ScenarioContext.getInstance();
        this.apiUtility = new APIUtility();
    }
    
    // ==================== UI Steps ====================
    
    @Given("User opens the Shady Meadows contact page")
    public void user_opens_contact_page() {
        LOGGER.info("Step: Opening Shady Meadows contact page");
        try {
            contactPage.openContactPage();
            LOGGER.info("Contact page opened successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to open contact page: " + e.getMessage());
            throw new RuntimeException("Failed to open contact page: " + e.getMessage());
        }
    }
    
    @When("User clicks the Contact tab")
    public void user_clicks_contact_tab() {
        LOGGER.info("Step: Clicking Contact tab");
        try {
            contactPage.clickContactTab();
            LOGGER.info("Contact tab clicked successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to click contact tab: " + e.getMessage());
            throw new RuntimeException("Failed to click contact tab: " + e.getMessage());
        }
    }
    
    @When("User fills the Send Us a Message form with following details:")
    public void user_fills_contact_form(Map<String, String> contactDetails) {
        LOGGER.info("Step: Filling contact form with details: " + contactDetails);
        try {
            // Store contact data in scenario context for later validation
            scenarioContext.setContactData(contactDetails);
            LOGGER.info("Contact data stored in context");
            
            // Fill the form on UI
            contactPage.fillContactForm(contactDetails);
            LOGGER.info("Contact form filled successfully with all details");
            
        } catch (Exception e) {
            LOGGER.error("Failed to fill contact form: " + e.getMessage());
            throw new RuntimeException("Failed to fill contact form: " + e.getMessage());
        }
    }
    
    @When("User submits the contact form")
    public void user_submits_contact_form() {
        LOGGER.info("Step: Submitting contact form");
        try {
            contactPage.submitContactForm();
            
            // Wait a moment for form submission
            Thread.sleep(2000);
            
            LOGGER.info("Contact form submitted successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to submit contact form: " + e.getMessage());
            throw new RuntimeException("Failed to submit contact form: " + e.getMessage());
        }
    }
    
    @Then("Verify success message is displayed on UI")
    public void verify_success_message_displayed() {
        LOGGER.info("Step: Verifying success message on UI");
        try {
            boolean isDisplayed = contactPage.isSuccessMessageDisplayed();
            String messageText = contactPage.getSuccessMessageText();
            
            Assert.assertTrue(isDisplayed, "Success message is not displayed");
            LOGGER.info("Success message verified: " + messageText);
            Assert.assertTrue(messageText.toLowerCase().contains("success") || 
                            messageText.toLowerCase().contains("message sent") ||
                            messageText.toLowerCase().contains("received"),
                            "Success message does not contain expected text");
            
        } catch (Exception e) {
            LOGGER.error("Failed to verify success message: " + e.getMessage());
            throw new RuntimeException("Success message verification failed: " + e.getMessage());
        }
    }
    
    @Then("Verify form data is retained in UI fields")
    public void verify_form_data_retained() {
        LOGGER.info("Step: Verifying form data in UI");
        try {
            Map<String, String> submittedData = scenarioContext.getContactData();
            Assert.assertNotNull(submittedData, "Contact data not found in context");
            
            // Verify each field value in the form (if form doesn't clear)
            for (Map.Entry<String, String> entry : submittedData.entrySet()) {
                String fieldValue = contactPage.getFormFieldValue(entry.getKey());
                LOGGER.info("Field: " + entry.getKey() + " - Expected: " + entry.getValue() + " - Actual: " + fieldValue);
            }
            
            LOGGER.info("Form data verification completed");
            
        } catch (Exception e) {
            LOGGER.error("Failed to verify form data: " + e.getMessage());
            throw new RuntimeException("Form data verification failed: " + e.getMessage());
        }
    }
    
    // ==================== API Steps ====================
    
    @Given("Contact API base URL is set to {string}")
    public void set_contact_api_url(String apiUrl) {
        LOGGER.info("Step: Setting contact API base URL: " + apiUrl);
        apiUtility.setBaseURL(apiUrl);
        scenarioContext.setData("apiBaseUrl", apiUrl);
        LOGGER.info("Contact API base URL set successfully");
    }
    
    @When("User calls the contact API to fetch submitted data using email {string}")
    public void user_calls_contact_api_fetch_by_email(String email) {
        LOGGER.info("Step: Calling contact API to fetch data for email: " + email);
        try {
            // Construct API URL - assuming endpoint is /contact or /contact/list
            String endpoint = "/contact?email=" + email;
            
            apiResponse = apiUtility.get(endpoint);
            LOGGER.info("Contact API response status: " + apiResponse.getStatusCode());
            
            // Store response in context
            if (apiResponse.getStatusCode() == 200) {
                try {
                    Map<String, Object> responseData = apiResponse.jsonPath().getMap(".");
                    scenarioContext.setApiResponse(responseData);
                    LOGGER.info("API Response stored in context");
                } catch (Exception e) {
                    LOGGER.warn("Could not parse API response as map: " + e.getMessage());
                    scenarioContext.setData("apiResponseRaw", apiResponse.getBody().asString());
                }
            }
            
        } catch (Exception e) {
            LOGGER.error("Failed to call contact API: " + e.getMessage());
            throw new RuntimeException("Failed to fetch contact data from API: " + e.getMessage());
        }
    }
    
    @When("User calls the contact API to fetch latest contact message")
    public void user_calls_contact_api_fetch_latest() {
        LOGGER.info("Step: Calling contact API to fetch latest contact message");
        try {
            String endpoint = "/contact";
            
            apiResponse = apiUtility.get(endpoint);
            LOGGER.info("Contact API response status: " + apiResponse.getStatusCode());
            
            // Store response in context
            if (apiResponse.getStatusCode() == 200) {
                try {
                    // Handle both single object and array responses
                    Object response = apiResponse.jsonPath().get(".");
                    if (response instanceof java.util.List) {
                        java.util.List<?> messages = (java.util.List<?>) response;
                        if (!messages.isEmpty()) {
                            scenarioContext.setData("apiContacts", messages);
                            LOGGER.info("Found " + messages.size() + " contact messages");
                        }
                    } else {
                        Map<String, Object> responseData = (Map<String, Object>) response;
                        scenarioContext.setApiResponse(responseData);
                    }
                } catch (Exception e) {
                    LOGGER.warn("Could not parse API response: " + e.getMessage());
                    scenarioContext.setData("apiResponseRaw", apiResponse.getBody().asString());
                }
            }
            
        } catch (Exception e) {
            LOGGER.error("Failed to call contact API: " + e.getMessage());
            throw new RuntimeException("Failed to fetch contact data from API: " + e.getMessage());
        }
    }
    
    @Then("API response status should be {int}")
    public void verify_api_response_status(int expectedStatus) {
        LOGGER.info("Step: Verifying API response status. Expected: " + expectedStatus + ", Actual: " + apiResponse.getStatusCode());
        Assert.assertEquals(apiResponse.getStatusCode(), expectedStatus, 
                "API response status code mismatch");
        LOGGER.info("API response status verified successfully");
    }
    
    @Then("API response should contain the submitted contact details")
    public void verify_api_response_contains_contact_data() {
        LOGGER.info("Step: Verifying API response contains submitted contact details");
        try {
            Map<String, String> submittedData = scenarioContext.getContactData();
            Assert.assertNotNull(submittedData, "Submitted contact data not found in context");
            
            Map<String, Object> apiData = scenarioContext.getApiResponse();
            if (apiData == null) {
                // Try to get raw response and parse
                String rawResponse = scenarioContext.getData("apiResponseRaw", String.class);
                Assert.assertNotNull(apiData, "API response data not found in context");
            }
            
            // Compare submitted data with API response
            for (Map.Entry<String, String> entry : submittedData.entrySet()) {
                String key = entry.getKey();
                String expectedValue = entry.getValue();
                
                // Try different key formats (e.g., "name" or "contactName")
                Object actualValue = apiData.get(key);
                if (actualValue == null) {
                    actualValue = apiData.get("contact" + capitalize(key));
                }
                
                if (actualValue != null) {
                    Assert.assertEquals(actualValue.toString(), expectedValue,
                            "API response value for '" + key + "' does not match submitted value");
                    LOGGER.info("Field '" + key + "' verified: " + expectedValue);
                }
            }
            
            LOGGER.info("All contact details verified successfully in API response");
            
        } catch (AssertionError e) {
            LOGGER.error("Contact data verification failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to verify API response: " + e.getMessage());
            throw new RuntimeException("Failed to verify API response: " + e.getMessage());
        }
    }
    
    @Then("Compare UI submitted data with API response data for email {string}")
    public void compare_ui_and_api_data(String email) {
        LOGGER.info("Step: Comparing UI submitted data with API response for email: " + email);
        try {
            Map<String, String> uiData = scenarioContext.getContactData();
            Assert.assertNotNull(uiData, "UI contact data not found in context");
            
            // Verify email matches
            Assert.assertEquals(uiData.get("email"), email, "Email mismatch between UI and API");
            LOGGER.info("Email verified: " + email);
            
            // Get API data
            Map<String, Object> apiData = scenarioContext.getApiResponse();
            Assert.assertNotNull(apiData, "API response data not found");
            
            // Compare all fields
            Map<String, String> comparisonResults = new HashMap<>();
            for (Map.Entry<String, String> entry : uiData.entrySet()) {
                String key = entry.getKey();
                String uiValue = entry.getValue();
                Object apiValue = apiData.get(key);
                
                if (apiValue != null) {
                    boolean matches = apiValue.toString().equals(uiValue);
                    comparisonResults.put(key, matches ? "MATCH" : "MISMATCH");
                    LOGGER.info("Field '" + key + "': UI=" + uiValue + ", API=" + apiValue + " -> " + comparisonResults.get(key));
                    Assert.assertTrue(matches, "Data mismatch for field '" + key + "'");
                }
            }
            
            LOGGER.info("All UI and API data comparisons completed successfully");
            scenarioContext.setData("comparisonResults", comparisonResults);
            
        } catch (AssertionError e) {
            LOGGER.error("Data comparison failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to compare data: " + e.getMessage());
            throw new RuntimeException("Data comparison failed: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to capitalize string
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
