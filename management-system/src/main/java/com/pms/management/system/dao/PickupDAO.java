package com.pms.management.system.dao;

import com.pms.management.system.model.Pickup;
import java.util.List;

public interface PickupDAO {
    void save(Pickup pickup);
    Pickup getById(Long id);
    List<Pickup> getAll();
    Pickup getByPackageId(Long packageId);
    void update(Pickup pickup);
    void delete(Pickup pickup);
    boolean exists(Long id);
    void deleteById(Long id);
}
