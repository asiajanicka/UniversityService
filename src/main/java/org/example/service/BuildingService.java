package org.example.service;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.BuildingDAO;
import org.example.dao.DeptDAO;
import org.example.dao.RoomDAO;
import org.example.dao.interfaces.IBuildingDAO;
import org.example.dao.interfaces.IDeptDAO;
import org.example.dao.interfaces.IRoomDAO;
import org.example.enums.EntityType;
import org.example.model.Building;
import org.example.model.Department;
import org.example.service.exception.EntityNotFoundException;

import java.util.List;

@NoArgsConstructor
public class BuildingService {

    private final IBuildingDAO buildingDAO = new BuildingDAO();
    private final IRoomDAO roomDAO = new RoomDAO();
    private final IDeptDAO deptDAO = new DeptDAO();
    private DepartmentService departmentService = new DepartmentService();
    private static final Logger logger = LogManager.getLogger(BuildingService.class);

    public Building getBuildingById(long id) throws EntityNotFoundException {
        Building tempBuilding = getBasicBuildingById(id);
        tempBuilding.setRooms(roomDAO.getRoomsByBuildingId(id));
        tempBuilding.setDepartments(getDeptsInBuilding(tempBuilding));
        return tempBuilding;
    }

    public Building getBasicBuildingById(long id) throws EntityNotFoundException {
        Building tempBuilding = buildingDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.BUILDING, id));
        logger.debug(String.format("Building (id: %d) retrieved from service", id));
        return tempBuilding;
    }

    public boolean assignDeptToBuilding(Department dept, Building building) {
        if (dept != null && building != null) {
            int result = deptDAO.bindDepartmentToBuildingId(dept.getId(), building.getId());
            if (result == 1) {
                logger.debug(String.format("Department (%s) assigned to building (%s) in the service", dept, building));
                return true;
            } else {
                logger.error(String.format("Department (%s) couldn't be assigned to building (%s) in the service", dept, building));
                return false;
            }
        } else {
            logger.error("Department couldn't be assigned to building in the service as one of them is NULL");
            return false;
        }
    }

    public boolean removeDeptFromBuilding(Department dept, Building building) {
        if (dept != null && building != null) {
            int result = deptDAO.removeDepartmentFromBuildingById(dept.getId(), dept.getId());
            if (result == 1) {
                logger.debug(String.format("Department (%s) removed from building (%s) in the service", dept, building));
                return true;
            } else {
                logger.error(String.format("Department (%s) couldn't be removed from building (%s) in the service", dept, building));
                return false;
            }
        } else {
            logger.error("Department couldn't be removed from building in the service as one of them is NULL");
            return false;
        }
    }

    public List<Department> getDeptsInBuilding(Building building) {
        List<Department> deptsByBuildingId = deptDAO.getDepartmentsByBuildingId(building.getId());
        for (Department dept : deptsByBuildingId) {
            dept.setTeachers(departmentService.getTeachersByDeptId(dept.getId()));
        }
        return deptsByBuildingId;
    }

}
