package com.pms.management.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "packages") // Changed from "parcel_packages" to match existing database table
public class ParcelPackage {
    
    // Updated PackageStatus enum with new values
    public enum PackageStatus {
        RECEIVED("Received"), 
        STORED("Stored"), 
        NOTIFIED("Notified"), 
        PICKED_UP("Picked Up");
        
        private final String displayName;
        
        PackageStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tracking_number")
    private String trackingNumber;
    
    @Column(name = "carrier")
    private String carrier;
    
    @Column(name = "delivery_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PackageStatus status = PackageStatus.RECEIVED;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident_id")
    private Resident resident;

    // Added missing fields
    @Column(name = "package_type")
    private String packageType;
    
    @Column(name = "size")
    private String size;
    
    @Column(name = "priority")
    private String priority;
    
    @Column(name = "condition_state") 
    private String condition;
    
    @Column(name = "storage_location")
    private String storageLocation;
    
    @Column(name = "notification_sent")
    private boolean notificationSent = false;
    
    @Column(name = "notification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate;
    
    // ADDED: Bidirectional relationship with Pickup
    @OneToOne(mappedBy = "packagePickedUp", fetch = FetchType.LAZY)
    private Pickup pickup;
    
    // Default constructor
    public ParcelPackage() {
        this.deliveryDate = new Date(); // Set default delivery date to current date/time
    }
    
    // Getters and setters for existing fields
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTrackingNumber() {
        return trackingNumber;
    }
    
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    
    public String getCarrier() {
        return carrier;
    }
    
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    
    public Date getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    /**
     * Get the formatted delivery date
     * @return The delivery date formatted as yyyy-MM-dd HH:mm
     */
    @Transient
    public String getFormattedDeliveryDate() {
        if (deliveryDate == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(deliveryDate);
    }
    
    /**
     * Get arrival date (alias for deliveryDate to match JSP)
     */
    @Transient
    public Date getArrivalDate() {
        return deliveryDate;
    }
    
    public PackageStatus getStatus() {
        return status;
    }
    
    public void setStatus(PackageStatus status) {
        this.status = status;
    }
    
    /**
     * Check if the package has been picked up
     * @return true if the status is PICKED_UP, false otherwise
     */
    @Transient
    public boolean isPickedUp() {
        return status == PackageStatus.PICKED_UP;
    }
    
    /**
     * Check if the package is pending pickup
     * @return true if the status is RECEIVED or STORED or NOTIFIED, false otherwise
     */
    @Transient
    public boolean isPending() {
        return status == PackageStatus.RECEIVED || 
               status == PackageStatus.STORED || 
               status == PackageStatus.NOTIFIED;
    }
    
    public Resident getResident() {
        return resident;
    }
    
    public void setResident(Resident resident) {
        this.resident = resident;
    }
    
    // ADDED: Getter and setter for pickup
    public Pickup getPickup() {
        return pickup;
    }
    
    public void setPickup(Pickup pickup) {
        this.pickup = pickup;
    }
    
    // Getters and setters for new fields
    public String getPackageType() {
        return packageType;
    }
    
    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public String getStorageLocation() {
        return storageLocation;
    }
    
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
    
    public boolean isNotificationSent() {
        return notificationSent;
    }
    
    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }
    
    public Date getNotificationDate() {
        return notificationDate;
    }
    
    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }
    
    /**
     * Mark package as notified
     */
    public void markAsNotified() {
        this.notificationSent = true;
        this.notificationDate = new Date();
        this.status = PackageStatus.NOTIFIED;
    }
    
    /**
     * Mark package as picked up
     */
    public void markAsPickedUp() {
        this.status = PackageStatus.PICKED_UP;
    }
    
    /**
     * Get the resident's full name
     * @return The resident's full name or "Unknown" if no resident is assigned
     */
    @Transient
    public String getResidentName() {
        if (resident == null) {
            return "Unknown";
        }
        return resident.getFirstName() + " " + resident.getLastName();
    }
    
    /**
     * Check if the package has a valid resident assigned
     * @return true if a resident is assigned, false otherwise
     */
    @Transient
    public boolean hasValidResident() {
        return resident != null && resident.getId() != null;
    }
    
    @Override
    public String toString() {
        return "ParcelPackage [id=" + id + 
               ", trackingNumber=" + trackingNumber + 
               ", carrier=" + carrier + 
               ", status=" + status + 
               ", packageType=" + packageType +
               ", size=" + size +
               ", priority=" + priority +
               ", condition=" + condition +
               ", storageLocation=" + storageLocation +
               ", resident=" + (resident != null ? resident.getFirstName() + " " + resident.getLastName() : "none") +
               "]";
    }
}