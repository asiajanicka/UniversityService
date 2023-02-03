package org.example.service.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IParkingSpotDAO;
import org.example.dao.jdbc.ParkingSpotDAO;
import org.example.enums.EntityType;
import org.example.model.ParkingSpot;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IParkingSpotService;

import java.util.List;

@NoArgsConstructor
public class ParkingSpotService implements IParkingSpotService {

    private final IParkingSpotDAO spotDAO = new ParkingSpotDAO();
    private static final Logger logger = LogManager.getLogger(ParkingSpotService.class);

    @Override
    public ParkingSpot getSpotById(long id) throws EntityNotFoundException {
        ParkingSpot tempSpot = spotDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.PARKING_SPOT, id));
        logger.debug(String.format("Parking spot (id: %d) retrieved from service", id));
        return tempSpot;
    }

    @Override
    public ParkingSpot addNewSpot(String name, String address) throws NoEntityCreatedException {
        ParkingSpot spotToAdd = new ParkingSpot(name, address);
        if (name != null && address != null) {
            spotDAO.createEntity(spotToAdd);
            logger.debug(String.format("Parking spot %s added to the service", spotToAdd));
            return spotToAdd;
        } else {
            throw new NoEntityCreatedException(EntityType.BUILDING, spotToAdd);
        }
    }

    @Override
    public boolean assignSpotToTeacher(long spotId, long teacherId) {
        if (spotId > 0 && teacherId > 0) {
            int result = spotDAO.bindSpotToTeacherId(spotId, teacherId);
            if (result == 1) {
                logger.debug(String.format("Parking spot (%d) assigned to teacher (%d) in the service",
                        spotId, teacherId));
                return true;
            } else {
                logger.error(String.format("Parking spot (%d) couldn't be assigned to teacher (%d) in the service",
                        spotId, teacherId));
                return false;
            }
        } else {
            logger.error("Parking spot couldn't be assigned to teacher in the service as one of its ids is invalid");
            return false;
        }
    }

    @Override
    public boolean setSpotFree(long id) {
        if (id > 0) {
            int result = spotDAO.setSpotFree(id);
            if (result == 1) {
                logger.debug(String.format("Removed teacher from parking spot (%d) in the service. Parking spot is free", id));
                return true;
            } else {
                logger.error(String.format("Parking spot (%d) couldn't be set free in the service", id));
                return false;
            }
        } else {
            logger.error("Parking spot couldn't be set free in the service as its id is invalid");
            return false;
        }
    }

    @Override
    public boolean removeSpot(long id) {
            int result = spotDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Parking spot (%d) removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Parking spot (%d) couldn't be removed from the service", id));
                return false;
            }
    }

    @Override
    public List<ParkingSpot> getFreeSpots() {
        return spotDAO.getFreeSpots();
    }

}