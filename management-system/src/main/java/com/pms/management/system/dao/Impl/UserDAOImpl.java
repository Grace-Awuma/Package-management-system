package com.pms.management.system.dao.Impl;

import com.pms.management.system.dao.UserDAO;
import com.pms.management.system.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;
    
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(User user) {
        logger.debug("Saving user: {}", user.getUsername());
        getCurrentSession().persist(user);
    }

    @Override
    public User getById(Long id) {
        logger.debug("Getting user by ID: {}", id);
        return getCurrentSession().get(User.class, id);
    }
    
    @Override
    public User getByUsername(String username) {
        logger.debug("Getting user by username: {}", username);
        Query<User> query = getCurrentSession()
            .createQuery("FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }

    @Override
    public List<User> getAll() {
        logger.debug("Getting all users");
        // Using Criteria API for type safety
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        
        return session.createQuery(cq).getResultList();
    }

    @Override
    public void update(User user) {
        logger.debug("Updating user: {}", user.getUsername());
        getCurrentSession().merge(user);
    }

    @Override
    public void delete(User user) {
        logger.debug("Deleting user: {}", user.getUsername());
        getCurrentSession().remove(user);
    }
    
    @Override
    public boolean exists(Long id) {
        logger.debug("Checking if user exists with ID: {}", id);
        Long count = getCurrentSession()
            .createQuery("SELECT COUNT(u) FROM User u WHERE u.id = :id", Long.class)
            .setParameter("id", id)
            .uniqueResult();
        return count != null && count > 0;
    }
    
    @Override
    public void deleteById(Long id) {
        logger.debug("Deleting user by ID: {}", id);
        // Hibernate 6 approach for delete operations
        getCurrentSession()
            .createMutationQuery("DELETE FROM User u WHERE u.id = :id")
            .setParameter("id", id)
            .executeUpdate();
    }
}
