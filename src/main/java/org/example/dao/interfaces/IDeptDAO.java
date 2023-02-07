package org.example.dao.interfaces;

import org.apache.ibatis.annotations.Param;
import org.example.model.Department;

import java.util.List;

public interface IDeptDAO extends IBaseDAO<Department> {

    List<Department> getDepartmentsByBuildingId(@Param("buildingId") long buildingId);

    int bindDepartmentToBuildingId(@Param("deptId") long deptId, @Param("buildingId") long buildingId);

    int removeDepartmentFromBuildingById(@Param("deptId") long deptId, @Param("buildingId") long buildingId);

    List<Department> getDepartmentsWithoutBuilding();

}
