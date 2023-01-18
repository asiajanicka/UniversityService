package org.example.dao.interfaces;

import org.example.model.Teacher;

import java.util.List;

public interface ITeacherDAO extends IBaseDAO<Teacher> {

    List<Teacher> getTeachersByDeptId(long deptId);

    int bindTeacherToDeptId(long teacherId, long deptId);

    int removeTeacherFromDeptById(long teacherId, long deptId);

}
