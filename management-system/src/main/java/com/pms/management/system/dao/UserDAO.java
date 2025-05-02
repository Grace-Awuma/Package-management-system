package com.pms.management.system.dao;

import com.pms.management.system.model.User;
import java.util.List;

public interface UserDAO {
    void save(User user);
    User getById(Long id);
    User getByUsername(String username);
    List<User> getAll();
    void update(User user);
    void delete(User user);
    boolean exists(Long id);
    void deleteById(Long id);
}
