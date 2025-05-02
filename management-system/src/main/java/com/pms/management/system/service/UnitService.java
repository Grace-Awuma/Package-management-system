package com.pms.management.system.service;

import com.pms.management.system.dao.UnitDAO;
import com.pms.management.system.model.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UnitService {
    
    private static final Logger logger = LoggerFactory.getLogger(UnitService.class);
    
    @Autowired
    private UnitDAO unitDAO;
    
    // NO constructor - use field injection only
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Unit initializeUnits() {
        try {
            Long unitCount = unitDAO.getUnitCount();
            logger.info("Current unit count: {}", unitCount);
            
            if (unitCount == 0) {
                logger.info("No units found. Initializing default unit...");
                
                // Create a default unit with a unit number
                Unit defaultUnit = new Unit("Default Unit", "U-001", "Default unit for all rooms");
                // buildingId is already set to 1 by default in the constructor
                
                unitDAO.saveUnit(defaultUnit);
                
                logger.info("Unit initialization complete. Created unit with ID: {}", defaultUnit.getId());
                return defaultUnit;
            } else {
                logger.info("Units already exist in the database. Retrieving first unit.");
                List<Unit> units = getAllUnits();
                if (!units.isEmpty()) {
                    return units.get(0);
                }
            }
        } catch (Exception e) {
            logger.error("Error initializing units: {}", e.getMessage(), e);
            throw e;
        }
        return null;
    }
    
    @Transactional(readOnly = true)
    public List<Unit> getAllUnits() {
        return unitDAO.getAllUnits();
    }
    
    @Transactional(readOnly = true)
    public Unit getUnitById(Long id) {
        return unitDAO.getUnitById(id);
    }
    
    @Transactional
    public void saveUnit(Unit unit) {
        // Ensure unit_number is set if not already
        if (unit.getUnitNumber() == null || unit.getUnitNumber().isEmpty()) {
            unit.setUnitNumber("U-" + System.currentTimeMillis() % 1000);
        }
        
        // Ensure buildingId is set
        if (unit.getBuildingId() == null) {
            unit.setBuildingId(1L);
        }
        
        unitDAO.saveUnit(unit);
    }
    
    @Transactional
    public void deleteUnit(Long id) {
        unitDAO.deleteUnit(id);
    }
}