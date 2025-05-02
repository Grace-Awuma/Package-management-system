package com.pms.management.system.dao.Impl;

import com.pms.management.system.dao.ParcelPackageDAO;
import com.pms.management.system.model.ParcelPackage;
import com.pms.management.system.model.ParcelPackage.PackageStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@Repository
public class ParcelPackageDAOImpl implements ParcelPackageDAO {

    private static final Logger logger = LoggerFactory.getLogger(ParcelPackageDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(ParcelPackage pkg) {
        logger.debug("Saving package: {}", pkg.getTrackingNumber());
        getCurrentSession().persist(pkg);
    }

    @Override
    public ParcelPackage getById(Long id) {
        logger.debug("Getting package by ID: {}", id);
        return getCurrentSession().get(ParcelPackage.class, id);
    }

    @Override
    public List<ParcelPackage> getAll() {
        logger.debug("Getting all packages");
        try {
            return getCurrentSession()
                .createSelectionQuery("FROM ParcelPackage p", ParcelPackage.class)
                .getResultList();
        } catch (Exception e) {
            logger.error("Error getting all packages: {}", e.getMessage(), e);
            return List.of(); // Return empty list instead of null
        }
    }
    @Override
    public List<ParcelPackage> getByResidentId(Long residentId) {
        logger.debug("Getting packages by resident ID: {}", residentId);
        return getCurrentSession()
            .createSelectionQuery(
                "FROM ParcelPackage p WHERE p.resident.id = :residentId", 
                ParcelPackage.class)
            .setParameter("residentId", residentId)
            .getResultList();
    }

    @Override
    public List<ParcelPackage> getByStatus(PackageStatus status) {
        logger.debug("Getting packages by status: {}", status);
        return getCurrentSession()
            .createSelectionQuery(
                "FROM ParcelPackage p WHERE p.status = :status", 
                ParcelPackage.class)
            .setParameter("status", status)
            .getResultList();
    }

    @Override
    public void update(ParcelPackage pkg) {
        logger.debug("Updating package: {}", pkg.getTrackingNumber());
        getCurrentSession().merge(pkg);
    }

    @Override
    public void remove(ParcelPackage pkg) {
        logger.debug("Removing package: {}", pkg.getTrackingNumber());
        getCurrentSession().remove(pkg);
    }
    
    @Override
    public boolean exists(Long id) {
        logger.debug("Checking if package exists with ID: {}", id);
        Long count = getCurrentSession()
            .createSelectionQuery("SELECT COUNT(p) FROM ParcelPackage p WHERE p.id = :id", Long.class)
            .setParameter("id", id)
            .getSingleResult();
        return count != null && count > 0;
    }
    
    @Override
    public void deleteById(Long id) {
        logger.debug("Deleting package by ID: {}", id);
        getCurrentSession()
            .createMutationQuery("DELETE FROM ParcelPackage p WHERE p.id = :id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public void saveOrUpdate(ParcelPackage pkg) {
        logger.debug("Saving or updating package: {}", pkg.getTrackingNumber());
        if (pkg.getId() == null) {
            save(pkg);
        } else {
            update(pkg);
        }
    }

    @Override
    public List<ParcelPackage> getByCarrier(String carrier) {
        logger.debug("Getting packages by carrier: {}", carrier);
        return getCurrentSession()
            .createSelectionQuery(
                "FROM ParcelPackage p WHERE p.carrier = :carrier", 
                ParcelPackage.class)
            .setParameter("carrier", carrier)
            .getResultList();
    }

    @Override
    public long count() {
        logger.debug("Counting all packages");
        return getCurrentSession()
            .createSelectionQuery("SELECT COUNT(p) FROM ParcelPackage p", Long.class)
            .getSingleResult();
    }

    @Override
    public List<ParcelPackage> getByDateRange(Date startDate, Date endDate) {
        logger.debug("Getting packages by date range: {} to {}", startDate, endDate);
        return getCurrentSession()
            .createSelectionQuery(
                "FROM ParcelPackage p WHERE p.arrivalDate BETWEEN :startDate AND :endDate", 
                ParcelPackage.class)
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .getResultList();
    }

    @Override
    public long countByStatus(PackageStatus status) {
        logger.debug("Counting packages by status: {}", status);
        return getCurrentSession()
            .createSelectionQuery(
                "SELECT COUNT(p) FROM ParcelPackage p WHERE p.status = :status", 
                Long.class)
            .setParameter("status", status)
            .getSingleResult();
    }

    @Override
    public void updateStatus(Long id, PackageStatus status) {
        logger.debug("Updating status for package ID: {} to {}", id, status);
        getCurrentSession()
            .createMutationQuery(
                "UPDATE ParcelPackage p SET p.status = :status WHERE p.id = :id")
            .setParameter("status", status)
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public void updateStatusForResident(Long residentId, PackageStatus status) {
        logger.debug("Updating status for all packages of resident ID: {} to {}", residentId, status);
        getCurrentSession()
            .createMutationQuery(
                "UPDATE ParcelPackage p SET p.status = :status WHERE p.resident.id = :residentId")
            .setParameter("status", status)
            .setParameter("residentId", residentId)
            .executeUpdate();
    }

    @Override
    public ParcelPackage getByTrackingNumber(String trackingNumber) {
        logger.debug("Getting package by tracking number: {}", trackingNumber);
        try {
            return getCurrentSession()
                .createSelectionQuery(
                    "FROM ParcelPackage p WHERE p.trackingNumber = :trackingNumber", 
                    ParcelPackage.class)
                .setParameter("trackingNumber", trackingNumber)
                .getSingleResult();
        } catch (Exception e) {
            logger.debug("No package found with tracking number: {}", trackingNumber);
            return null;
        }
    }
}
