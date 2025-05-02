package com.pms.management.system.dao.Impl;

import com.pms.management.system.dao.PickupDAO;
import com.pms.management.system.model.Pickup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Repository
public class PickupDAOImpl implements PickupDAO {

    private static final Logger logger = LoggerFactory.getLogger(PickupDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Pickup pickup) {
        logger.debug("Saving pickup: {}", pickup.getId());
        getCurrentSession().persist(pickup);
    }

    @Override
    public Pickup getById(Long id) {
        logger.debug("Getting pickup by ID: {}", id);
        return getCurrentSession().get(Pickup.class, id);
    }

    @Override
    public List<Pickup> getAll() {
        logger.debug("Getting all pickups");
        return getCurrentSession()
            .createSelectionQuery("FROM Pickup", Pickup.class)
            .getResultList();
    }

    @Override
    public Pickup getByPackageId(Long packageId) {
        logger.debug("Getting pickup by package ID: {}", packageId);
        try {
            return getCurrentSession()
                .createSelectionQuery(
                    "FROM Pickup p WHERE p.packagePickedUp.id = :packageId", 
                    Pickup.class)
                .setParameter("packageId", packageId)
                .getSingleResult();
        } catch (Exception e) {
            logger.debug("No pickup found for package ID: {}", packageId);
            return null;
        }
    }

    @Override
    public void update(Pickup pickup) {
        logger.debug("Updating pickup: {}", pickup.getId());
        getCurrentSession().merge(pickup);
    }

    @Override
    public void delete(Pickup pickup) {
        logger.debug("Deleting pickup: {}", pickup.getId());
        getCurrentSession().remove(pickup);
    }
    
    @Override
    public boolean exists(Long id) {
        logger.debug("Checking if pickup exists with ID: {}", id);
        Long count = getCurrentSession()
            .createSelectionQuery("SELECT COUNT(p) FROM Pickup p WHERE p.id = :id", Long.class)
            .setParameter("id", id)
            .getSingleResult();
        return count != null && count > 0;
    }
    
    @Override
    public void deleteById(Long id) {
        logger.debug("Deleting pickup by ID: {}", id);
        getCurrentSession()
            .createMutationQuery("DELETE FROM Pickup p WHERE p.id = :id")
            .setParameter("id", id)
            .executeUpdate();
    }
}
