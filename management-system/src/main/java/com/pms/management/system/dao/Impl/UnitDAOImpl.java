package com.pms.management.system.dao.Impl;

import com.pms.management.system.dao.UnitDAO;
import com.pms.management.system.model.Unit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UnitDAOImpl implements UnitDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(UnitDAOImpl.class);
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public List<Unit> getAllUnits() {
        return getCurrentSession()
            .createQuery("FROM Unit", Unit.class)
            .getResultList();
    }
    
    @Override
    public Unit getUnitById(Long id) {
        return getCurrentSession().get(Unit.class, id);
    }
    
    @Override
    public void saveUnit(Unit unit) {
        try {
            if (unit.getId() == null) {
                getCurrentSession().persist(unit);
                logger.info("Unit saved successfully with ID: {}", unit.getId());
            } else {
                getCurrentSession().merge(unit);
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
            getCurrentSession().remove(unit);
            logger.info("Unit deleted successfully");
        }
    }
    
    @Override
    public Long getUnitCount() {
        return getCurrentSession()
            .createQuery("SELECT COUNT(u) FROM Unit u", Long.class)
            .getSingleResult();
    }
}