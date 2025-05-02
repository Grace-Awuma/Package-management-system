package com.pms.management.system.dao;

import com.pms.management.system.model.ParcelPackage;
import com.pms.management.system.model.ParcelPackage.PackageStatus;
import java.util.Date;
import java.util.List;

public interface ParcelPackageDAO {
    // Create/Update operations
    void save(ParcelPackage pkg);  // Use this as the standard method to save new entities
    void update(ParcelPackage pkg); // Use this to update existing entities
    void saveOrUpdate(ParcelPackage pkg); // Combined save or update based on ID
    
    // Read operations
    ParcelPackage getById(Long id); // Standard method to get by ID using Long
    List<ParcelPackage> getAll();
    List<ParcelPackage> getByResidentId(Long residentId);
    List<ParcelPackage> getByStatus(PackageStatus status);
    List<ParcelPackage> getByCarrier(String carrier);
    List<ParcelPackage> getByDateRange(Date startDate, Date endDate);
    ParcelPackage getByTrackingNumber(String trackingNumber);
    boolean exists(Long id);
    
    // Count operations
    long count();
    long countByStatus(PackageStatus status);
    
    // Status update operations
    void updateStatus(Long id, PackageStatus status);
    void updateStatusForResident(Long residentId, PackageStatus status);
    
    // Delete operations
    void remove(ParcelPackage pkg);
    void deleteById(Long id);
}
