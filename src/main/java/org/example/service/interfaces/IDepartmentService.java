package org.example.service.interfaces;

import org.example.model.Department;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.List;

public interface IDepartmentService {

    Teacher getTeacherById(long id) throws EntityNotFoundException;

    Teacher getBasicTeacherById(long id) throws EntityNotFoundException;

    Teacher addTeacherWithoutSubjects(Teacher teacher) throws NoEntityCreatedException;

    boolean updateTeacherInfo(Teacher teacher);

    boolean removeTeacher(Teacher teacher);

    List<Subject> getAllSubjectsByTeacher(Teacher teacher);

    Department getDeptById(long id) throws EntityNotFoundException;

    Department getBasicDeptById(long id) throws EntityNotFoundException;

    Department addEmptyDept(String name) throws NoEntityCreatedException;

    boolean removeDept(Department dept);

    boolean assignTeacherToDept(Teacher teacher, Department dept);

    boolean removeTeacherFromDept(Teacher student, Department dept);

    List<Teacher> getTeachersByDeptId(long id);

    List<Department> getDepartmentsWithoutBuilding();

}