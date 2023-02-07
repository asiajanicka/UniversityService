package org.example.dao.interfaces;

import org.apache.ibatis.annotations.Param;
import org.example.model.Room;

import java.util.List;

public interface IRoomDAO extends IBaseDAO<Room> {

    int bindRoomToBuildingId(@Param("roomId") long roomId, @Param("buildingId") long buildingId);

    List<Room> getRoomsByBuildingId(@Param("teacherId")long teacherId);

    int removeRoomsByBuildingId(@Param("buildingId") long buildingId);

}