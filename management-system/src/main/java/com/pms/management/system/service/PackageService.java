package com.pms.management.system.service;

import com.pms.management.system.dao.ParcelPackageDAO;
import com.pms.management.system.dao.ResidentDAO;
import com.pms.management.system.model.ParcelPackage;
import com.pms.management.system.model.ParcelPackage.PackageStatus;
import com.pms.management.system.model.Resident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional
public class PackageService {
    
    private static final Logger logger = LoggerFactory.getLogger(PackageService.class);
    
    @Autowired
    private ParcelPackageDAO packageDAO;
    
    @Autowired
    private ResidentDAO residentDAO;
    
    /**
     * Logs a new package arrival
     * 
     * @param pkg The package to log
     * @param residentId The ID of the resident the package is for
     * @return The saved package
     */
    public ParcelPackage logPackageArrival(ParcelPackage pkg, Long residentId) {
        logger.info("Logging package arrival for resident ID: {}", residentId);
        
        Resident resident = residentDAO.getById(residentId);
        if (resident == null) {
            throw new IllegalArgumentException("Resident not found with ID: " + residentId);
        }
        
        pkg.setResident(resident);
        pkg.setStatus(PackageStatus.RECEIVED);
        packageDAO.save(pkg);
        
        return pkg;
    }
    
    /**
     * Gets all packages
     * 
     * @return List of all packages
     */
    public List<ParcelPackage> getAllPackages() {
        return packageDAO.getAll();
    }
    
    /**
     * Gets packages by status
     * 
     * @param status The status to filter by
     * @return List of packages with the specified status
     */
    public List<ParcelPackage> getPackagesByStatus(PackageStatus status) {
        return packageDAO.getByStatus(status);
    }
    
    /**
     * Gets packages for a specific resident
     * 
     * @param residentId The ID of the resident
     * @return List of packages for the resident
     */
    public List<ParcelPackage> getPackagesByResident(Long residentId) {
        return packageDAO.getByResidentId(residentId);
    }
    
    /**
     * Gets a package by ID
     * 
     * @param packageId The ID of the package
     * @return The package
     */
    public ParcelPackage getPackageById(Long packageId) {
        return packageDAO.getById(packageId);
    }
    
    /**
     * Deletes a package
     * 
     * @param packageId The ID of the package to delete
     */
    public void deletePackage(Long packageId) {
        ParcelPackage pkg = packageDAO.getById(packageId);
        if (pkg == null) {
            throw new IllegalArgumentException("Package not found with ID: " + packageId);
        }
        
        packageDAO.remove(pkg);
    }
    
    /**
     * Reassigns a package to a different resident
     * 
     * @param packageId The ID of the package
     * @param newResidentId The ID of the new resident
     * @return The updated package
     */
    public ParcelPackage reassignPackage(Long packageId, Long newResidentId) {
        ParcelPackage pkg = packageDAO.getById(packageId);
        if (pkg == null) {
            throw new IllegalArgumentException("Package not found with ID: " + packageId);
        }
        
        Resident newResident = residentDAO.getById(newResidentId);
        if (newResident == null) {
            throw new IllegalArgumentException("Resident not found with ID: " + newResidentId);
        }
        
        pkg.setResident(newResident);
        packageDAO.update(pkg);
        
        return pkg;
    }
}
