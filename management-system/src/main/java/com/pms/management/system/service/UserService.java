package com.pms.management.system.service;

import com.pms.management.system.dao.UserDAO;
import com.pms.management.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserDAO userDAO;
    
    /**
     * Authenticates a user with the given username and password
     */
    @Transactional(readOnly = true)
    public User authenticate(String username, String password) {
        User user = userDAO.getByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            // In a real application, you would use a password encoder
            // and compare the encoded passwords
            
            // Update last login time
            user.setLastLogin(new Date());
            
            // This update needs its own transaction
            updateUserLastLogin(user);
            
            return user;
        }
        
        return null;
    }
    
    /**
     * Update user's last login time in a separate transaction
     */
    @Transactional
    private void updateUserLastLogin(User user) {
        userDAO.update(user);
    }
    
    /**
     * Registers a new user
     */
    @Transactional
    public User register(User user) {
        User existingUser = userDAO.getByUsername(user.getUsername());
        
        if (existingUser != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Set creation date if not already set
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Date());
        }
        
        // In a real application, you would encode the password before saving
        userDAO.save(user);
        
        return user;
    }
    
    /**
     * Gets a user by ID
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userDAO.getById(userId);
    }
    
    /**
     * Gets all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }
    
    /**
     * Updates a user
     */
    @Transactional
    public User updateUser(User user) {
        if (!userDAO.exists(user.getId())) {
            throw new IllegalArgumentException("User not found with ID: " + user.getId());
        }
        
        userDAO.update(user);
        
        return user;
    }
    
    /**
     * Deletes a user
     */
    @Transactional
    public void deleteUser(Long userId) {
        if (!userDAO.exists(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        userDAO.deleteById(userId);
    }
    
    /**
     * Checks if a username is available
     */
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return userDAO.getByUsername(username) == null;
    }
}
