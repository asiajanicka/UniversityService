package org.example.service.interfaces;

import org.example.model.ParkingSpot;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.List;

public interface IParkingSpotService {

    ParkingSpot getSpotById(long id) throws EntityNotFoundException;

    ParkingSpot addNewSpot(String name, String address) throws NoEntityCreatedException;

    boolean assignSpotToTeacher(long spotId, long teacherId);

    boolean setSpotFree(long id);

    boolean removeSpot(long id);

    List<ParkingSpot> getFreeSpots();

}