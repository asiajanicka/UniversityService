package org.example.service.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IParkingSpotDAO;
import org.example.dao.jdbc.ParkingSpotDAO;
import org.example.enums.EntityType;
import org.example.model.ParkingSpot;
import org.example.model.Teacher;
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
        ParkingSpot tempSpot = spotDAO
                .createEntity(spotToAdd)
                .orElseThrow(() -> new NoEntityCreatedException(EntityType.PARKING_SPOT, spotToAdd));
        logger.debug(String.format("Parking spot %s added to the service", tempSpot));
        return tempSpot;
    }

    @Override
    public boolean assignSpotToTeacher(ParkingSpot spot, Teacher teacher) {
        if (spot != null && teacher != null) {
            int result = spotDAO.bindSpotToTeacherId(spot.getId(), teacher.getId());
            if (result == 1) {
                logger.debug(String.format("Parking spot (%s) assigned to teacher (%s) in the service",
                        spot, teacher));
                return true;
            } else {
                logger.error(String.format("Parking spot (%s) couldn't be assigned to teacher (%s) in the service",
                        spot, teacher));
                return false;
            }
        } else {
            logger.error("Parking spot couldn't be assigned to teacher in the service as one of them is NULL");
            return false;
        }
    }

    @Override
    public boolean setSpotFree(ParkingSpot spot) {
        if (spot != null) {
            int result = spotDAO.setSpotFree(spot.getId());
            if (result == 1) {
                logger.debug(String.format("Removed teacher from parking spot (%s) in the service. Parking spot is free",
                        spot));
                return true;
            } else {
                logger.error(String.format("Parking spot (%s) couldn't be set free in the service", spot));
                return false;
            }
        } else {
            logger.error("Parking spot couldn't be set free in the service as subject is NULL");
            return false;
        }
    }

    @Override
    public boolean removeSpot(ParkingSpot spot) {
        if (spot != null) {
            int result = spotDAO.removeEntity(spot.getId());
            if (result == 1) {
                logger.debug(String.format("Parking spot (%s) removed from the service", spot));
                return true;
            } else {
                logger.error(String.format("Parking spot (%s) couldn't be removed from the service", spot));
                return false;
            }
        } else {
            logger.error("Parking spot couldn't be removed from the service as it is NULL");
            return false;
        }
    }

    @Override
    public List<ParkingSpot> getFreeSpots() {
        return spotDAO.getFreeSpots();
    }

}