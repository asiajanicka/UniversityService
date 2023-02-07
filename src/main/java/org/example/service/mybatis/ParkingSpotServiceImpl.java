package org.example.service.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IParkingSpotDAO;
import org.example.enums.EntityType;
import org.example.model.ParkingSpot;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IParkingSpotService;
import org.example.utils.MyBatisDaoFactory;

import java.util.List;

public class ParkingSpotServiceImpl implements IParkingSpotService {

    private static final SqlSessionFactory SESSION_FACTORY = MyBatisDaoFactory.getSqlSessionFactory();
    private static final Logger logger = LogManager.getLogger(ParkingSpotServiceImpl.class);

    @Override
    public ParkingSpot getSpotById(long id) throws EntityNotFoundException {
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            ParkingSpot tempSpot;
            IParkingSpotDAO spotDAO = sqlSession.getMapper(IParkingSpotDAO.class);
            tempSpot = spotDAO
                    .getEntityById(id)
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.PARKING_SPOT, id));
            logger.debug(String.format("Parking spot (id: %d) retrieved from service", id));
            return tempSpot;
        }
    }

    @Override
    public ParkingSpot addNewSpot(String name, String address) throws NoEntityCreatedException {
        ParkingSpot spotToAdd = new ParkingSpot(name, address);
        if (name != null && address != null) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IParkingSpotDAO spotDAO = sqlSession.getMapper(IParkingSpotDAO.class);
                try {
                    spotDAO.createEntity(spotToAdd);
                    sqlSession.commit();
                    logger.debug(String.format("Parking spot %s added to the service", spotToAdd));
                    return spotToAdd;
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                    throw new NoEntityCreatedException(EntityType.PARKING_SPOT, spotToAdd);
                }
            }
        } else {
            throw new NoEntityCreatedException(EntityType.BUILDING, spotToAdd);
        }
    }

    @Override
    public boolean assignSpotToTeacher(long spotId, long teacherId) {
        if (spotId > 0 && teacherId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IParkingSpotDAO spotDAO = sqlSession.getMapper(IParkingSpotDAO.class);
                int result = 0;
                try {
                    result = spotDAO.bindSpotToTeacherId(spotId, teacherId);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Parking spot (%d) assigned to teacher (%d) in the service",
                            spotId, teacherId));
                    return true;
                } else {
                    logger.error(String.format("Parking spot (%d) couldn't be assigned to teacher (%d) in the service",
                            spotId, teacherId));
                    return false;
                }
            }
        } else {
            logger.error("Parking spot couldn't be assigned to teacher in the service as one of its ids is invalid");
            return false;
        }
    }

    @Override
    public boolean setSpotFree(long id) {
        if (id > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IParkingSpotDAO spotDAO = sqlSession.getMapper(IParkingSpotDAO.class);
                int result = 0;
                try {
                    result = spotDAO.setSpotFree(id);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Removed teacher from parking spot (%d) in the service. Parking spot is free", id));
                    return true;
                } else {
                    logger.error(String.format("Parking spot (%d) couldn't be set free in the service", id));
                    return false;
                }
            }
        } else {
            logger.error("Parking spot couldn't be set free in the service as its id is invalid");
            return false;
        }
    }

    @Override
    public boolean removeSpot(long id) {
        if (id > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IParkingSpotDAO spotDAO = sqlSession.getMapper(IParkingSpotDAO.class);
                int result = 0;
                try {
                    result = spotDAO.removeEntity(id);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Parking spot (%d) removed from the service", id));
                    return true;
                } else {
                    logger.error(String.format("Parking spot (%d) couldn't be removed from the service", id));
                    return false;
                }
            }
        } else {
            logger.error("Parking spot couldn't be removed from the service as its id is invalid");
            return false;
        }
    }

    @Override
    public List<ParkingSpot> getFreeSpots() {
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            IParkingSpotDAO spotDAO = sqlSession.getMapper(IParkingSpotDAO.class);
            return spotDAO.getFreeSpots();
        }
    }

}