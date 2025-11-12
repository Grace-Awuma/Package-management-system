/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pms.management.system.controller;

import com.pms.management.system.service.AIAssistantService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/chat")
public class ChatController {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    @Autowired
    private AIAssistantService aiAssistantService;
    
    /**
     * Handle chat messages from users
     */
    @PostMapping("/message")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleMessage(
            @RequestBody Map<String, String> request,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String message = request.get("message");
            Long userId = (Long) session.getAttribute("userId");
            
            if (message == null || message.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Please enter a message");
                return ResponseEntity.badRequest().body(response);
            }
            
            logger.info("Chat message from user {}: {}", userId, message);
            
            // Get resident ID from session (you can enhance this later to link users to residents)
            Long residentId = null;
            
            // Get AI response
            String aiResponse = aiAssistantService.getAIResponse(message, residentId);
            
            response.put("success", true);
            response.put("response", aiResponse);
            response.put("timestamp", System.currentTimeMillis());
            
            logger.info("AI response: {}", aiResponse);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error handling chat message: {}", e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "Sorry, I encountered an error. Please try again.");
            
            return ResponseEntity.status(500).body(response);
        }
    }
}
