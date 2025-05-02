package com.pms.management.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "residents")
public class Resident {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "unit_number")
    private String unitNumber;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @Column(name = "move_in_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date moveInDate;
    
    @Column(name = "lease_end_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date leaseEndDate;
    
    @Column(name = "notes")
    private String notes;
    
 // Update the relationship to include cascade delete
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ParcelPackage> packages = new HashSet<>();
    
    
    // Default constructor
    public Resident() {
    }
    
    // Getters and setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Get the full name of the resident
     * @return The full name (first name + last name)
     */
    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getUnitNumber() {
        return unitNumber;
    }
    
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
    
    /**
     * Get the room number from the associated room
     * @return The room number or null if no room is assigned
     */
    @Transient
    public String getRoomNumber() {
        return room != null ? room.getRoomNumber() : null;
    }
    
    /**
     * Check if the resident has a valid room assigned
     * @return true if a room is assigned, false otherwise
     */
    @Transient
    public boolean hasValidRoom() {
        return room != null && room.getId() != null;
    }
    
    public Date getMoveInDate() {
        return moveInDate;
    }
    
    public void setMoveInDate(Date moveInDate) {
        this.moveInDate = moveInDate;
    }
    
    public Date getLeaseEndDate() {
        return leaseEndDate;
    }
    
    public void setLeaseEndDate(Date leaseEndDate) {
        this.leaseEndDate = leaseEndDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "Resident [id=" + id + 
               ", name=" + getFullName() + 
               ", email=" + email + 
               ", unitNumber=" + unitNumber + 
               ", room=" + (room != null ? room.getRoomNumber() : "none") + 
               "]";
    }
}