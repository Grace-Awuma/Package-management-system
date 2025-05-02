package com.pms.management.system.service;

import com.pms.management.system.dao.ResidentDAO;
import com.pms.management.system.dao.ParcelPackageDAO;
import com.pms.management.system.model.Resident;
import com.pms.management.system.model.ParcelPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional
public class ResidentService {
    
    private static final Logger logger = LoggerFactory.getLogger(ResidentService.class);
    
    @Autowired
    private ResidentDAO residentDAO;
    
    @Autowired
    private ParcelPackageDAO parcelPackageDAO;
    
    /**
     * Saves a resident (creates new or updates existing)
     * 
     * @param resident The resident to save
     * @return The saved resident
     */
    public Resident saveResident(Resident resident) {
        logger.info("Saving resident: {}", resident);
        
        if (resident.getId() == null) {
            return createResident(resident);
        } else {
            return updateResident(resident);
        }
    }
    
    /**
     * Creates a new resident
     * 
     * @param resident The resident to create
     * @return The created resident
     */
    public Resident createResident(Resident resident) {
        logger.info("Creating resident: {} {}", resident.getFirstName(), resident.getLastName());
        
        // Validate input
        if (resident.getUnitNumber() == null || resident.getUnitNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Unit number is required");
        }
        
        // Room validation - updated to use Room entity
        if (resident.getRoom() == null || resident.getRoom().getId() == null) {
            throw new IllegalArgumentException("Room is required");
        }
        
        // Save the resident
        residentDAO.save(resident);
        logger.info("Resident created successfully: {}", resident.getId());
        
        return resident;
    }
    
    /**
     * Updates an existing resident
     * 
     * @param resident The resident to update
     * @return The updated resident
     */
    public Resident updateResident(Resident resident) {
        logger.info("Updating resident ID: {}", resident.getId());
        
        // Validate input
        if (resident.getUnitNumber() == null || resident.getUnitNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Unit number is required");
        }
        
        // Room validation - updated to use Room entity
        if (resident.getRoom() == null || resident.getRoom().getId() == null) {
            throw new IllegalArgumentException("Room is required");
        }
        
        // Check if resident exists
        Resident existingResident = residentDAO.getById(resident.getId());
        if (existingResident == null) {
            throw new IllegalArgumentException("Resident not found with ID: " + resident.getId());
        }
        
        // Update the resident
        residentDAO.update(resident);
        logger.info("Resident updated successfully: {}", resident.getId());
        
        return resident;
    }
    
    /**
     * Gets all residents
     * 
     * @return List of all residents
     */
    public List<Resident> getAllResidents() {
        return residentDAO.getAll();
    }
    
    /**
     * Gets a resident by ID
     * 
     * @param residentId The ID of the resident
     * @return The resident
     */
    public Resident getResidentById(Long residentId) {
        return residentDAO.getById(residentId);
    }
    
    /**
     * Deletes a resident
     * 
     * @param residentId The ID of the resident to delete
     */
    public void deleteResident(Long residentId) {
        logger.info("Attempting to delete resident with ID: {}", residentId);
        
        Resident resident = residentDAO.getById(residentId);
        if (resident == null) {
            throw new IllegalArgumentException("Resident not found with ID: " + residentId);
        }
        
        // Get associated packages for logging purposes
        List<ParcelPackage> packages = parcelPackageDAO.getByResidentId(residentId);
        if (packages != null && !packages.isEmpty()) {
            logger.info("Resident has {} associated packages that will be deleted", packages.size());
            
            // Delete all associated packages first to avoid foreign key constraint issues
            for (ParcelPackage pkg : packages) {
                logger.debug("Deleting package with ID: {}", pkg.getId());
                parcelPackageDAO.remove(pkg);
            }
        }
        
        // Now delete the resident
        residentDAO.delete(resident);
        logger.info("Resident with ID: {} deleted successfully", residentId);
    }
    
    /**
     * Checks if a resident has packages
     * 
     * @param residentId The ID of the resident
     * @return true if the resident has packages, false otherwise
     */
    public boolean hasPackages(Long residentId) {
        List<ParcelPackage> packages = parcelPackageDAO.getByResidentId(residentId);
        return packages != null && !packages.isEmpty();
    }
    
    /**
     * Gets the count of packages for a resident
     * 
     * @param residentId The ID of the resident
     * @return The number of packages
     */
    public int getPackageCount(Long residentId) {
        List<ParcelPackage> packages = parcelPackageDAO.getByResidentId(residentId);
        return packages != null ? packages.size() : 0;
    }
    
    /**
     * Searches for residents by name, email, phone, unit number, or room number
     * 
     * @param searchTerm The search term
     * @return List of matching residents
     */
    public List<Resident> searchResidents(String searchTerm) {
        logger.info("Searching for residents with term: {}", searchTerm);
        
        // Use the enhanced search method from the DAO
        return residentDAO.searchResidents(searchTerm);
    }
}