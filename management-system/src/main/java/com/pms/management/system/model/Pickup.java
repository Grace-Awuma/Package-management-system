package com.pms.management.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickups")
public class Pickup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "pickup_date", nullable = false)
    private LocalDateTime pickupDate;
    
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;
    
    @Column(name = "signature_confirmation")
    private boolean signatureConfirmation;
    
    @Column(name = "signature_data")
    private String signatureData;
    
    @Column
    private String notes;
    
    // UPDATED: Explicit foreign key reference to the packages table
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", referencedColumnName = "id", 
               foreignKey = @ForeignKey(name = "FK_PICKUP_PACKAGE", 
                                       foreignKeyDefinition = "FOREIGN KEY (package_id) REFERENCES packages(id)"),
               nullable = false, unique = true)
    private ParcelPackage packagePickedUp;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    // Constructors
    public Pickup() {
        this.pickupDate = LocalDateTime.now();
    }
    
    public Pickup(String recipientName, boolean signatureConfirmation, String signatureData, String notes) {
        this();
        this.recipientName = recipientName;
        this.signatureConfirmation = signatureConfirmation;
        this.signatureData = signatureData;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDateTime pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public boolean isSignatureConfirmation() {
        return signatureConfirmation;
    }

    public void setSignatureConfirmation(boolean signatureConfirmation) {
        this.signatureConfirmation = signatureConfirmation;
    }

    public String getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(String signatureData) {
        this.signatureData = signatureData;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ParcelPackage getPackagePickedUp() {
        return packagePickedUp;
    }

    public void setPackagePickedUp(ParcelPackage packagePickedUp) {
        this.packagePickedUp = packagePickedUp;
    }
    
    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }
}