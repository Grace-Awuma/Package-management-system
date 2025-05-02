package com.pms.management.system.dao.Impl;

import com.pms.management.system.dao.UnitDAO;
import com.pms.management.system.model.Unit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UnitDAOImpl implements UnitDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(UnitDAOImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<Unit> getAllUnits() {
        return entityManager.createQuery("FROM Unit", Unit.class).getResultList();
    }
    
    @Override
    public Unit getUnitById(Long id) {
        return entityManager.find(Unit.class, id);
    }
    
    @Override
    public void saveUnit(Unit unit) {
        try {
            if (unit.getId() == null) {
                entityManager.persist(unit);
                // Force a flush to ensure the ID is generated
                entityManager.flush();
                logger.info("Unit saved successfully with ID: {}", unit.getId());
            } else {
                entityManager.merge(unit);
                logger.info("Unit updated successfully");
            }
        } catch (Exception e) {
            logger.error("Error saving unit: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public void deleteUnit(Long id) {
        Unit unit = getUnitById(id);
        if (unit != null) {
            entityManager.remove(unit);
            logger.info("Unit deleted successfully");
        }
    }
    
    @Override
    public Long getUnitCount() {
        Query query = entityManager.createQuery("SELECT COUNT(u) FROM Unit u");
        return (Long) query.getSingleResult();
    }
}