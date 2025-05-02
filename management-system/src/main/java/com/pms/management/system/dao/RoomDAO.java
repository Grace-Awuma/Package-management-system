package com.pms.management.system.dao;

import com.pms.management.system.model.Room;
import java.util.List;

public interface RoomDAO {
    List<Room> getAllRooms();
    Room getRoomById(Long id);
    Room getRoomByRoomNumber(String roomNumber);
    void saveRoom(Room room);
    void deleteRoom(Long id);
    boolean roomExists(String roomNumber);
    Long getRoomCount(); // Added this method
}