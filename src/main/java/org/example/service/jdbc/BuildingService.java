package org.example.service.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IBuildingDAO;
import org.example.dao.interfaces.IDeptDAO;
import org.example.dao.interfaces.IRoomDAO;
import org.example.dao.jdbc.BuildingDAO;
import org.example.dao.jdbc.DeptDAO;
import org.example.dao.jdbc.RoomDAO;
import org.example.enums.EntityType;
import org.example.model.Building;
import org.example.model.Department;
import org.example.model.Room;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IBuildingService;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class BuildingService implements IBuildingService {

    private final IBuildingDAO buildingDAO = new BuildingDAO();
    private final IDeptDAO deptDAO = new DeptDAO();
    private final IRoomDAO roomDAO = new RoomDAO();
    private final DepartmentService departmentService = new DepartmentService();
    private static final Logger logger = LogManager.getLogger(BuildingService.class);

    @Override
    public Building getBuildingById(long id) throws EntityNotFoundException {
        Building tempBuilding = getBasicBuildingById(id);
        tempBuilding.setDepartments(getDeptsInBuilding(tempBuilding.getId()));
        return tempBuilding;
    }

    @Override
    public Building addNewBuilding(String name, String address) throws NoEntityCreatedException {
        Building buildingToAdd = new Building(name, address);
        if (name != null && address != null) {
            buildingDAO.createEntity(buildingToAdd);
            logger.debug(String.format("Building %s added to the service", buildingToAdd));
            return buildingToAdd;
        } else {
            throw new NoEntityCreatedException(EntityType.BUILDING, buildingToAdd);
        }
    }

    private Building getBasicBuildingById(long id) throws EntityNotFoundException {
        Building tempBuilding = buildingDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.BUILDING, id));
        logger.debug(String.format("Building (id: %d) retrieved from service", id));
        return tempBuilding;
    }

    @Override
    public boolean updateBuildingInfo(Building building) {
        if (building != null && building.getId() > 0) {
            if (building.getName() != null && building.getAddress() != null) {
                int result = buildingDAO.updateEntity(building);
                if (result == 1) {
                    logger.debug(String.format("Building (%s) updated in the service", building));
                    return true;
                } else {
                    logger.error(String.format("Building (%s) couldn't be updated in the service", building));
                    return false;
                }
            } else {
                logger.error("Building couldn't be updated in the service as some of fields have either incorrect value or are null");
                return false;
            }
        } else {
            logger.error("Building couldn't be updated in the service as it is NULL or has invalid id");
            return false;
        }
    }

    @Override
    public boolean removeBuilding(long buildingId) {
        if (buildingId > 0) {
            int result = buildingDAO.removeEntity(buildingId);
            if (result == 1) {
                logger.debug(String.format("Building (%d) removed from the service", buildingId));
                return true;
            } else {
                logger.error(String.format("Building (%d) couldn't be removed from the service", buildingId));
                return false;
            }
        } else {
            logger.error("Building couldn't be removed from the service as its id is invalid");
            return false;
        }
    }

    @Override
    public Room getRoomById(long id) throws EntityNotFoundException {
        Room tempRoom = roomDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.STUDENT, id));
        tempRoom.setBuilding(getBuildingById(tempRoom.getBuilding().getId()));
        logger.debug(String.format("Room (id: %d) retrieved from service", id));
        return tempRoom;
    }

    @Override
    public Room addRoom(Room room) throws NoEntityCreatedException {
        if (room != null && room.getBuilding() != null && room.getBuilding().getId() > 0) {
            if (room.getNumber() != null) {
                roomDAO.createEntity(room);
                logger.debug(String.format("Room %s added to the service", room));
                room.setBuilding(room.getBuilding());
                return room;
            } else {
                throw new NoEntityCreatedException(EntityType.ROOM, room);
            }
        } else {
            logger.error("Room couldn't be added to the service as room or building in which it's supposed to be located" +
                    " is NULL");
            throw new NullPointerException("Room or building is NULL - can't add room to the service");
        }
    }

    @Override
    public boolean updateRoom(Room room) {
        if (room != null && room.getId() > 0) {
            if (room.getNumber() != null && room.getBuilding().getId() > 0) {
                int result = roomDAO.updateEntity(room);
                if (result == 1) {
                    logger.debug(String.format("Room (%s) updated in the service", room));
                    return true;
                } else {
                    logger.error(String.format("Room (%s) couldn't be updated in the service", room));
                    return false;
                }
            } else {
                logger.error("Room couldn't be updated in the service as its number is invalid or building id is incorrect");
                return false;
            }
        } else {
            logger.error("Room couldn't be updated in the service as it is NULL or has invalid id");
            return false;
        }
    }

    @Override
    public boolean removeRoom(long id) {
        if (id > 0) {
            int result = roomDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Room (%d) removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Room (%d) couldn't be removed from the service", id));
                return false;
            }
        } else {
            logger.error("Room couldn't be removed from the service as its id is invalid");
            return false;
        }
    }

    @Override
    public List<Room> getRoomsInBuilding(long buildingId) throws EntityNotFoundException {
        List<Room> allRoomsByBuildingId = new ArrayList<>();
        if (buildingId > 0) {
            allRoomsByBuildingId = roomDAO.getRoomsByBuildingId(buildingId);
            for (Room room : allRoomsByBuildingId) {
                room.setBuilding(getBuildingById(buildingId));
            }
            logger.debug(String.format("Rooms in building (%d) retrieved from service", buildingId));
        } else {
            logger.error("Rooms from building couldn't be retrieved from the service as its id is invalid");
        }
        return allRoomsByBuildingId;
    }

    @Override
    public boolean removeAllRoomsFromBuilding(long buildingId) {
        if (buildingId > 0) {
            return roomDAO.removeRoomsByBuildingId(buildingId) > 0;
        } else {
            logger.error("Rooms couldn't be removed from building in the service as its id is invalid");
            return false;
        }
    }

    @Override
    public boolean assignDeptToBuilding(long deptId, long buildingId) {
        if (deptId > 0 && buildingId > 0) {
            int result = deptDAO.bindDepartmentToBuildingId(deptId, buildingId);
            if (result == 1) {
                logger.debug(String.format("Department (%d) assigned to building (%d) in the service", deptId, buildingId));
                return true;
            } else {
                logger.error(String.format("Department (%d) couldn't be assigned to building (%d) in the service", deptId, buildingId));
                return false;
            }
        } else {
            logger.error("Department couldn't be assigned to building in the service as one of has invalid id");
            return false;
        }
    }

    @Override
    public boolean removeDeptFromBuilding(long deptId, long buildingId) {
        if (deptId > 0 && buildingId > 0) {
            int result = deptDAO.removeDepartmentFromBuildingById(deptId, buildingId);
            if (result == 1) {
                logger.debug(String.format("Department (%d) removed from building (%d) in the service", deptId, buildingId));
                return true;
            } else {
                logger.error(String.format("Department (%d) couldn't be removed from building (%d) in the service", deptId, buildingId));
                return false;
            }
        } else {
            logger.error("Department couldn't be removed from building in the service as one of them has invalid id");
            return false;
        }
    }

    @Override
    public List<Department> getDeptsInBuilding(long buildingId) {
        List<Department> deptsByBuildingId = new ArrayList<>();
        if (buildingId > 0) {
            deptsByBuildingId.addAll(deptDAO.getDepartmentsByBuildingId(buildingId));
            for (Department dept : deptsByBuildingId) {
                dept.setTeachers(departmentService.getTeachersByDeptId(dept.getId()));
            }
        } else {
            logger.error("Departments in building couldn't be retrieved in the service as its id is invalid");
        }
        return deptsByBuildingId;
    }

}