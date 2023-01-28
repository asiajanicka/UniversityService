package org.example.service.interfaces;

import org.example.model.ParkingSpot;
import org.example.model.Teacher;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.List;

public interface IParkingSpotService {

    ParkingSpot getSpotById(long id) throws EntityNotFoundException;

    ParkingSpot addNewSpot(String name, String address) throws NoEntityCreatedException;

    boolean assignSpotToTeacher(ParkingSpot spot, Teacher teacher);

    boolean setSpotFree(ParkingSpot spot);

    boolean removeSpot(ParkingSpot spot);

    List<ParkingSpot> getFreeSpots();

}