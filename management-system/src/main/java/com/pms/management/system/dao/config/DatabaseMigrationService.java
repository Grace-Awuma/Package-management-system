package com.pms.management.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseMigrationService implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMigrationService.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting database migration for residents table");
        
        try {
            // PART 1: Original migration for unit_number and room_number
            boolean originalMigrationNeeded = !columnExists("residents", "unit_number");
            
            if (originalMigrationNeeded) {
                logger.info("Original migration needed - adding new columns and migrating data");
                
                // Add new columns
                logger.info("Adding unit_number and room_number columns");
                jdbcTemplate.execute("ALTER TABLE residents ADD COLUMN IF NOT EXISTS unit_number VARCHAR(50)");
                jdbcTemplate.execute("ALTER TABLE residents ADD COLUMN IF NOT EXISTS room_number VARCHAR(50)");
                
                // Copy data
                logger.info("Copying data from relationships to new columns");
                jdbcTemplate.execute("UPDATE residents r " +
                                   "SET unit_number = u.unit_number, room_number = rm.room_number " +
                                   "FROM rooms rm JOIN units u ON rm.unit_id = u.id " +
                                   "WHERE r.room_id = rm.id");
                
                // Make columns not nullable
                logger.info("Making new columns not nullable");
                jdbcTemplate.execute("ALTER TABLE residents ALTER COLUMN unit_number SET NOT NULL");
                jdbcTemplate.execute("ALTER TABLE residents ALTER COLUMN room_number SET NOT NULL");
                
                // Find and drop foreign key constraint
                logger.info("Finding foreign key constraint name");
                String constraintQuery = 
                    "SELECT tc.constraint_name FROM information_schema.table_constraints tc " +
                    "JOIN information_schema.constraint_column_usage ccu ON tc.constraint_name = ccu.constraint_name " +
                    "WHERE tc.table_name = 'residents' AND ccu.column_name = 'room_id' " +
                    "AND tc.constraint_type = 'FOREIGN KEY'";
                
                String constraintName = null;
                try {
                    constraintName = jdbcTemplate.queryForObject(constraintQuery, String.class);
                } catch (Exception e) {
                    logger.warn("Could not find constraint name: {}", e.getMessage());
                }
                
                if (constraintName != null) {
                    logger.info("Dropping foreign key constraint: {}", constraintName);
                    jdbcTemplate.execute("ALTER TABLE residents DROP CONSTRAINT " + constraintName);
                } else {
                    logger.warn("No foreign key constraint found for room_id");
                }
                
                // Drop column
                logger.info("Dropping room_id column");
                jdbcTemplate.execute("ALTER TABLE residents DROP COLUMN IF EXISTS room_id");
                
                logger.info("Original migration completed successfully");
            } else {
                logger.info("Original migration already applied - skipping");
            }
            
            // PART 2: Room entity migration
            logger.info("Starting Room entity migration");
            
            // Step 1: Check if rooms table exists, create if not
            if (!tableExists("rooms")) {
                logger.info("Creating rooms table");
                jdbcTemplate.execute(
                    "CREATE TABLE rooms (" +
                    "id SERIAL PRIMARY KEY, " +
                    "room_number VARCHAR(50) NOT NULL UNIQUE" +
                    ")"
                );
            }
            
            // Step 2: Check if room_id column exists in residents table
            boolean roomIdExists = columnExists("residents", "room_id");
            
            if (!roomIdExists) {
                logger.info("Adding room_id column to residents table");
                jdbcTemplate.execute("ALTER TABLE residents ADD COLUMN room_id BIGINT");
                
                // Step 3: Populate rooms table with unique room numbers from residents
                logger.info("Populating rooms table with unique room numbers");
                jdbcTemplate.execute(
                    "INSERT INTO rooms (room_number) " +
                    "SELECT DISTINCT room_number FROM residents " +
                    "WHERE room_number IS NOT NULL " +
                    "ON CONFLICT (room_number) DO NOTHING"
                );
                
                // Step 4: Update residents table to set room_id based on room_number
                logger.info("Updating residents with room_id references");
                jdbcTemplate.execute(
                    "UPDATE residents r " +
                    "SET room_id = rm.id " +
                    "FROM rooms rm " +
                    "WHERE r.room_number = rm.room_number"
                );
                
                // Step 5: Add foreign key constraint
                logger.info("Adding foreign key constraint");
                jdbcTemplate.execute(
                    "ALTER TABLE residents " +
                    "ADD CONSTRAINT fk_resident_room " +
                    "FOREIGN KEY (room_id) REFERENCES rooms(id)"
                );
                
                // Step 6: Make room_id not nullable
                logger.info("Making room_id not nullable");
                jdbcTemplate.execute("ALTER TABLE residents ALTER COLUMN room_id SET NOT NULL");
                
                logger.info("Room entity migration completed successfully");
            } else {
                logger.info("Room entity migration already applied - skipping");
            }
            
        } catch (Exception e) {
            logger.error("Error during migration: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    private boolean columnExists(String tableName, String columnName) {
        String query = 
            "SELECT COUNT(*) FROM information_schema.columns " +
            "WHERE table_name = ? AND column_name = ?";
        
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, tableName, columnName);
        return count != null && count > 0;
    }
    
    private boolean tableExists(String tableName) {
        String query = 
            "SELECT COUNT(*) FROM information_schema.tables " +
            "WHERE table_name = ? AND table_schema = 'public'";
        
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, tableName);
        return count != null && count > 0;
    }
}