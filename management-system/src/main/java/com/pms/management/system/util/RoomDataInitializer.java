package com.pms.management.system.util;

import com.pms.management.system.dao.RoomDAO;
import com.pms.management.system.model.Room;
import com.pms.management.system.model.Unit;
import com.pms.management.system.service.RoomService;
import com.pms.management.system.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RoomDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RoomDataInitializer.class);

    @Autowired
    private UnitService unitService;

    @Autowired
    private RoomService roomService;
    
    @Autowired
    private RoomDAO roomDAO; // Add this field

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeData();
    }

    private void initializeData() {
        try {
            // First initialize units and get the default unit
            Unit defaultUnit = unitService.initializeUnits();
            if (defaultUnit == null) {
                logger.error("Failed to initialize or retrieve default unit");
                return;
            }

            logger.info("Using unit: {} (ID: {})", defaultUnit.getUnitName(), defaultUnit.getId());

            // Then initialize rooms
            Long roomCount = roomService.getRoomCount();
            logger.info("Current room count: {}", roomCount);

            if (roomCount == 0) {
                logger.info("No rooms found. Initializing rooms...");

                // Create rooms from 101 to 200
                for (int i = 101; i <= 200; i++) {
                    Room room = new Room();
                    room.setRoomNumber(String.valueOf(i));
                    room.setUnit(defaultUnit);
                    // Remove the setStatus call or replace with a method that exists
                    // For example, if there's a "type" or "description" field:
                    // room.setDescription("Available");

                    roomDAO.saveRoom(room);
                    logger.info("Created room: {}", room.getRoomNumber());
                }

                logger.info("Room initialization complete.");
            } else {
                logger.info("Rooms already exist in the database. Skipping initialization.");
            }
        } catch (Exception e) {
            logger.error("Error initializing data: {}", e.getMessage(), e);
        }
    }
}