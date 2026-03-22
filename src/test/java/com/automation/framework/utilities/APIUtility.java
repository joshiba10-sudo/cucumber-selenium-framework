package com.automation.framework.utilities;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * APIUtility: Utility class for making API calls using RestAssured
 * Handles GET, POST, PUT, DELETE requests with proper logging and response handling
 */
public class APIUtility {

    private static final Logger LOGGER = LogManager.getLogger(APIUtility.class);
    private String baseURL;
    private RequestSpecification requestSpec;
    private int connectionTimeout;
    private int responseTimeout;

    public APIUtility() {
        this.baseURL = ConfigReader.getProperty("api.base.url", "https://automationintesting.online/api");
        this.connectionTimeout = Integer.parseInt(ConfigReader.getProperty("api.connection.timeout", "5000"));
        this.responseTimeout = Integer.parseInt(ConfigReader.getProperty("api.timeout", "5000"));
        setupRestAssured();
    }

    /**
     * Setup RestAssured base configuration
     */
    private void setupRestAssured() {
        RestAssured.baseURI = this.baseURL;
        RestAssured.basePath = "";
        LOGGER.info("RestAssured configured with base URL: " + this.baseURL);
    }

    /**
     * Set custom base URL
     */
    public void setBaseURL(String url) {
        this.baseURL = url;
        RestAssured.baseURI = url;
        LOGGER.info("Base URL updated to: " + url);
    }

    /**
     * Build request specification with common headers
     */
    private RequestSpecification buildRequestSpec() {
        RequestSpecification spec = RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .log().all();
        
        return spec;
    }

    /**
     * Perform GET request
     */
    public Response get(String endpoint) {
        LOGGER.info("Performing GET request to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("GET request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing GET request: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform GET request with query parameters
     */
    public Response getWithQueryParams(String endpoint, String... params) {
        LOGGER.info("Performing GET request to endpoint: " + endpoint + " with query parameters");
        try {
            RequestSpecification spec = buildRequestSpec();
            
            // Add query parameters in pairs
            for (int i = 0; i < params.length; i += 2) {
                String key = params[i];
                String value = params[i + 1];
                spec = spec.queryParam(key, value);
                LOGGER.info("Added query parameter: " + key + " = " + value);
            }
            
            Response response = spec
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("GET request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing GET request with query params: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform GET request with proxy or additional headers
     */
    public Response getWithHeader(String endpoint, String headerName, String headerValue) {
        LOGGER.info("Performing GET request with custom header: " + headerName);
        try {
            Response response = buildRequestSpec()
                .header(headerName, headerValue)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("GET request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing GET request with header: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform POST request
     */
    public Response post(String endpoint, Object body) {
        LOGGER.info("Performing POST request to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("POST request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing POST request: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform POST request with form data
     */
    public Response postFormData(String endpoint, Map<String, String> formData) {
        LOGGER.info("Performing POST request with form data to endpoint: " + endpoint);
        try {
            RequestSpecification spec = RestAssured.given()
                .accept(ContentType.JSON)
                .log().all();
            
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                spec = spec.formParam(entry.getKey(), entry.getValue());
            }
            
            Response response = spec
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("POST form data request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing POST request with form data: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform POST request with custom headers
     */
    public Response postWithHeader(String endpoint, Object body, String headerName, String headerValue) {
        LOGGER.info("Performing POST request with custom header to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .header(headerName, headerValue)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("POST request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing POST request with header: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform PUT request
     */
    public Response put(String endpoint, Object body) {
        LOGGER.info("Performing PUT request to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("PUT request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing PUT request: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform PUT request with custom headers
     */
    public Response putWithHeader(String endpoint, Object body, String headerName, String headerValue) {
        LOGGER.info("Performing PUT request with custom header to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .header(headerName, headerValue)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("PUT request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing PUT request with header: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform PATCH request
     */
    public Response patch(String endpoint, Object body) {
        LOGGER.info("Performing PATCH request to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("PATCH request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing PATCH request: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform DELETE request
     */
    public Response delete(String endpoint) {
        LOGGER.info("Performing DELETE request to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("DELETE request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing DELETE request: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform DELETE request with body
     */
    public Response deleteWithBody(String endpoint, Object body) {
        LOGGER.info("Performing DELETE request with body to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .body(body)
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("DELETE request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing DELETE request with body: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform DELETE request with custom headers
     */
    public Response deleteWithHeader(String endpoint, String headerName, String headerValue) {
        LOGGER.info("Performing DELETE request with custom header to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .header(headerName, headerValue)
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("DELETE request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing DELETE request with header: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform HEAD request
     */
    public Response head(String endpoint) {
        LOGGER.info("Performing HEAD request to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .when()
                .head(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("HEAD request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing HEAD request: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Validate HTTP status code
     */
    public boolean validateStatusCode(Response response, int expectedStatus) {
        LOGGER.info("Validating response status code. Expected: " + expectedStatus + 
                   " Actual: " + response.getStatusCode());
        return response.getStatusCode() == expectedStatus;
    }

    /**
     * Extract value from response using JSON path
     */
    public String extractValueFromResponse(Response response, String jsonPath) {
        LOGGER.info("Extracting value from response using path: " + jsonPath);
        try {
            String value = response.jsonPath().getString(jsonPath);
            LOGGER.info("Extracted value: " + value);
            return value;
        } catch (Exception e) {
            LOGGER.error("Error extracting value from response: " + e.getMessage());
            return null;
        }
    }

    /**
     * Validate response contains expected text
     */
    public boolean validateResponseContains(Response response, String expectedText) {
        LOGGER.info("Validating response contains: " + expectedText);
        String responseBody = response.getBody().asString();
        return responseBody.contains(expectedText);
    }

    /**
     * Get response body as String
     */
    public String getResponseBody(Response response) {
        LOGGER.info("Getting response body as string");
        return response.getBody().asString();
    }

    /**
     * Get response content type
     */
    public String getResponseContentType(Response response) {
        LOGGER.info("Getting response content type");
        return response.getContentType();
    }

    /**
     * Get response headers
     */
    public String getResponseHeader(Response response, String headerName) {
        LOGGER.info("Getting response header: " + headerName);
        return response.getHeader(headerName);
    }

    /**
     * Get all response headers
     */
    public io.restassured.http.Headers getAllResponseHeaders(Response response) {
        LOGGER.info("Getting all response headers");
        return response.getHeaders();
    }
}
