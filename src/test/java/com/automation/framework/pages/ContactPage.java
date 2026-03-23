package com.automation.framework.pages;

import com.automation.framework.base.BaseClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * ContactPage: Page Object for the Contact form on Shady Meadows website
 * Handles interactions with the "Send Us a Message" form
 * URL: https://automationintesting.online/#contact
 */
public class ContactPage extends BasePage {
    
    private static final Logger LOGGER = LogManager.getLogger(ContactPage.class);
    
    // Locators for Contact Tab and Form Elements
    private By contactTabLocator = By.xpath("//div[@id='navbarNav']//a[contains(text(), 'Contact')]");
    private By nameInputLocator = By.id("name");
    private By emailInputLocator = By.id("email");
    private By phoneInputLocator = By.id("phone");
    private By subjectInputLocator = By.id("subject");
    private By messageTextareaLocator = By.xpath("//textarea[@id='description']");
    private By submitButtonLocator = By.xpath("//button[contains(text(), 'Submit')] | //button[@class='btn' and contains(text(), 'Send')]");
    private By successMessageLocator = By.xpath("//h3[contains(text(),'Thanks for getting in touch')]");
    private By formContainerLocator = By.id("contact");
    
    /**
     * Open the Shady Meadows website contact page
     */
    public void openContactPage() {
        LOGGER.info("Opening Shady Meadows contact page");
        try {
            BaseClass.driver.navigate().to("https://automationintesting.online/");
            waitForPageLoad();
            LOGGER.info("Contact page opened successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to open contact page: " + e.getMessage());
            throw new RuntimeException("Could not open contact page");
        }
    }
    
    /**
     * Click on the Contact tab/section
     */
    public void clickContactTab() {
        LOGGER.info("Clicking on Contact tab");
        try {
            click(contactTabLocator);
            LOGGER.info("Contact tab clicked successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to click contact tab: " + e.getMessage());
            throw new RuntimeException("Could not click contact tab");
        }
    }
    
    /**
     * Fill the contact form with provided details
     * @param contactDetails Map containing name, email, phone, subject, message
     */
    public void fillContactForm(java.util.Map<String, String> contactDetails) {
        LOGGER.info("Filling contact form with details: " + contactDetails);
        try {
            // Fill Name
            if (contactDetails.containsKey("name")) {
                type(nameInputLocator, contactDetails.get("name"));
                LOGGER.info("Name entered: " + contactDetails.get("name"));
            }
            
            // Fill Email
            if (contactDetails.containsKey("email")) {
                type(emailInputLocator, contactDetails.get("email"));
                LOGGER.info("Email entered: " + contactDetails.get("email"));
            }
            
            // Fill Phone
            if (contactDetails.containsKey("phone")) {
                type(phoneInputLocator, contactDetails.get("phone"));
                LOGGER.info("Phone entered: " + contactDetails.get("phone"));
            }
            
            // Fill Subject
            if (contactDetails.containsKey("subject")) {
                type(subjectInputLocator, contactDetails.get("subject"));
                LOGGER.info("Subject entered: " + contactDetails.get("subject"));
            }
            
            // Fill Message
            if (contactDetails.containsKey("message")) {
                type(messageTextareaLocator, contactDetails.get("message"));
                LOGGER.info("Message entered: " + contactDetails.get("message"));
            }
            
            LOGGER.info("Contact form filled successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to fill contact form: " + e.getMessage());
            throw new RuntimeException("Could not fill contact form: " + e.getMessage());
        }
    }
    
    /**
     * Submit the contact form
     */
    public void submitContactForm() {
        LOGGER.info("Submitting contact form");
        try {
            click(submitButtonLocator);
            LOGGER.info("Contact form submitted");
        } catch (Exception e) {
            LOGGER.error("Failed to submit contact form: " + e.getMessage());
            throw new RuntimeException("Could not submit contact form: " + e.getMessage());
        }
    }
    
