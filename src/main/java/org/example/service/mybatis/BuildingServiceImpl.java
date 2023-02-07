package org.example.service.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IBuildingDAO;
import org.example.dao.interfaces.IDeptDAO;
import org.example.dao.interfaces.IRoomDAO;
import org.example.enums.EntityType;
import org.example.model.Building;
import org.example.model.Department;
import org.example.model.Room;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IBuildingService;
import org.example.utils.MyBatisDaoFactory;

import java.util.ArrayList;
import java.util.List;

public class BuildingServiceImpl implements IBuildingService {

    private static final SqlSessionFactory SESSION_FACTORY = MyBatisDaoFactory.getSqlSessionFactory();
    private static final Logger logger = LogManager.getLogger(BuildingServiceImpl.class);

    @Override
    public Building getBuildingById(long id) throws EntityNotFoundException {
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            Building tempBuilding;
            IBuildingDAO buildingDAO = sqlSession.getMapper(IBuildingDAO.class);
            tempBuilding = buildingDAO
                    .getEntityById(id)
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.BUILDING, id));
            logger.debug(String.format("Building (id: %d) retrieved from service", id));
            return tempBuilding;
        }
    }

    @Override
    public Building addNewBuilding(String name, String address) throws NoEntityCreatedException {
        Building buildingToAdd = new Building(name, address);
        if (name != null && address != null) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IBuildingDAO buildingDAO = sqlSession.getMapper(IBuildingDAO.class);
                try {
                    buildingDAO.createEntity(buildingToAdd);
                    sqlSession.commit();
                    logger.debug(String.format("Building (%s) added to the service", buildingToAdd));
                    return buildingToAdd;
                } catch (Exception e) {
                    sqlSession.rollback();
                    throw new NoEntityCreatedException(EntityType.BUILDING, buildingToAdd);
                }
            }
        } else {
            throw new NoEntityCreatedException(EntityType.BUILDING, buildingToAdd);
        }
    }

    @Override
    public boolean updateBuildingInfo(Building building) {
        if (building != null && building.getId() > 0) {
            if (building.getName() != null && building.getAddress() != null) {
                try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                    IBuildingDAO buildingDAO = sqlSession.getMapper(IBuildingDAO.class);
                    int result = 0;
                    try {
                        result = buildingDAO.updateEntity(building);
                        sqlSession.commit();
                    } catch (Exception e) {
                        sqlSession.rollback();
                        e.printStackTrace();
                        logger.error(e.getMessage(), e);
                    }
                    if (result == 1) {
                        logger.debug(String.format("Building (%s) updated in the service", building));
                        return true;
                    } else {
                        logger.error(String.format("Building (%s) couldn't be updated in the service", building));
                        return false;
                    }
                }
            } else {
                logger.error("Building couldn't be updated in the service as it is NULL or has invalid id");
                return false;
            }
        } else {
            logger.error("Building couldn't be updated in the service as it is NULL");
            return false;
        }
    }

    @Override
    public boolean removeBuilding(long buildingId) {
        if (buildingId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IBuildingDAO buildingDAO = sqlSession.getMapper(IBuildingDAO.class);
                int result = 0;
                try {
                    result = buildingDAO.removeEntity(buildingId);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Building (%d) removed from the service", buildingId));
                    return true;
                } else {
                    logger.error(String.format("Building (%d) couldn't be removed from the service", buildingId));
                    return false;
                }
            }
        } else {
            logger.error("Building couldn't be removed from the service as its id is invalid");
            return false;
        }
    }

    @Override
    public Room getRoomById(long id) throws EntityNotFoundException {
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            Room tempRoom;
            IRoomDAO roomDAO = sqlSession.getMapper(IRoomDAO.class);
            tempRoom = roomDAO
                    .getEntityById(id)
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.ROOM, id));
            logger.debug(String.format("Room (id: %d) retrieved from service", id));
            return tempRoom;
        }
    }

    @Override
    public Room addRoom(Room room) throws NoEntityCreatedException {
        if (room != null && room.getBuilding() != null && room.getBuilding().getId() > 0) {
            if (room.getNumber() != null) {
                try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                    IRoomDAO roomDAO = sqlSession.getMapper(IRoomDAO.class);
                    try {
                        roomDAO.createEntity(room);
                        sqlSession.commit();
                        logger.debug(String.format("Room (%s) added to the service", room));
                        return room;
                    } catch (Exception e) {
                        sqlSession.rollback();
                        e.printStackTrace();
                        logger.error(e.getMessage(), e);
                        throw new NoEntityCreatedException(EntityType.ROOM, room);
                    }
                }
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
        if (room != null) {
            if (room.getNumber() != null && room.getBuilding().getId() > 0) {
                try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                    IRoomDAO roomDAO = sqlSession.getMapper(IRoomDAO.class);
                    int result = 0;
                    try {
                        result = roomDAO.updateEntity(room);
                        sqlSession.commit();
                    } catch (Exception e) {
                        sqlSession.rollback();
                        e.printStackTrace();
                        logger.error(e.getMessage(), e);
                    }
                    if (result == 1) {
                        logger.debug(String.format("Room (%s) updated in the service", room));
                        return true;
                    } else {
                        logger.error(String.format("Room (%s) couldn't be updated in the service", room));
                        return false;
                    }
                }
            } else {
                logger.error("Room couldn't be updated in the service as its number is NULL or has invalid id");
                return false;
            }
        } else {
            logger.error("Room couldn't be updated in the service as it is NULL");
            return false;
        }
    }

    @Override
    public boolean removeRoom(long id) {
        if (id > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IRoomDAO roomDAO = sqlSession.getMapper(IRoomDAO.class);
                int result = 0;
                try {
                    result = roomDAO.removeEntity(id);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Room (%d) removed from the service", id));
                    return true;
                } else {
                    logger.error(String.format("Room (%d) couldn't be removed from the service", id));
                    return false;
                }
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
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IRoomDAO roomDAO = sqlSession.getMapper(IRoomDAO.class);
                allRoomsByBuildingId = roomDAO.getRoomsByBuildingId(buildingId);
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
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IRoomDAO roomDAO = sqlSession.getMapper(IRoomDAO.class);
                int result = 0;
                try {
                    result = roomDAO.removeRoomsByBuildingId(buildingId);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result > 1) {
                    logger.debug(String.format("Rooms removed from building (%d) in the service", buildingId));
                    return true;
                } else {
                    logger.error(String.format("Rooms couldn't be removed from the building (%d) in the service", buildingId));
                    return false;
                }
            }
        } else {
            logger.error("Rooms couldn't be removed from building in the service as its id is invalid");
            return false;
        }
    }

    @Override
    public boolean assignDeptToBuilding(long deptId, long buildingId) {
        if (deptId > 0 && buildingId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IDeptDAO deptDAO = sqlSession.getMapper(IDeptDAO.class);
                int result = 0;
                try {
                    result = deptDAO.bindDepartmentToBuildingId(deptId, buildingId);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Department (%d) assigned to building (%d) in the service", deptId, buildingId));
                    return true;
                } else {
                    logger.error(String.format("Department (%d) couldn't be assigned to building (%d) in the service", deptId, buildingId));
                    return false;
                }
            }
        } else {
            logger.error("Department couldn't be assigned to building in the service as one of them has invalid id");
            return false;
        }
    }

    @Override
    public boolean removeDeptFromBuilding(long deptId, long buildingId) {
        if (deptId > 0 && buildingId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IDeptDAO deptDAO = sqlSession.getMapper(IDeptDAO.class);
                int result = 0;
                try {
                    result = deptDAO.removeDepartmentFromBuildingById(deptId, buildingId);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Department (%d) removed from building (%d) in the service", deptId, buildingId));
                    return true;
                } else {
                    logger.error(String.format("Department (%d) couldn't be removed from building (%d) in the service", deptId, buildingId));
                    return false;
                }
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
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IDeptDAO deptDAO = sqlSession.getMapper(IDeptDAO.class);
                deptsByBuildingId.addAll(deptDAO.getDepartmentsByBuildingId(buildingId));
            }
        } else {
            logger.error("Departments in building couldn't be retrieved in the service as its id is invalid");
        }
        return deptsByBuildingId;
    }

}