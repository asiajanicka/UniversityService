package org.example.dao.interfaces;

import org.example.model.Department;

import java.util.List;

public interface IDeptDAO extends IBaseDAO<Department> {

    List<Department> getDepartmentsByBuildingId(long buildingId);

    int bindDepartmentToBuildingId(long deptId, long buildingId);

    int removeDepartmentFromBuildingById(long deptId, long buildingId);

}
