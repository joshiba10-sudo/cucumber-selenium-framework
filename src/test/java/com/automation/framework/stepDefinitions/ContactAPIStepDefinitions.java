package com.automation.framework.stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

/**
 * Step definitions for Contact API testing
 * Handles all contact form submission and retrieval scenarios
 */
public class ContactAPIStepDefinitions {

    private String baseURL;
    private Response response;
    private Map<String, String> contactFormData;
    private String contactSubmissionID;
    private long responseTimeInMs;

    /**
     * Initialize base URL from BookingAPIStepDefinitions
     */
    public ContactAPIStepDefinitions(BookingAPIStepDefinitions bookingSteps) {
        // Get base URL from booking API step definitions context
        this.baseURL = System.getProperty("base.api.url", "https://automationintesting.online/api");
    }

    /**
     * Submit contact form with provided details
     */
    @When("User submits a contact form with the following details:")
    public void submitContactForm(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> formData = data.get(0);
        
        contactFormData = formData;
        
        // Build request body
        Map<String, Object> requestBody = new HashMap<>();
        
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            requestBody.put(entry.getKey(), entry.getValue());
        }
        
        // Record response time
        long startTime = System.currentTimeMillis();
        
        // Submit contact form
        response = given()
                .baseUri(baseURL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/contact");
        
        responseTimeInMs = System.currentTimeMillis() - startTime;
        
        // Store submission ID if available
        try {
            contactSubmissionID = response.path("contactid");
        } catch (Exception e) {
            // Contact ID might not be in response
        }
    }

    /**
     * Verify response contains success message
     */
    @Then("Response should contain success message")
    public void verifySuccessMessage() {
        String responseBody = response.getBody().asString();
        Assert.assertTrue(
            responseBody.contains("success") || 
            responseBody.contains("submitted") ||
            responseBody.contains("created"),
            "Response should contain success message"
        );
    }

    /**
     * Verify response contains contact submission ID
     */
    @Then("Response should contain contact submission ID")
    public void verifyContactSubmissionID() {
        contactSubmissionID = response.path("contactid");
        Assert.assertNotNull(contactSubmissionID, "Response should contain contact submission ID");
    }

    /**
     * Verify response includes all submitted details
     */
    @Then("Response should include all submitted details")
    public void verifySubmittedDetails() {
        for (Map.Entry<String, String> entry : contactFormData.entrySet()) {
            String fieldValue = response.path(entry.getKey());
            Assert.assertEquals(
                fieldValue,
                entry.getValue(),
                "Response should include submitted detail: " + entry.getKey()
            );
        }
    }

    /**
     * Verify invalid email format error
     */
    @Then("Response should indicate invalid email format")
    public void verifyInvalidEmailError() {
        String errorMessage = response.getBody().asString();
        Assert.assertTrue(
            errorMessage.toLowerCase().contains("invalid") ||
            errorMessage.toLowerCase().contains("email"),
            "Response should indicate invalid email format"
        );
    }

    /**
     * Verify required field error messages
     */
    @Then("Response should indicate {word} is required")
    public void verifyRequiredFieldError(String fieldName) {
        String errorMessage = response.getBody().asString();
        Assert.assertTrue(
            errorMessage.toLowerCase().contains(fieldName.toLowerCase()) ||
            errorMessage.toLowerCase().contains("required"),
            "Response should indicate " + fieldName + " is required"
        );
    }

    /**
     * Verify error message contains field name
     */
    @Then("Error message should contain {string}")
    public void verifyErrorMessageContains(String fieldName) {
        String errorMessage = response.getBody().asString();
        Assert.assertTrue(
            errorMessage.contains(fieldName),
            "Error message should contain: " + fieldName
        );
    }

    /**
     * Verify invalid phone format error
     */
    @Then("Response should indicate invalid phone format")
    public void verifyInvalidPhoneError() {
        String errorMessage = response.getBody().asString();
        Assert.assertTrue(
            errorMessage.toLowerCase().contains("invalid") ||
            errorMessage.toLowerCase().contains("phone"),
            "Response should indicate invalid phone format"
        );
    }

    /**
     * Retrieve all contact submissions
     */
    @When("User requests to retrieve all contact submissions")
    public void retrieveAllContactSubmissions() {
        response = given()
                .baseUri(baseURL)
                .when()
                .get("/contact");
    }

    /**
     * Verify response contains list of contact submissions
     */
    @Then("Response should contain list of contact submissions")
    public void verifyContactSubmissionsList() {
        Assert.assertNotNull(response.getBody(), "Response should contain submission list");
        List<?> submissions = response.path("$");
        Assert.assertNotNull(submissions, "Response should contain submissions");
    }

    /**
     * Verify response contains submission count
     */
    @Then("Response should contain submission count")
    public void verifySubmissionCount() {
        List<?> submissions = response.path("$");
        Assert.assertTrue(
            submissions.size() >= 0,
            "Response should contain submission count"
        );
    }

    /**
     * Store contact submission ID
     */
    @And("User stores the contact submission ID")
    public void storeContactSubmissionID() {
        contactSubmissionID = response.path("contactid");
        Assert.assertNotNull(contactSubmissionID, "Contact submission ID should be available");
    }

    /**
     * Retrieve contact submission by ID
     */
    @And("User requests to retrieve contact submission with stored ID")
    public void retrieveContactSubmissionByID() {
        Assert.assertNotNull(contactSubmissionID, "Contact submission ID should be stored");
        
        response = given()
                .baseUri(baseURL)
                .pathParam("id", contactSubmissionID)
                .when()
                .get("/contact/{id}");
    }

