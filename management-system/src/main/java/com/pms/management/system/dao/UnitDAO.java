package com.pms.management.system.dao;

import com.pms.management.system.model.Unit;
import java.util.List;

public interface UnitDAO {
    List<Unit> getAllUnits();
    Unit getUnitById(Long id);
    void saveUnit(Unit unit);
    void deleteUnit(Long id);
    Long getUnitCount();
}