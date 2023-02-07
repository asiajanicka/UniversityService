package org.example.dao.interfaces;

import org.apache.ibatis.annotations.Param;
import org.example.model.Teacher;

import java.util.List;

public interface ITeacherDAO extends IBaseDAO<Teacher> {

    List<Teacher> getTeachersByDeptId(@Param("deptId") long deptId);

    int bindTeacherToDeptId(@Param("teacherId") long teacherId, @Param("deptId") long deptId);

    int removeTeacherFromDeptById(@Param("teacherId") long teacherId, @Param("deptId") long deptId);

}
