package org.example.e2eTests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.ParkingSpot;
import org.example.model.Teacher;
import org.example.service.DepartmentService;
import org.example.service.ParkingSpotService;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.junit.jupiter.api.Test;
import utils.TestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ParkingSpotServiceTests {

    private static final Logger logger = LogManager.getLogger(ParkingSpotServiceTests.class);

    @Test
    public void usecase1Test() throws NoEntityCreatedException, EntityNotFoundException {

//        Add a new parking spot
//        Add a new teacher
//        Assign spot to teacher
//          - check if spot is assigned to teacher
//        Set spot free
//          - check if spot is free
//          - check if teacher doesn't have a spot
//        Assign spot to teacher again
//        Remove teacher from uni
//          - check if spot is free again
//        Remove spot
//          - check if spot is removed
//        Remove teacher (clean up)

        logger.info("Start of Parking Spot Service Tests - test case 1");
        ParkingSpotService spotService = new ParkingSpotService();
        DepartmentService deptService = new DepartmentService();
        Teacher testTeacher = TestData.getBasicTeacher();
        String expectedSpotName = "AB123";
        String expectedSpotAddress = "Old Road Campus OX3 7LF";

        Teacher actualTeacher = deptService.addTeacherWithoutSubjects(testTeacher);
        ParkingSpot actualSpot = spotService.addNewSpot(expectedSpotName, expectedSpotAddress);
        assertThat(actualSpot.getName()).isEqualTo(expectedSpotName);
        assertThat(actualSpot.getAddress()).isEqualTo(expectedSpotAddress);

        assertThat(spotService.assignSpotToTeacher(actualSpot, actualTeacher)).isTrue();
        actualTeacher = deptService.getTeacherById(actualTeacher.getId());
        assertThat(actualTeacher.getParkingSpot()).isEqualTo(actualSpot);

        assertThat(spotService.setSpotFree(actualSpot)).isTrue();
        assertThat(spotService.getFreeSpots()).contains(actualSpot);

        actualTeacher = deptService.getTeacherById(actualTeacher.getId());
        assertThat(actualTeacher.getParkingSpot()).isNull();

        spotService.assignSpotToTeacher(actualSpot, actualTeacher);

        deptService.removeTeacher(actualTeacher);
        assertThat(spotService.getFreeSpots()).contains(actualSpot);

        assertThat(spotService.removeSpot(actualSpot)).isTrue();
        assertThatThrownBy(() -> spotService.getSpotById(actualSpot.getId()))
                .isInstanceOf(EntityNotFoundException.class);

        deptService.removeTeacher(actualTeacher);
        logger.info("End of Parking Spot Service Tests - test case 1");
    }

}