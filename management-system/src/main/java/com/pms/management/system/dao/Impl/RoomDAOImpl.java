package com.pms.management.system.dao.Impl;

import com.pms.management.system.dao.RoomDAO;
import com.pms.management.system.model.Room;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDAOImpl implements RoomDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(RoomDAOImpl.class);
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public List<Room> getAllRooms() {
        return getCurrentSession()
            .createQuery("FROM Room", Room.class)
            .getResultList();
    }
    
    @Override
    public Room getRoomById(Long id) {
        return getCurrentSession().get(Room.class, id);
    }
    
    @Override
    public Room getRoomByRoomNumber(String roomNumber) {
        try {
            return getCurrentSession()
                .createQuery("FROM Room WHERE roomNumber = :roomNumber", Room.class)
                .setParameter("roomNumber", roomNumber)
                .getSingleResult();
        } catch (Exception e) {
            logger.debug("No room found with number: {}", roomNumber);
            return null;
        }
    }
    
    @Override
    public void saveRoom(Room room) {
        try {
            if (room.getId() == null) {
                getCurrentSession().persist(room);
                logger.info("Room saved successfully");
            } else {
                getCurrentSession().merge(room);
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
            getCurrentSession().remove(room);
            logger.info("Room deleted successfully");
        }
    }
    
    @Override
    public boolean roomExists(String roomNumber) {
        Long count = getCurrentSession()
            .createQuery("SELECT COUNT(r) FROM Room r WHERE r.roomNumber = :roomNumber", Long.class)
            .setParameter("roomNumber", roomNumber)
            .getSingleResult();
        return count > 0;
    }
    
    @Override
    public Long getRoomCount() {
        return getCurrentSession()
            .createQuery("SELECT COUNT(r) FROM Room r", Long.class)
            .getSingleResult();
    }
}