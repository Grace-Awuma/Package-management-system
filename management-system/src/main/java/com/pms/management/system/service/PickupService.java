package com.pms.management.system.service;

import com.pms.management.system.dao.ParcelPackageDAO;
import com.pms.management.system.dao.PickupDAO;
import com.pms.management.system.model.ParcelPackage;
import com.pms.management.system.model.ParcelPackage.PackageStatus;
import com.pms.management.system.model.Pickup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PickupService {
    
    private static final Logger logger = LoggerFactory.getLogger(PickupService.class);
    
    @Autowired
    private PickupDAO pickupDAO;
    
    @Autowired
    private ParcelPackageDAO packageDAO;
    
    /**
     * Records a package pickup
     * 
     * @param packageId The ID of the package being picked up
     * @param pickup The pickup details
     * @return The recorded pickup
     * @throws IllegalArgumentException if package not found or already picked up
     */
    public Pickup recordPickup(Long packageId, Pickup pickup) {
        logger.info("Recording pickup for package ID: {}", packageId);
        
        ParcelPackage pkg = packageDAO.getById(packageId);
        if (pkg == null) {
            throw new IllegalArgumentException("Package not found with ID: " + packageId);
        }
        
        if (pkg.getStatus() == PackageStatus.PICKED_UP) {
            throw new IllegalStateException("Package has already been picked up");
        }
        
        // Set pickup date if not already set
        if (pickup.getPickupDate() == null) {
            pickup.setPickupDate(LocalDateTime.now());
        }
        
        // If recipient name is not provided, use resident's name
        if (pickup.getRecipientName() == null || pickup.getRecipientName().isEmpty()) {
            pickup.setRecipientName(pkg.getResident().getFullName());
        }
        
        // Associate pickup with package
        pickup.setPackagePickedUp(pkg);
        
        // Set the resident from the package
        pickup.setResident(pkg.getResident());
        
        // Mark package as picked up
        pkg.setStatus(PackageStatus.PICKED_UP);
        
        // ADDED: Set the bidirectional relationship
        pkg.setPickup(pickup);
        
        // Save pickup and update package
        pickupDAO.save(pickup);
        packageDAO.update(pkg);
        
        logger.info("Package {} picked up by recipient {} for resident {}", 
            pkg.getTrackingNumber(), pickup.getRecipientName(), pkg.getResident().getFullName());
        
        return pickup;
    }
    
    /**
     * Gets all pickups
     * 
     * @return List of all pickups
     */
    public List<Pickup> getAllPickups() {
        return pickupDAO.getAll();
    }
    
    /**
     * Gets a pickup by ID
     * 
     * @param id The ID of the pickup
     * @return The pickup
     */
    public Pickup getPickupById(Long id) {
        return pickupDAO.getById(id);
    }
    
    /**
     * Gets a pickup by package ID
     * 
     * @param packageId The ID of the package
     * @return The pickup for the package
     */
    public Pickup getPickupByPackageId(Long packageId) {
        return pickupDAO.getByPackageId(packageId);
    }
    
    /**
     * Deletes a pickup
     * 
     * @param id The ID of the pickup to delete
     */
    public void deletePickup(Long id) {
        Pickup pickup = pickupDAO.getById(id);
        if (pickup != null) {
            pickupDAO.delete(pickup);
        }
    }
}