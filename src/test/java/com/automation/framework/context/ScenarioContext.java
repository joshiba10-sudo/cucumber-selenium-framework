package com.automation.framework.context;

import java.util.HashMap;
import java.util.Map;

/**
 * ScenarioContext: Thread-safe context for sharing data between UI and API layers
 * Provides a mechanism to pass data (contact details, form inputs, API responses) 
 * across different step definitions within the same scenario
 */
public class ScenarioContext {
    
    private final Map<String, Object> contextData = new HashMap<>();
    private static final ThreadLocal<ScenarioContext> contextThreadLocal = ThreadLocal.withInitial(ScenarioContext::new);
    
    /**
     * Get the current scenario context
     */
    public static ScenarioContext getInstance() {
        return contextThreadLocal.get();
    }
    
    /**
     * Clear the context (typically called in hooks after scenario)
     */
    public static void clear() {
        contextThreadLocal.remove();
    }
    
    /**
     * Store data with a key
     */
    public void setData(String key, Object value) {
        contextData.put(key, value);
    }
    
    /**
     * Retrieve data by key
     */
    public Object getData(String key) {
        return contextData.get(key);
    }
    
    /**
     * Retrieve data by key with type casting
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key, Class<T> type) {
        Object value = contextData.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Check if key exists in context
     */
    public boolean hasData(String key) {
        return contextData.containsKey(key);
    }
    
    /**
     * Remove data from context
     */
    public void removeData(String key) {
        contextData.remove(key);
    }
    
    /**
     * Get all context data
     */
    public Map<String, Object> getAllData() {
        return new HashMap<>(contextData);
    }
    
    /**
     * Store contact form data
     */
    public void setContactData(Map<String, String> contactData) {
        setData("contactData", new HashMap<>(contactData));
    }
    
    /**
     * Get contact form data
     */
    public Map<String, String> getContactData() {
        return getData("contactData", Map.class);
    }
    
    /**
     * Store API response data
     */
    public void setApiResponse(Map<String, Object> apiResponse) {
        setData("apiResponse", new HashMap<>(apiResponse));
    }
    
    /**
     * Get API response data
     */
    public Map<String, Object> getApiResponse() {
        return getData("apiResponse", Map.class);
    }
}
