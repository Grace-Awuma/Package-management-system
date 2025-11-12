package com.pms.management.system.service;

import com.pms.management.system.model.ParcelPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class AIAssistantService {
    
    private static final Logger logger = LoggerFactory.getLogger(AIAssistantService.class);
    
    @Value("${google.gemini.api.key:}")
    private String apiKey;
    
    @Autowired
    private PackageService packageService;
    
    /**
     * Get AI response using Google Gemini
     */
    public String getAIResponse(String userMessage, Long residentId) {
        try {
            // Get package context
            String packageContext = "";
            if (residentId != null) {
                List<ParcelPackage> packages = packageService.getPackagesByResident(residentId);
                packageContext = buildPackageContext(packages);
            }
            
            // Build the prompt
            String systemPrompt = "You are a helpful package management assistant. " +
                "You help residents track their packages. Be friendly and concise.\n\n" +
                "Current package information:\n" + packageContext;
            
            String fullPrompt = systemPrompt + "\n\nUser question: " + userMessage + 
                "\n\nProvide a helpful, friendly response in 2-3 sentences.";
            
            // Use Gemini API if key exists
            if (apiKey != null && !apiKey.isEmpty()) {
                return callGeminiAPI(fullPrompt);
            } else {
                return getPatternBasedResponse(userMessage, packageContext);
            }
            
        } catch (Exception e) {
            logger.error("Error getting AI response: {}", e.getMessage(), e);
            return "I'm having trouble right now. Please contact the front desk.";
        }
    }
    
    /**
     * Call Google Gemini API - COMPLETELY FREE!
     */
    private String callGeminiAPI(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            
            // Gemini API endpoint
            String url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + apiKey;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            
            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();
            
            List<Map<String, String>> parts = new ArrayList<>();
            Map<String, String> part = new HashMap<>();
            part.put("text", prompt);
            parts.add(part);
            
            content.put("parts", parts);
            contents.add(content);
            
            requestBody.put("contents", contents);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> candidate = candidates.get(0);
                    Map<String, Object> candidateContent = (Map<String, Object>) candidate.get("content");
                    List<Map<String, Object>> candidateParts = (List<Map<String, Object>>) candidateContent.get("parts");
                    
                    if (candidateParts != null && !candidateParts.isEmpty()) {
                        return (String) candidateParts.get(0).get("text");
                    }
                }
            }
            
            return "I couldn't process that. Please try again.";
            
        } catch (Exception e) {
            logger.error("Error calling Gemini API: {}", e.getMessage(), e);
            return "I'm having trouble connecting. Please try again.";
        }
    }
    
    /**
     * Fallback pattern-based responses (works without API)
     */
    private String getPatternBasedResponse(String message, String packageContext) {
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("package") && (lowerMessage.contains("where") || lowerMessage.contains("status"))) {
            if (packageContext.contains("No packages")) {
                return "You don't have any packages currently. When a package arrives, you'll be notified by email or SMS.";
            }
            return "Here's what I found:\n\n" + packageContext + 
                   "\n\nPickup hours: Monday-Friday 9 AM - 6 PM, Saturday 10 AM - 4 PM.";
        }
        
        if (lowerMessage.contains("pick") && lowerMessage.contains("up")) {
            return "📦 Pickup Hours:\n" +
                   "• Monday-Friday: 9 AM - 6 PM\n" +
                   "• Saturday: 10 AM - 4 PM\n" +
                   "• Sunday: Closed\n\n" +
                   "Bring a valid ID when picking up packages.";
        }
        
        if (lowerMessage.contains("hello") || lowerMessage.contains("hi")) {
            return "👋 Hi! I can help you track your packages, check pickup hours, and answer delivery questions. What would you like to know?";
        }
        
        if (lowerMessage.contains("thank")) {
            return "You're welcome! Feel free to ask anything else. 😊";
        }
        
        return "I can help with:\n" +
               "• 'Where is my package?'\n" +
               "• 'When can I pick up?'\n" +
               "• 'What are pickup hours?'\n\n" +
               "What would you like to know?";
    }
    
    /**
     * Build package context string
     */
    private String buildPackageContext(List<ParcelPackage> packages) {
        if (packages == null || packages.isEmpty()) {
            return "No packages found.";
        }
        
        StringBuilder context = new StringBuilder();
        context.append("You have ").append(packages.size()).append(" package(s):\n\n");
        
        for (int i = 0; i < Math.min(packages.size(), 5); i++) {
            ParcelPackage pkg = packages.get(i);
            context.append((i + 1)).append(". ")
                   .append(pkg.getCarrier()).append(" - ")
                   .append(pkg.getStatus()).append("\n")
                   .append("   Tracking: ").append(pkg.getTrackingNumber()).append("\n")
                   .append("   Arrived: ").append(pkg.getFormattedDeliveryDate()).append("\n");
            
            if (pkg.getStorageLocation() != null) {
                context.append("   Location: ").append(pkg.getStorageLocation()).append("\n");
            }
            context.append("\n");
        }
        
        return context.toString();
    }
}