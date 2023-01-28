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

    Building getBasicBuildingById(long id) throws EntityNotFoundException;

    boolean updateBuildingInfo(Building building);

    boolean removeBuilding(Building building);

    Room getRoomById(long id) throws EntityNotFoundException;

    Room addRoom(Room room) throws NoEntityCreatedException;

    boolean updateRoom(Room room);

    boolean removeRoom(Room room);

    List<Room> getRoomsInBuilding(Building building);

    boolean removeAllRoomsFromBuilding(Building building);

    boolean assignDeptToBuilding(Department dept, Building building);

    boolean removeDeptFromBuilding(Department dept, Building building);

    List<Department> getDeptsInBuilding(Building building);

}