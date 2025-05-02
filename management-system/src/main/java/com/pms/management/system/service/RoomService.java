package com.pms.management.system.service;

import com.pms.management.system.dao.RoomDAO;
import com.pms.management.system.model.Room;
import com.pms.management.system.model.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomService {
    
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    
    @Autowired
    private RoomDAO roomDAO;
    
    @Autowired
    private UnitService unitService;
    
    // NO constructor - use field injection only
    
    @Transactional
    public void initializeRooms() {
        try {
            Long roomCount = getRoomCount();
            logger.info("Current room count: {}", roomCount);
            
            if (roomCount == 0) {
                logger.info("No rooms found. Initializing rooms...");
                
                // Get or create a default unit
                Unit defaultUnit = unitService.initializeUnits();
                
                if (defaultUnit == null) {
                    logger.error("Failed to initialize default unit. Cannot create rooms.");
                    return;
                }
                
                logger.info("Using unit with ID: {} for room initialization", defaultUnit.getId());
                
                // Create rooms 101-200
                for (int i = 101; i <= 200; i++) {
                    Room room = new Room();
                    room.setRoomNumber(String.valueOf(i));
                    room.setUnit(defaultUnit);
                    // Removed the setStatus call as requested
                    
                    roomDAO.saveRoom(room);
                    logger.info("Created room: {}", room.getRoomNumber());
                }
                
                logger.info("Room initialization complete. Created 100 rooms.");
            } else {
                logger.info("Rooms already exist in the database. Skipping initialization.");
            }
        } catch (Exception e) {
            logger.error("Error initializing rooms: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }
    
    @Transactional(readOnly = true)
    public Room getRoomById(Long id) {
        return roomDAO.getRoomById(id);
    }
    
    @Transactional(readOnly = true)
    public Long getRoomCount() {
        return roomDAO.getRoomCount();
    }
    
    @Transactional
    public void saveRoom(Room room) {
        roomDAO.saveRoom(room);
    }
    
    @Transactional
    public void deleteRoom(Long id) {
        roomDAO.deleteRoom(id);
    }
}