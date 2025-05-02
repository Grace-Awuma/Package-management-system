package com.pms.management.system.service;

import com.pms.management.system.model.ParcelPackage;
import com.pms.management.system.model.Resident;
import com.pms.management.system.dao.ParcelPackageDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private ParcelPackageDAO parcelPackageDAO;
  
    /**
     * Sends an email notification to a resident about their package
     * 
     * @param resident The resident to notify
     * @param pkg The package that arrived
     * @return true if notification was sent successfully, false otherwise
     */
    @Transactional
    public boolean sendEmailNotification(Resident resident, ParcelPackage pkg) {
        // In a real application, this would connect to an email service
        // For now, we'll just simulate the notification
        
        if (resident == null || resident.getEmail() == null || resident.getEmail().isEmpty()) {
            logger.warn("Cannot send email notification: resident is null or has no email address");
            return false;
        }
        
        try {
            // Log the notification (in a real app, this would send an actual email)
            logger.info("Sending email notification to: {}", resident.getEmail());
            logger.info("Subject: Package Arrival Notification");
            logger.info("Body: Hello {}, you have a package from {} that arrived on {}. " +
                    "Please pick it up at your earliest convenience.", 
                    resident.getFullName(), 
                    pkg.getCarrier(), 
                    pkg.getFormattedDeliveryDate());
            
            // Update package status if needed
            // Update to NOTIFIED if it's RECEIVED or STORED
            if (pkg.getStatus() == ParcelPackage.PackageStatus.RECEIVED || 
                pkg.getStatus() == ParcelPackage.PackageStatus.STORED) {
                pkg.setStatus(ParcelPackage.PackageStatus.NOTIFIED);
                parcelPackageDAO.update(pkg);
                logger.info("Updated package status to NOTIFIED after email notification");
            }
            
            return true;
        } catch (Exception e) {
            logger.error("Error sending email notification: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Sends an SMS notification to a resident about their package
     * 
     * @param resident The resident to notify
     * @param pkg The package that arrived
     * @return true if notification was sent successfully, false otherwise
     */
    @Transactional
    public boolean sendSMSNotification(Resident resident, ParcelPackage pkg) {
        // In a real application, this would connect to an SMS service
        // For now, we'll just simulate the notification
        
        if (resident == null || resident.getPhone() == null || resident.getPhone().isEmpty()) {
            logger.warn("Cannot send SMS notification: resident is null or has no phone number");
            return false;
        }
        
        try {
            // Log the notification (in a real app, this would send an actual SMS)
            logger.info("Sending SMS notification to: {}", resident.getPhone());
            logger.info("Message: Hello {}, you have a package from {} that arrived today. " +
                    "Please pick it up at your earliest convenience.",
                    resident.getFirstName(),
                    pkg.getCarrier());
            
            // Update package status if needed
            // Update to NOTIFIED if it's RECEIVED or STORED
            if (pkg.getStatus() == ParcelPackage.PackageStatus.RECEIVED || 
                pkg.getStatus() == ParcelPackage.PackageStatus.STORED) {
                pkg.setStatus(ParcelPackage.PackageStatus.NOTIFIED);
                parcelPackageDAO.update(pkg);
                logger.info("Updated package status to NOTIFIED after SMS notification");
            }
            
            return true;
        } catch (Exception e) {
            logger.error("Error sending SMS notification: {}", e.getMessage(), e);
            return false;
        }
    }
}