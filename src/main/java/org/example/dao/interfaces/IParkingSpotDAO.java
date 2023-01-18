package org.example.dao.interfaces;

import org.example.model.ParkingSpot;

import java.util.List;
import java.util.Optional;

public interface IParkingSpotDAO extends IBaseDAO<ParkingSpot> {

    Optional<ParkingSpot> getSpotByTeacherId(long teacherId);

    int bindSpotToTeacherId(long spotId, long teacherId);

    int setSpotFree(long spotId);

    List<ParkingSpot> getFreeSpots();

}
