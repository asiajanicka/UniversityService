package org.example.e2eTests.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Building;
import org.example.model.Department;
import org.example.model.Room;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IBuildingService;
import org.example.service.interfaces.IDepartmentService;
import org.example.service.jdbc.BuildingService;
import org.example.service.jdbc.DepartmentService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BuildingServiceJDBCTests {

    private static final Logger logger = LogManager.getLogger(BuildingServiceJDBCTests.class);

    @Test
    public void usecase1Test() throws NoEntityCreatedException, EntityNotFoundException {

        logger.info("Start of Building Service JDBC Tests - test case 1");
        IBuildingService buildingService = new BuildingService();
        String expectedBuildingName = "Big Data Institute";
        String expectedBuildingAddress = "Old Road Campus OX3 7LF";
        Building actualBuilding = buildingService.addNewBuilding(expectedBuildingName, expectedBuildingAddress);
        assertThat(actualBuilding.getName()).isEqualTo(expectedBuildingName);
        assertThat(actualBuilding.getAddress()).isEqualTo(expectedBuildingAddress);

        String updatedBuildingName = "Institute of Electronics";
        String updatedBuildingAddress = "New Road Campus OX3 7LF";
        actualBuilding.setName(updatedBuildingName);
        actualBuilding.setAddress(updatedBuildingAddress);
        assertThat(buildingService.updateBuildingInfo(actualBuilding)).isTrue();

        String roomOneExpectedNumber = "BDI01";
        String roomTwoExpectedNumber = "BDI02";
        Room roomOne = new Room(roomOneExpectedNumber, actualBuilding);
        Room roomTwo = new Room(roomTwoExpectedNumber, actualBuilding);
        Room actualRoomOne = buildingService.addRoom(roomOne);
        Room actualRoomTwo = buildingService.addRoom(roomTwo);
        assertThat(actualRoomOne.getNumber()).isEqualTo(roomOneExpectedNumber);
        assertThat(actualRoomOne.getBuilding()).isEqualTo(actualBuilding);

        String roomOneUpdatedNumber = "BDI0122";
        actualRoomOne.setNumber(roomOneUpdatedNumber);

        assertThat(buildingService.updateRoom(actualRoomOne)).isTrue();
        assertThat(buildingService.getRoomsInBuilding(actualBuilding.getId()))
                .contains(actualRoomOne)
                .contains(actualRoomTwo);

        assertThat(buildingService.removeBuilding(actualBuilding.getId())).isFalse();
        assertThat(buildingService.removeAllRoomsFromBuilding(actualBuilding.getId())).isTrue();
        assertThatThrownBy(() -> buildingService.getRoomById(actualRoomOne.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> buildingService.getRoomById(actualRoomTwo.getId()))
                .isInstanceOf(EntityNotFoundException.class);

        assertThat(buildingService.removeBuilding(actualBuilding.getId())).isTrue();
        assertThatThrownBy(() -> buildingService.getBuildingById(actualBuilding.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        logger.info("End of Building Service JDBC Tests - test case 1");
    }

    @Test
    public void usecase2Test() throws NoEntityCreatedException {

        logger.info("Start of Building Service JDBC Tests - test case 2");
        IBuildingService buildingService = new BuildingService();
        IDepartmentService deptService = new DepartmentService();
        Building actualBuilding = buildingService.addNewBuilding("Big Data Institute", "Old Road Campus OX3 7LF");

        Department actualDeptOne = deptService.addEmptyDept("Statistics");
        Department actualDeptTwo = deptService.addEmptyDept("Data Analytics");
        assertThat(buildingService.assignDeptToBuilding(actualDeptOne.getId(), actualBuilding.getId())).isTrue();
        assertThat(buildingService.assignDeptToBuilding(actualDeptTwo.getId(), actualBuilding.getId())).isTrue();
        assertThat(buildingService.getDeptsInBuilding(actualBuilding.getId()))
                .contains(actualDeptOne)
                .contains((actualDeptTwo));

        assertThat(buildingService.removeDeptFromBuilding(actualDeptOne.getId(), actualBuilding.getId())).isTrue();
        assertThat(buildingService.getDeptsInBuilding(actualBuilding.getId())).doesNotContain(actualDeptOne);

        assertThat(buildingService.removeBuilding(actualBuilding.getId())).isTrue();
        assertThatThrownBy(() -> buildingService.getBuildingById(actualBuilding.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        assertThat(deptService.getDepartmentsWithoutBuilding()).contains(actualDeptTwo);

        deptService.removeDept(actualDeptOne.getId());
        deptService.removeDept(actualDeptTwo.getId());
        logger.info("End of Building Service JDBC Tests - test case 2");
    }

}