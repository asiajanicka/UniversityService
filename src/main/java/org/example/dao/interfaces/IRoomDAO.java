package org.example.dao.interfaces;

import org.example.model.Room;

import java.util.List;

public interface IRoomDAO extends IBaseDAO<Room> {

    int bindRoomToBuildingId(long roomId, long buildingId);

    List<Room> getRoomsByBuildingId(long teacherId);

    int removeRoomsByBuildingId(long buildingId);

}