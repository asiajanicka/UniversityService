package org.example.service;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.BuildingDAO;
import org.example.dao.DeptDAO;
import org.example.dao.RoomDAO;
import org.example.dao.interfaces.IBuildingDAO;
import org.example.dao.interfaces.IDeptDAO;
import org.example.dao.interfaces.IRoomDAO;
import org.example.enums.EntityType;
import org.example.model.Building;
import org.example.model.Department;
import org.example.model.Room;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class BuildingService {

    private final IBuildingDAO buildingDAO = new BuildingDAO();
    private final IDeptDAO deptDAO = new DeptDAO();
    private final IRoomDAO roomDAO = new RoomDAO();
    private final DepartmentService departmentService = new DepartmentService();
    private static final Logger logger = LogManager.getLogger(BuildingService.class);

    public Building getBuildingById(long id) throws EntityNotFoundException {
        Building tempBuilding = getBasicBuildingById(id);
        tempBuilding.setDepartments(getDeptsInBuilding(tempBuilding));
        return tempBuilding;
    }

    public Building addNewBuilding(String name, String address) throws NoEntityCreatedException {
        Building buildingToAdd = new Building(name, address);
        Building tempBuilding = buildingDAO
                .createEntity(buildingToAdd)
                .orElseThrow(() -> new NoEntityCreatedException(EntityType.BUILDING, buildingToAdd));
        logger.debug(String.format("Building %s added to the service", buildingToAdd));
        return tempBuilding;
    }

    public Building getBasicBuildingById(long id) throws EntityNotFoundException {
        Building tempBuilding = buildingDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.BUILDING, id));
        logger.debug(String.format("Building (id: %d) retrieved from service", id));
        return tempBuilding;
    }

    public boolean updateBuildingInfo(Building building) {
        if (building != null) {
            int result = buildingDAO.updateEntity(building);
            if (result == 1) {
                logger.debug(String.format("Building (%s) updated in the service", building));
                return true;
            } else {
                logger.error(String.format("Building (%s) couldn't be updated in the service", building));
                return false;
            }
        } else {
            logger.error("Building couldn't be updated in the service as it is NULL");
            return false;
        }
    }

    public boolean removeBuilding(Building building) {
        if (building != null) {
            int result = buildingDAO.removeEntity(building.getId());
            if (result == 1) {
                logger.debug(String.format("Building (%s) removed from the service", building));
                return true;
            } else {
                logger.error(String.format("Building (%s) couldn't be removed from the service", building));
                return false;
            }
        } else {
            logger.error("Building couldn't be removed from the service as it is NULL");
            return false;
        }
    }

    public Room getRoomById(long id) throws EntityNotFoundException {
        Room tempRoom = roomDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.STUDENT, id));
        tempRoom.setBuilding(getBuildingById(tempRoom.getId()));
        logger.debug(String.format("Room (id: %d) retrieved from service", id));
        return tempRoom;
    }

    public Room addRoom(Room room) throws NoEntityCreatedException {
        if (room != null && room.getBuilding() != null && room.getBuilding().getId() > 0) {
            Room tempRoom = roomDAO
                    .createEntity(room)
                    .orElseThrow(() -> new NoEntityCreatedException(EntityType.ROOM, room));
            logger.debug(String.format("Room %s added to the service", tempRoom));
            tempRoom.setBuilding(room.getBuilding());
            return tempRoom;
        } else {
            logger.error("Room couldn't be added to the service as room or building in which it's supposed to be located" +
                    " is NULL");
            throw new NullPointerException("Room or building is NULL - can't add room to the service");
        }
    }

    public boolean updateRoom(Room room) {
        if (room != null) {
            int result = roomDAO.updateEntity(room);
            if (result == 1) {
                logger.debug(String.format("Room (%s) updated in the service", room));
                return true;
            } else {
                logger.error(String.format("Room (%s) couldn't be updated in the service", room));
                return false;
            }
        } else {
            logger.error("Room couldn't be updated in the service as it is NULL");
            return false;
        }
    }

    public List<Room> getRoomsInBuilding(Building building) {
        List<Room> allRoomsByBuildingId = new ArrayList<>();
        if (building != null) {
            allRoomsByBuildingId = roomDAO.getRoomsByBuildingId(building.getId());
            for (Room room : allRoomsByBuildingId) {
                room.setBuilding(building);
            }
            logger.debug(String.format("Rooms in building (%s) retrieved from service", building));
        } else {
            logger.error("Rooms from building couldn't be retrieved from the service as building is NULL");
        }
        return allRoomsByBuildingId;
    }

    public boolean removeAllRoomsFromBuilding(Building building) {
        if (building != null) {
            return roomDAO.removeRoomsByBuildingId(building.getId()) > 0;
        } else {
            logger.error("Rooms couldn't be removed from building in the service as building is NULL");
            return false;
        }
    }

    public boolean assignDeptToBuilding(Department dept, Building building) {
        if (dept != null && building != null) {
            int result = deptDAO.bindDepartmentToBuildingId(dept.getId(), building.getId());
            if (result == 1) {
                logger.debug(String.format("Department (%s) assigned to building (%s) in the service", dept, building));
                return true;
            } else {
                logger.error(String.format("Department (%s) couldn't be assigned to building (%s) in the service", dept, building));
                return false;
            }
        } else {
            logger.error("Department couldn't be assigned to building in the service as one of them is NULL");
            return false;
        }
    }

    public boolean removeDeptFromBuilding(Department dept, Building building) {
        if (dept != null && building != null) {
            int result = deptDAO.removeDepartmentFromBuildingById(dept.getId(), dept.getId());
            if (result == 1) {
                logger.debug(String.format("Department (%s) removed from building (%s) in the service", dept, building));
                return true;
            } else {
                logger.error(String.format("Department (%s) couldn't be removed from building (%s) in the service", dept, building));
                return false;
            }
        } else {
            logger.error("Department couldn't be removed from building in the service as one of them is NULL");
            return false;
        }
    }

    public List<Department> getDeptsInBuilding(Building building) {
        List<Department> deptsByBuildingId = new ArrayList<>();
        if (building != null) {
            deptsByBuildingId.addAll(deptDAO.getDepartmentsByBuildingId(building.getId()));
            for (Department dept : deptsByBuildingId) {
                dept.setTeachers(departmentService.getTeachersByDeptId(dept.getId()));
            }
        } else {
            logger.error("Departments in building couldn't be retrieved in the service as building is NULL");
        }
        return deptsByBuildingId;
    }

}