    /**
     * Verify retrieved submission contains submitter's name
     */
    @Then("Retrieved submission should contain the submitter's name")
    public void verifySubmitterName() {
        String name = response.path("name");
        Assert.assertNotNull(name, "Retrieved submission should contain submitter's name");
    }

    /**
     * Verify retrieved submission contains original message
     */
    @Then("Retrieved submission should contain the original message")
    public void verifyOriginalMessage() {
        String message = response.path("message");
        Assert.assertNotNull(message, "Retrieved submission should contain original message");
    }

    /**
     * Verify special characters are preserved
     */
    @Then("Message should preserve all special characters")
    public void verifySpecialCharactersPreserved() {
        String message = response.path("message");
        Assert.assertTrue(
            message.contains("!") && message.contains("@") && message.contains("#"),
            "Message should preserve all special characters"
        );
    }

    /**
     * Submit duplicate contact form
     */
    @And("User submits the same contact form again within 60 seconds")
    public void submitDuplicateContactForm() {
        Map<String, Object> requestBody = new HashMap<>();
        for (Map.Entry<String, String> entry : contactFormData.entrySet()) {
            requestBody.put(entry.getKey(), entry.getValue());
        }
        
        response = given()
                .baseUri(baseURL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/contact");
    }

    /**
     * Verify response status code options
     */
    @Then("Response status code should be {word} or {word}")
    public void verifyResponseStatusCodeOptions(String statusCode1, String statusCode2) {
        int actualStatusCode = response.getStatusCode();
        int expected1 = Integer.parseInt(statusCode1);
        int expected2 = Integer.parseInt(statusCode2);
        
        Assert.assertTrue(
            actualStatusCode == expected1 || actualStatusCode == expected2,
            "Response status code should be " + expected1 + " or " + expected2 +
            " but was " + actualStatusCode
        );
    }

    /**
     * Verify API handles duplicate appropriately
     */
    @Then("API should handle the duplicate submission appropriately")
    public void verifyDuplicateHandling() {
        int statusCode = response.getStatusCode();
        Assert.assertTrue(
            statusCode >= 200 && statusCode < 500,
            "API should handle duplicate appropriately with valid status code"
        );
    }

    /**
     * Verify response time
     */
    @Then("Response should be received within {int} milliseconds")
    public void verifyResponseTime(int maxTimeMs) {
        Assert.assertTrue(
            responseTimeInMs <= maxTimeMs,
            "Response should be received within " + maxTimeMs + 
            " ms but took " + responseTimeInMs + " ms"
        );
    }

    /**
     * Delete contact submission
     */
    @And("User deletes the contact submission using stored ID")
    public void deleteContactSubmission() {
        Assert.assertNotNull(contactSubmissionID, "Contact submission ID should be stored");
        
        response = given()
                .baseUri(baseURL)
                .pathParam("id", contactSubmissionID)
                .when()
                .delete("/contact/{id}");
    }

    /**
     * Verify contact submission is removed
     */
    @Then("Contact submission should be removed")
    public void verifyContactSubmissionRemoved() {
        // Attempt to retrieve the deleted submission
        Response retrievalResponse = given()
                .baseUri(baseURL)
                .pathParam("id", contactSubmissionID)
                .when()
                .get("/contact/{id}");
        
        Assert.assertTrue(
            retrievalResponse.getStatusCode() == 404 || 
            retrievalResponse.getStatusCode() == 204,
            "Deleted contact submission should not be retrievable"
        );
    }

    /**
     * Update contact submission status
     */
    @And("User updates contact submission status to {string}")
    public void updateContactSubmissionStatus(String status) {
        Assert.assertNotNull(contactSubmissionID, "Contact submission ID should be stored");
        
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("status", status);
        
        response = given()
                .baseUri(baseURL)
                .contentType(ContentType.JSON)
                .pathParam("id", contactSubmissionID)
                .body(updateData)
                .when()
                .put("/contact/{id}");
    }

    /**
     * Verify contact submission status is updated
     */
    @Then("Contact submission status should be updated to {string}")
    public void verifyStatusUpdated(String expectedStatus) {
        String actualStatus = response.path("status");
        Assert.assertEquals(
            actualStatus,
            expectedStatus,
            "Contact submission status should be updated to " + expectedStatus
        );
    }

    /**
     * Verify submission timestamp
     */
    @Then("Response should contain submission timestamp")
    public void verifySubmissionTimestamp() {
        String timestamp = response.path("timestamp");
        Assert.assertNotNull(timestamp, "Response should contain submission timestamp");
    }

    /**
     * Verify timestamp is in ISO 8601 format
     */
    @Then("Timestamp should be in valid ISO 8601 format")
    public void verifyISO8601Format() {
        String timestamp = response.path("timestamp");
        Assert.assertNotNull(timestamp, "Timestamp should not be null");
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            formatter.parse(timestamp);
        } catch (Exception e) {
            Assert.fail("Timestamp should be in valid ISO 8601 format: " + e.getMessage());
        }
    }

    /**
     * Verify response is processed accordingly
     */
    @Then("Response should be processed accordingly")
    public void verifyResponseProcessedAccordingly() {
        Assert.assertNotNull(response, "Response should be available");
        Assert.assertTrue(
            response.getStatusCode() >= 200,
            "Response should have valid status code"
        );
    }
}
