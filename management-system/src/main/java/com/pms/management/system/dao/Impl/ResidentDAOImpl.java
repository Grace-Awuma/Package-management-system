package com.pms.management.system.dao.Impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pms.management.system.dao.ResidentDAO;
import com.pms.management.system.model.Resident;

@Repository
public class ResidentDAOImpl implements ResidentDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(ResidentDAOImpl.class);
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public List<Resident> getAll() {
        try {
            Query<Resident> query = getCurrentSession().createQuery("from Resident", Resident.class);
            List<Resident> residents = query.getResultList();
            logger.info("Retrieved {} residents from database", residents.size());
            return residents;
        } catch (Exception e) {
            logger.error("Error retrieving all residents: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public Resident getById(Long id) {
        try {
            Resident resident = getCurrentSession().get(Resident.class, id);
            if (resident != null) {
                logger.info("Retrieved resident with id: {}, name: {} {}", 
                    id, resident.getFirstName(), resident.getLastName());
            } else {
                logger.warn("No resident found with id: {}", id);
            }
            return resident;
        } catch (Exception e) {
            logger.error("Error retrieving resident by id {}: {}", id, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public void save(Resident resident) {
        try {
            getCurrentSession().persist(resident);
            logger.info("Resident saved successfully: {} {}", 
                resident.getFirstName(), resident.getLastName());
        } catch (Exception e) {
            logger.error("Error saving resident: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public void update(Resident resident) {
        try {
            getCurrentSession().merge(resident);
            logger.info("Resident updated successfully: id={}, name={} {}", 
                resident.getId(), resident.getFirstName(), resident.getLastName());
        } catch (Exception e) {
            logger.error("Error updating resident: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public void delete(Resident resident) {
        try {
            getCurrentSession().remove(resident);
            logger.info("Resident deleted successfully: id={}, name={} {}", 
                resident.getId(), resident.getFirstName(), resident.getLastName());
        } catch (Exception e) {
            logger.error("Error deleting resident: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public List<Resident> search(String keyword) {
        try {
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            Query<Resident> query = getCurrentSession().createQuery(
                "from Resident where lower(firstName) like :pattern " +
                "or lower(lastName) like :pattern " +
                "or lower(email) like :pattern " +
                "or lower(phone) like :pattern " +
                "or lower(unitNumber) like :pattern", 
                Resident.class);
            query.setParameter("pattern", searchPattern);
            List<Resident> results = query.getResultList();
            logger.info("Search for '{}' returned {} results", keyword, results.size());
            return results;
        } catch (Exception e) {
            logger.error("Error searching residents with keyword {}: {}", keyword, e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Resident> searchResidents(String searchTerm) {
        logger.info("Searching for residents with term: {}", searchTerm);
        try {
            Session session = getCurrentSession();
            
            // Create a case-insensitive search with wildcards
            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            
            // Build the HQL query
            StringBuilder hql = new StringBuilder();
            hql.append("FROM Resident r WHERE ");
            hql.append("LOWER(r.firstName) LIKE :searchPattern OR ");
            hql.append("LOWER(r.lastName) LIKE :searchPattern OR ");
            hql.append("LOWER(CONCAT(r.firstName, ' ', r.lastName)) LIKE :searchPattern OR ");
            hql.append("LOWER(r.email) LIKE :searchPattern OR ");
            hql.append("LOWER(r.phone) LIKE :searchPattern");
            
            // Add unit search
            if (searchTerm.matches("\\d+")) {
                hql.append(" OR CAST(r.unit AS string) = :unitNumber");
            }
            
            // Add room search if the room relationship exists
            hql.append(" OR (r.room IS NOT NULL AND LOWER(r.room.roomNumber) LIKE :searchPattern)");
            
            // Create the query
            Query<Resident> query = session.createQuery(hql.toString(), Resident.class);
            query.setParameter("searchPattern", searchPattern);
            
            // Set unit number parameter if it's a number
            if (searchTerm.matches("\\d+")) {
                query.setParameter("unitNumber", searchTerm);
            }
            
            // Execute the query
            List<Resident> results = query.getResultList();
            logger.info("Found {} residents matching search term '{}'", results.size(), searchTerm);
            return results;
        } catch (Exception e) {
            logger.error("Error searching for residents: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Resident> getByRoomId(Long roomId) {
        try {
            Query<Resident> query = getCurrentSession().createQuery(
                "from Resident where room.id = :roomId", Resident.class);
            query.setParameter("roomId", roomId);
            List<Resident> residents = query.getResultList();
            logger.info("Retrieved {} residents for room id: {}", residents.size(), roomId);
            return residents;
        } catch (Exception e) {
            logger.error("Error retrieving residents by room id {}: {}", roomId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean exists(Long id) {
        try {
            Query<Long> query = getCurrentSession().createQuery(
                "select count(*) from Resident where id = :id", Long.class);
            query.setParameter("id", id);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Error checking if resident exists with id {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        try {
            Query<Long> query = getCurrentSession().createQuery(
                "select count(*) from Resident where email = :email", Long.class);
            query.setParameter("email", email);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Error checking if resident exists with email {}: {}", email, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public Long count() {
        try {
            Query<Long> query = getCurrentSession().createQuery(
                "select count(*) from Resident", Long.class);
            Long count = query.getSingleResult();
            logger.debug("Resident count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting residents: {}", e.getMessage(), e);
            return 0L;
        }
    }
}