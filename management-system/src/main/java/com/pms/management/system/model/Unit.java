package com.pms.management.system.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "units")
public class Unit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "unit_name", nullable = false)
    private String unitName;
    
    @Column(name = "unit_number", nullable = false)
    private String unitNumber;
    
    @Column(name = "description")
    private String description;
    
    // Add this field to satisfy the database constraint
    @Column(name = "building_id", nullable = false)
    private Long buildingId = 1L; // Default value
    
    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();
    
    // Default constructor
    public Unit() {
        this.buildingId = 1L; // Set default value
    }
    
    // Constructor with fields
    public Unit(String unitName, String unitNumber, String description) {
        this.unitName = unitName;
        this.unitNumber = unitNumber;
        this.description = description;
        this.buildingId = 1L; // Set default value
    }
    
    // Add room to unit
    public void addRoom(Room room) {
        rooms.add(room);
        room.setUnit(this);
    }
    
    // Remove room from unit
    public void removeRoom(Room room) {
        rooms.remove(room);
        room.setUnit(null);
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUnitName() {
        return unitName;
    }
    
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    
    public String getUnitNumber() {
        return unitNumber;
    }
    
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getBuildingId() {
        return buildingId;
    }
    
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }
    
    public List<Room> getRooms() {
        return rooms;
    }
    
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    
    @Override
    public String toString() {
        return "Unit [id=" + id + ", unitName=" + unitName + ", unitNumber=" + unitNumber + 
               ", description=" + description + "]";
    }
}