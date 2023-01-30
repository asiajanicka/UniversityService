package org.example.service.interfaces;

import org.example.model.Building;
import org.example.model.Department;
import org.example.model.Room;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.List;

public interface IBuildingService {

    Building getBuildingById(long id) throws EntityNotFoundException;

    Building addNewBuilding(String name, String address) throws NoEntityCreatedException;

    boolean updateBuildingInfo(Building building) throws NoEntityCreatedException;

    boolean removeBuilding(long buildingId);

    Room getRoomById(long id) throws EntityNotFoundException;

    Room addRoom(Room room) throws NoEntityCreatedException;

    boolean updateRoom(Room room) throws NoEntityCreatedException;

    boolean removeRoom(long id);

    List<Room> getRoomsInBuilding(long buildingId) throws EntityNotFoundException;

    boolean removeAllRoomsFromBuilding(long buildingId);

    boolean assignDeptToBuilding(long deptId, long buildingId);

    boolean removeDeptFromBuilding(long deptId, long buildingId);

    List<Department> getDeptsInBuilding(long buildingId);

}