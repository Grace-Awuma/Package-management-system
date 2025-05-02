package com.pms.management.system.dao.Impl;

import com.pms.management.system.dao.RoomDAO;
import com.pms.management.system.model.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDAOImpl implements RoomDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(RoomDAOImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<Room> getAllRooms() {
        return entityManager.createQuery("from Room", Room.class).getResultList();
    }
    
    @Override
    public Room getRoomById(Long id) {
        return entityManager.find(Room.class, id);
    }
    
    @Override
    public Room getRoomByRoomNumber(String roomNumber) {
        TypedQuery<Room> query = entityManager.createQuery(
            "from Room where roomNumber = :roomNumber", Room.class);
        query.setParameter("roomNumber", roomNumber);
        List<Room> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    @Override
    public void saveRoom(Room room) {
        try {
            if (room.getId() == null) {
                entityManager.persist(room);
                logger.info("Room saved successfully");
            } else {
                entityManager.merge(room);
                logger.info("Room updated successfully");
            }
        } catch (Exception e) {
            logger.error("Error saving room: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        if (room != null) {
            entityManager.remove(room);
            logger.info("Room deleted successfully");
        }
    }
    
    @Override
    public boolean roomExists(String roomNumber) {
        TypedQuery<Long> query = entityManager.createQuery(
            "select count(r) from Room r where r.roomNumber = :roomNumber", Long.class);
        query.setParameter("roomNumber", roomNumber);
        return query.getSingleResult() > 0;
    }
    
    @Override
    public Long getRoomCount() {
        Query query = entityManager.createQuery("SELECT COUNT(r) FROM Room r");
        return (Long) query.getSingleResult();
    }
}