package com.pms.management.system.controller;

import com.pms.management.system.model.ParcelPackage;
import com.pms.management.system.model.Resident;
import com.pms.management.system.model.User;
import com.pms.management.system.service.PackageService;
import com.pms.management.system.service.ResidentService;
import com.pms.management.system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private PackageService packageService;

    @Autowired
    private ResidentService residentService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String dashboard(Model model, HttpSession session) {
        logger.info("Accessing dashboard");

        // Check if user is logged in
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            logger.warn("Unauthorized access attempt to dashboard");
            return "redirect:/auth/login";
        }

        try {
            // Add user to model if userService is available
            if (userService != null) {
                User user = userService.getUserById(userId);
                model.addAttribute("user", user);
            }

            // Add dashboard statistics
            if (packageService != null) {
                try {
                    List<ParcelPackage> allPackages = packageService.getAllPackages();
                    model.addAttribute("totalPackages", allPackages.size());
                    
                    // Log the packages to debug
                    logger.info("Retrieved {} packages from database", allPackages.size());
                    for (ParcelPackage pkg : allPackages) {
                        logger.debug("Package ID: {}, Status: {}", pkg.getId(), pkg.getStatus());
                    }

                    // Count packages by status
                    int pendingCount = 0;
                    int notifiedCount = 0;
                    List<ParcelPackage> recentPackages = new ArrayList<>();

                    for (ParcelPackage pkg : allPackages) {
                        // Assuming your ParcelPackage has an enum called PackageStatus
                        if (pkg.getStatus() != null) {
                            String status = pkg.getStatus().toString();
                            if ("RECEIVED".equals(status) || "STORED".equals(status)) {
                                pendingCount++;
                            } else if ("NOTIFIED".equals(status)) {
                                notifiedCount++;
                            }
                        }

                        // Get 5 most recent packages for display
                        if (recentPackages.size() < 5) {
                            recentPackages.add(pkg);
                        }
                    }

                    model.addAttribute("pendingPackages", pendingCount);
                    model.addAttribute("notifiedPackages", notifiedCount);
                    model.addAttribute("recentPackages", recentPackages);

                    logger.info("Dashboard statistics: total={}, pending={}, notified={}",
                            allPackages.size(), pendingCount, notifiedCount);
                } catch (Exception e) {
                    logger.error("Error fetching package statistics: {}", e.getMessage(), e);
                    // Set default values
                    model.addAttribute("totalPackages", 0);
                    model.addAttribute("pendingPackages", 0);
                    model.addAttribute("notifiedPackages", 0);
                    model.addAttribute("recentPackages", new ArrayList<>());
                }
            } else {
                logger.warn("PackageService is null");
                // Set default values if service is not available
                model.addAttribute("totalPackages", 0);
                model.addAttribute("pendingPackages", 0);
                model.addAttribute("notifiedPackages", 0);
                model.addAttribute("recentPackages", new ArrayList<>());
            }

            if (residentService != null) {
                try {
                    List<Resident> residents = residentService.getAllResidents();
                    model.addAttribute("totalResidents", residents.size());
                    
                    // Log the residents to debug
                    logger.info("Retrieved {} residents from database", residents.size());
                    for (Resident resident : residents) {
                        logger.debug("Resident ID: {}", resident.getId());
                    }
                } catch (Exception e) {
                    logger.error("Error fetching resident statistics: {}", e.getMessage(), e);
                    model.addAttribute("totalResidents", 0);
                }
            } else {
                logger.warn("ResidentService is null");
                model.addAttribute("totalResidents", 0);
            }

            return "dashboard";
        } catch (Exception e) {
            logger.error("Error rendering dashboard: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            return "error";
        }
    }
}