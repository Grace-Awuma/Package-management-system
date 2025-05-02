package com.pms.management.system.controller;

import com.pms.management.system.model.ParcelPackage;
import com.pms.management.system.model.ParcelPackage.PackageStatus;
import com.pms.management.system.model.Pickup;
import com.pms.management.system.model.Resident;
import com.pms.management.system.service.PackageService;
import com.pms.management.system.service.PickupService;
import com.pms.management.system.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/packages")
public class PackageController {

    private static final Logger logger = LoggerFactory.getLogger(PackageController.class);

    @Autowired
    private PackageService packageService;

    @Autowired
    private ResidentService residentService;

    @Autowired
    private PickupService pickupService;

    /**
     * Get all packages
     */
    @GetMapping
    public String getAllPackages(Model model) {
        try {
            logger.info("Fetching all packages");
            List<ParcelPackage> packages = packageService.getAllPackages();
            model.addAttribute("parcelPackages", packages);
            model.addAttribute("carriers", getCarrierOptions());
            return "packages/list";
        } catch (Exception e) {
            logger.error("Error fetching packages: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error fetching packages: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Filter packages by status and carrier
     */
    @GetMapping("/filter")
    public String filterPackages(@RequestParam(required = false) String status,
                                 @RequestParam(required = false) String carrier,
                                 Model model) {
        try {
            List<ParcelPackage> packages = packageService.getAllPackages();

            if (status != null && !status.isEmpty()) {
                PackageStatus packageStatus = PackageStatus.valueOf(status);
                packages = packageService.getPackagesByStatus(packageStatus);
                model.addAttribute("selectedStatus", status);
            }

            if (carrier != null && !carrier.isEmpty()) {
                packages = packages.stream()
                        .filter(pkg -> carrier.equals(pkg.getCarrier()))
                        .collect(Collectors.toList());
                model.addAttribute("selectedCarrier", carrier);
            }

            model.addAttribute("parcelPackages", packages);
            model.addAttribute("carriers", getCarrierOptions());
            return "packages/list";
        } catch (Exception e) {
            logger.error("Error filtering packages: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error filtering packages: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Show form to log a new package
     */
    @GetMapping("/new")
    public String showNewPackageForm(Model model) {
        try {
            logger.info("Showing new package form");

            model.addAttribute("parcelPackage", new ParcelPackage());

            List<Resident> residents = residentService.getAllResidents();
            model.addAttribute("residents", residents);

            // ✅ FIX: Add carriers to the form model
            model.addAttribute("carriers", getCarrierOptions());

            return "packages/form";
        } catch (Exception e) {
            logger.error("Error showing new package form: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error showing new package form: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Process new package submission
     */
    @PostMapping
    public String createPackage(@ModelAttribute ParcelPackage parcelPackage,
                                @RequestParam Long residentId,
                                RedirectAttributes redirectAttributes) {
        try {
            logger.info("Creating new package for resident ID: {}", residentId);
            packageService.logPackageArrival(parcelPackage, residentId);
            redirectAttributes.addFlashAttribute("successMessage", "Package logged successfully");
            return "redirect:/packages";
        } catch (Exception e) {
            logger.error("Error creating package: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating package: " + e.getMessage());
            return "redirect:/packages/new";
        }
    }

    /**
     * Show details of a single package
     */
    @GetMapping("/{id}")
    public String getPackageDetails(@PathVariable("id") Long id, Model model) {
        try {
            logger.info("Fetching package details for ID: {}", id);
            ParcelPackage parcelPackage = packageService.getPackageById(id);

            if (parcelPackage == null) {
                model.addAttribute("errorMessage", "Package not found with ID: " + id);
                return "error";
            }

            model.addAttribute("parcelPackage", parcelPackage);
            return "packages/details";
        } catch (Exception e) {
            logger.error("Error fetching package details: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error fetching package details: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Handle pickup logic
     */
    @GetMapping("/{id}/pickup")
    public String pickupPackage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Processing quick pickup for package ID: {}", id);
            ParcelPackage parcelPackage = packageService.getPackageById(id);

            if (parcelPackage == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Package not found.");
                return "redirect:/packages";
            }

            if (parcelPackage.getStatus() == PackageStatus.PICKED_UP) {
                redirectAttributes.addFlashAttribute("errorMessage", "This package has already been picked up.");
                return "redirect:/packages";
            }

            Pickup pickup = new Pickup();
            pickup.setRecipientName(parcelPackage.getResident().getFullName());
            pickup.setSignatureConfirmation(false);
            pickup.setNotes("Quick pickup processed");

            pickupService.recordPickup(id, pickup);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Package #" + id + " has been marked as picked up by " + parcelPackage.getResident().getFullName());

            // Just redirect back to the packages page instead of the picked-up page
            return "redirect:/packages";
        } catch (Exception e) {
            logger.error("Error processing quick pickup: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing pickup: " + e.getMessage());
            return "redirect:/packages";
        }
    }

    /**
     * View all picked up packages
     */
    @GetMapping("/picked-up")
    public String getPickedUpPackages(Model model) {
        try {
            logger.info("Fetching picked up packages");
            List<ParcelPackage> pickedUpPackages = packageService.getPackagesByStatus(PackageStatus.PICKED_UP);
            
            // Create a date formatter
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            // Initialize the pickup relationship for each package
            for (ParcelPackage pkg : pickedUpPackages) {
                Pickup pickup = pickupService.getPickupByPackageId(pkg.getId());
                if (pickup != null) {
                    pkg.setPickup(pickup);
                    logger.debug("Set pickup for package {}: {}", pkg.getId(), pickup.getId());
                    
                    // Format pickup date
                    if (pickup.getPickupDate() != null) {
                        String formattedDate = dateFormat.format(pickup.getPickupDate());
                        model.addAttribute("formattedPickupDate_" + pkg.getId(), formattedDate);
                    }
                } else {
                    logger.warn("No pickup found for picked-up package ID: {}", pkg.getId());
                }
                
                // Format arrival date
                if (pkg.getArrivalDate() != null) {
                    String formattedDate = dateFormat.format(pkg.getArrivalDate());
                    model.addAttribute("formattedArrivalDate_" + pkg.getId(), formattedDate);
                }
            }
            
            model.addAttribute("parcelPackages", pickedUpPackages);
            return "packages/picked-up";
        } catch (Exception e) {
            logger.error("Error fetching picked up packages: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error fetching picked up packages: " + e.getMessage());
            return "error";
        }
    }

    // ✅ Utility method for reusable carrier options
    private List<String> getCarrierOptions() {
        return List.of("USPS", "UPS", "FedEx", "DHL", "Amazon", "Other");
    }
}