    /**
     * Verify success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        LOGGER.info("Verifying success message");
        try {
            return isElementDisplayed(successMessageLocator);
        } catch (Exception e) {
            LOGGER.warn("Success message not found: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get success message text
     */
    public String getSuccessMessageText() {
        LOGGER.info("Getting success message text");
        try {
            String messageText = getText(successMessageLocator);
            LOGGER.info("Success message: " + messageText);
            return messageText;
        } catch (Exception e) {
            LOGGER.error("Failed to get success message: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Get form input value (for verification)
     */
    public String getFormFieldValue(String fieldName) {
        LOGGER.info("Getting form field value for: " + fieldName);
        try {
            By locator = null;
            switch (fieldName.toLowerCase()) {
                case "name":
                    locator = nameInputLocator;
                    break;
                case "email":
                    locator = emailInputLocator;
                    break;
                case "phone":
                    locator = phoneInputLocator;
                    break;
                case "subject":
                    locator = subjectInputLocator;
                    break;
                case "message":
                    locator = messageTextareaLocator;
                    break;
            }
            
            if (locator != null) {
                String value = getAttribute(locator, "value");
                if (value == null || value.isEmpty()) {
                    value = getText(locator);
                }
                LOGGER.info("Field '" + fieldName + "' value: " + value);
                return value;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get form field value: " + e.getMessage());
        }
        return "";
    }


    /**
     * Enter name in the contact form
     * @param name Name to enter
     */
    public void enterName(String name) {
        LOGGER.info("Entering name: " + name);
        try {
            scrollToElement(nameInputLocator);
            Thread.sleep(200);
            type(nameInputLocator, name);
            LOGGER.info("Name entered successfully: " + name);
        } catch (Exception e) {
            LOGGER.error("Failed to enter name: " + e.getMessage());
            throw new RuntimeException("Could not enter name: " + e.getMessage());
        }
    }

    /**
     * Enter email in the contact form
     * @param email Email to enter
     */
    public void enterEmail(String email) {
        LOGGER.info("Entering email: " + email);
        try {
            scrollToElement(emailInputLocator);
            Thread.sleep(200);
            type(emailInputLocator, email);
            LOGGER.info("Email entered successfully: " + email);
        } catch (Exception e) {
            LOGGER.error("Failed to enter email: " + e.getMessage());
            throw new RuntimeException("Could not enter email: " + e.getMessage());
        }
    }

    /**
     * Enter phone in the contact form
     * @param phone Phone to enter
     */
    public void enterPhone(String phone) {
        LOGGER.info("Entering phone: " + phone);
        try {
            scrollToElement(phoneInputLocator);
            Thread.sleep(200);
            type(phoneInputLocator, phone);
            LOGGER.info("Phone entered successfully: " + phone);
        } catch (Exception e) {
            LOGGER.error("Failed to enter phone: " + e.getMessage());
            throw new RuntimeException("Could not enter phone: " + e.getMessage());
        }
    }

    /**
     * Enter subject in the contact form
     * @param subject Subject to enter
     */
    public void enterSubject(String subject) {
        LOGGER.info("Entering subject: " + subject);
        try {
            scrollToElement(subjectInputLocator);
            Thread.sleep(200);
            type(subjectInputLocator, subject);
            LOGGER.info("Subject entered successfully: " + subject);
        } catch (Exception e) {
            LOGGER.error("Failed to enter subject: " + e.getMessage());
            throw new RuntimeException("Could not enter subject: " + e.getMessage());
        }
    }

    /**
     * Enter message in the contact form
     * @param message Message to enter
     */
    public void enterMessage(String message) {
        LOGGER.info("Entering message: " + message);
        try {
            scrollToElement(messageTextareaLocator);
            Thread.sleep(200);
            type(messageTextareaLocator, message);
            LOGGER.info("Message entered successfully: " + message);
        } catch (Exception e) {
            LOGGER.error("Failed to enter message: " + e.getMessage());
            throw new RuntimeException("Could not enter message: " + e.getMessage());
        }
    }

    /**
     * Submit the contact form using submit button
     */
    public void submitForm() {
        LOGGER.info("Submitting contact form");
        try {
            // Scroll into view before clicking to avoid element intercepted error
            scrollToElement(submitButtonLocator);
            Thread.sleep(500); // Brief pause to allow page to settle
            click(submitButtonLocator);
            LOGGER.info("Form submitted successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to submit form: " + e.getMessage());
            throw new RuntimeException("Could not submit form: " + e.getMessage());
        }
    }

    /**
     * Get displayed name from the form or success message
     * @return Name displayed on UI
     */
    public String getDisplayedName() {
        LOGGER.info("Getting displayed name");
        try {
            String name = getFormFieldValue("name");
            LOGGER.info("Displayed name: " + name);
            return name;
        } catch (Exception e) {
            LOGGER.warn("Failed to get displayed name: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get displayed email from the form or success message
     * @return Email displayed on UI
     */
    public String getDisplayedEmail() {
        LOGGER.info("Getting displayed email");
        try {
            String email = getFormFieldValue("email");
            LOGGER.info("Displayed email: " + email);
            return email;
        } catch (Exception e) {
            LOGGER.warn("Failed to get displayed email: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get displayed phone from the form or success message
     * @return Phone displayed on UI
     */
    public String getDisplayedPhone() {
        LOGGER.info("Getting displayed phone");
        try {
            String phone = getFormFieldValue("phone");
            LOGGER.info("Displayed phone: " + phone);
            return phone;
        } catch (Exception e) {
            LOGGER.warn("Failed to get displayed phone: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get displayed message from the form or success message
     * @return Message displayed on UI
     */
    public String getDisplayedMessage() {
        LOGGER.info("Getting displayed message");
        try {
            String message = getFormFieldValue("message");
            LOGGER.info("Displayed message: " + message);
            return message;
        } catch (Exception e) {
            LOGGER.warn("Failed to get displayed message: " + e.getMessage());
            return "";
        }
    }
}
