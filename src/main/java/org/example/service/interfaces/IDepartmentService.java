package org.example.service.interfaces;

import org.example.model.Department;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.List;

public interface IDepartmentService {

    Teacher getTeacherById(long id) throws EntityNotFoundException;


    Teacher addTeacherWithoutSubjects(Teacher teacher) throws NoEntityCreatedException;

    boolean updateTeacherInfo(Teacher teacher);

    boolean removeTeacher(long id);

    List<Subject> getAllSubjectsByTeacher(long teacherId);

    Department getDeptById(long id) throws EntityNotFoundException;

    Department addEmptyDept(String name) throws NoEntityCreatedException;

    boolean removeDept(long id);

    boolean assignTeacherToDept(long teacherId, long deptId);

    boolean removeTeacherFromDept(long teacherId, long deptId);

    List<Teacher> getTeachersByDeptId(long id);

    List<Department> getDepartmentsWithoutBuilding();

}