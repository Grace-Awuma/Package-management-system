package com.pms.management.system.dao;

import java.util.List;
import com.pms.management.system.model.Resident;

public interface ResidentDAO {
    List<Resident> getAll();
    Resident getById(Long id);
    void save(Resident resident);
    void update(Resident resident);
    void delete(Resident resident);
    List<Resident> search(String keyword);
    List<Resident> searchResidents(String searchTerm);
    List<Resident> getByRoomId(Long roomId);
    boolean exists(Long id);
    boolean existsByEmail(String email);
    Long count();
}