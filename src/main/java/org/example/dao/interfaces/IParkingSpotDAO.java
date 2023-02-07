package org.example.dao.interfaces;

import org.apache.ibatis.annotations.Param;
import org.example.model.ParkingSpot;

import java.util.List;
import java.util.Optional;

public interface IParkingSpotDAO extends IBaseDAO<ParkingSpot> {

    Optional<ParkingSpot> getSpotByTeacherId(@Param("teacherId") long teacherId);

    int bindSpotToTeacherId(@Param("spotId") long spotId, @Param("teacherId") long teacherId);

    int setSpotFree(@Param("spotId")long spotId);

    List<ParkingSpot> getFreeSpots();

}
