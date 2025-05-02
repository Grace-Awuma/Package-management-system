package com.pms.management.system.controller;

import com.pms.management.system.model.Pickup;
import com.pms.management.system.service.PickupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/pickups")
public class PickupController {
    
    private static final Logger logger = LoggerFactory.getLogger(PickupController.class);
    
    @Autowired
    private PickupService pickupService;
    
    @GetMapping
    public String getAllPickups(Model model) {
        try {
            List<Pickup> pickups = pickupService.getAllPickups();
            model.addAttribute("pickups", pickups);
            return "pickups/list";
        } catch (Exception e) {
            logger.error("Error fetching pickups: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error fetching pickups: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/{id}")
    public String getPickupDetails(@PathVariable("id") Long id, Model model) {
        try {
            Pickup pickup = pickupService.getPickupById(id);
            if (pickup == null) {
                model.addAttribute("errorMessage", "Pickup not found with ID: " + id);
                return "error";
            }
            
            model.addAttribute("pickup", pickup);
            return "pickups/details";
        } catch (Exception e) {
            logger.error("Error fetching pickup details: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error fetching pickup details: " + e.getMessage());
            return "error";
        }
    }
}
