package com.pms.management.system.controller;

import com.pms.management.system.model.User;
import com.pms.management.system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    // Regex pattern for name validation - only letters, spaces, hyphens, and apostrophes
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]+$");
    
    @Autowired
    private UserService userService;
    
    // Create a simple class to hold login form data
    public static class LoginForm {
        private String username;
        private String password;
        private boolean remember;
        
        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public boolean isRemember() { return remember; }
        public void setRemember(boolean remember) { this.remember = remember; }
    }
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Add a new empty login form to the model
        model.addAttribute("loginForm", new LoginForm());
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        try {
            logger.info("Login attempt for username: {}", loginForm.getUsername());
            
            User user = userService.authenticate(loginForm.getUsername(), loginForm.getPassword());
            
            if (user != null) {
                // Store user in session
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                
                logger.info("User {} successfully logged in", loginForm.getUsername());
                return "redirect:/dashboard";
            } else {
                logger.warn("Failed login attempt for username: {}", loginForm.getUsername());
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
                return "redirect:/auth/login";
            }
        } catch (Exception e) {
            logger.error("Error during login process", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error during login: " + e.getMessage());
            return "redirect:/auth/login";
        }
    }
    
    // controller methods
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                          @RequestParam String confirmPassword,
                          RedirectAttributes redirectAttributes) {
        try {
            // Validate input
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Username is required");
                return "redirect:/auth/register";
            }
            
            // Validate password - must be at least 8 characters
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Password is required");
                return "redirect:/auth/register";
            }
            
            if (user.getPassword().length() < 8) {
                redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 8 characters long");
                return "redirect:/auth/register";
            }
            
            // Validate full name - must not contain numbers and use proper name pattern
            if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Full name is required");
                return "redirect:/auth/register";
            }
            
            // Check if name contains only valid characters (letters, spaces, hyphens, apostrophes)
            if (!NAME_PATTERN.matcher(user.getFullName()).matches()) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Full name must contain only letters, spaces, hyphens, and apostrophes");
                return "redirect:/auth/register";
            }
            
            // Validate password confirmation
            if (!user.getPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match");
                return "redirect:/auth/register";
            }
            
            // Check if username is available
            if (!userService.isUsernameAvailable(user.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Username already exists");
                return "redirect:/auth/register";
            }
            
            // Trim the full name to remove extra spaces
            user.setFullName(user.getFullName().trim().replaceAll("\\s+", " "));
            
            userService.register(user);
            
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error during registration: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
    
    @GetMapping("/check-username")
    @ResponseBody
    public String checkUsername(@RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        return available ? "available" : "taken";
    }
    
    // NEW ENDPOINT: Add this method for AJAX field validation
    @GetMapping("/validate-field")
    @ResponseBody
    public Map<String, Object> validateField(@RequestParam String field, @RequestParam String value) {
        Map<String, Object> response = new HashMap<>();
        
        logger.info("Validating field: {} with value: {}", field, value);
        
        if ("fullName".equals(field)) {
            if (value == null || value.trim().isEmpty()) {
                response.put("valid", false);
                response.put("message", "Full name is required");
            } else if (!NAME_PATTERN.matcher(value).matches()) {
                response.put("valid", false);
                response.put("message", "Full name must contain only letters, spaces, hyphens, and apostrophes");
            } else {
                response.put("valid", true);
            }
        } else if ("password".equals(field)) {
            if (value == null || value.isEmpty()) {
                response.put("valid", false);
                response.put("message", "Password is required");
            } else if (value.length() < 8) {
                response.put("valid", false);
                response.put("message", "Password must be at least 8 characters long");
            } else {
                response.put("valid", true);
            }
        } else if ("username".equals(field)) {
            if (value == null || value.trim().isEmpty()) {
                response.put("valid", false);
                response.put("message", "Username is required");
            } else {
                // Check if username is available
                boolean available = userService.isUsernameAvailable(value);
                response.put("valid", available);
                if (!available) {
                    response.put("message", "Username already exists");
                }
            }
        } else if ("confirmPassword".equals(field)) {
            // For confirm password, we need the original password
            String[] parts = value.split("::");
            if (parts.length != 2) {
                response.put("valid", false);
                response.put("message", "Invalid data format");
                return response;
            }
            
            String password = parts[0];
            String confirmPassword = parts[1];
            
            if (!password.equals(confirmPassword)) {
                response.put("valid", false);
                response.put("message", "Passwords do not match");
            } else {
                response.put("valid", true);
            }
        }
        
        logger.info("Validation result: {}", response);
        return response;
    }
}