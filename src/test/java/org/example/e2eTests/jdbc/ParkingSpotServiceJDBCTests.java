package org.example.e2eTests.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.ParkingSpot;
import org.example.model.Teacher;
import org.example.service.interfaces.IDepartmentService;
import org.example.service.interfaces.IParkingSpotService;
import org.example.service.jdbc.DepartmentService;
import org.example.service.jdbc.ParkingSpotService;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.junit.jupiter.api.Test;
import utils.TestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ParkingSpotServiceJDBCTests {

    private static final Logger logger = LogManager.getLogger(ParkingSpotServiceJDBCTests.class);

    @Test
    public void usecase1Test() throws NoEntityCreatedException, EntityNotFoundException {

        logger.info("Start of Parking Spot Service JDBC Tests - test case 1");
        IParkingSpotService spotService = new ParkingSpotService();
        IDepartmentService deptService = new DepartmentService();
        Teacher testTeacher = TestData.getBasicTeacher();
        String expectedSpotName = "AB123";
        String expectedSpotAddress = "Old Road Campus OX3 7LF";

        Teacher actualTeacher = deptService.addTeacherWithoutSubjects(testTeacher);
        ParkingSpot actualSpot = spotService.addNewSpot(expectedSpotName, expectedSpotAddress);
        assertThat(actualSpot.getName()).isEqualTo(expectedSpotName);
        assertThat(actualSpot.getAddress()).isEqualTo(expectedSpotAddress);

        assertThat(spotService.assignSpotToTeacher(actualSpot.getId(), actualTeacher.getId())).isTrue();
        actualTeacher = deptService.getTeacherById(actualTeacher.getId());
        assertThat(actualTeacher.getParkingSpot()).isEqualTo(actualSpot);

        assertThat(spotService.setSpotFree(actualSpot.getId())).isTrue();
        assertThat(spotService.getFreeSpots()).contains(actualSpot);

        actualTeacher = deptService.getTeacherById(actualTeacher.getId());
        assertThat(actualTeacher.getParkingSpot()).isNull();

        spotService.assignSpotToTeacher(actualSpot.getId(), actualTeacher.getId());

        deptService.removeTeacher(actualTeacher.getId());
        assertThat(spotService.getFreeSpots()).contains(actualSpot);

        assertThat(spotService.removeSpot(actualSpot.getId())).isTrue();
        assertThatThrownBy(() -> spotService.getSpotById(actualSpot.getId()))
                .isInstanceOf(EntityNotFoundException.class);

        deptService.removeTeacher(actualTeacher.getId());
        logger.info("End of Parking Spot Service JDBC Tests - test case 1");
    }

}