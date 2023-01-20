package org.example.service;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.DeptDAO;
import org.example.dao.ParkingSpotDAO;
import org.example.dao.SubjectDAO;
import org.example.dao.TeacherDAO;
import org.example.dao.interfaces.IDeptDAO;
import org.example.dao.interfaces.IParkingSpotDAO;
import org.example.dao.interfaces.ISubjectDAO;
import org.example.dao.interfaces.ITeacherDAO;
import org.example.enums.EntityType;
import org.example.model.Department;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class DepartmentService {

    private final ITeacherDAO teacherDAO = new TeacherDAO();
    private final ISubjectDAO subjectDAO = new SubjectDAO();
    private final IDeptDAO deptDAO = new DeptDAO();
    private final IParkingSpotDAO spotDAO = new ParkingSpotDAO();
    private static final Logger logger = LogManager.getLogger(DepartmentService.class);

    public Teacher getTeacherById(long id) throws EntityNotFoundException {
        Teacher tempTeacher = getBasicTeacherById(id);
        tempTeacher.setSubjects(subjectDAO.getSubjectsByTeacherId(id));
        tempTeacher.setParkingSpot(spotDAO.getSpotByTeacherId(id).orElse(null));
        return tempTeacher;
    }

    public Teacher getBasicTeacherById(long id) throws EntityNotFoundException {
        Teacher tempTeacher = teacherDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TEACHER, id));
        logger.debug(String.format("Teacher (id: %d) retrieved from service", id));
        return tempTeacher;
    }

    public Teacher addTeacherWithoutSubjects(Teacher teacher) throws NoEntityCreatedException {
        if (teacher != null) {
            Teacher tempTeacher = teacherDAO
                    .createEntity(teacher)
                    .orElseThrow(() -> new NoEntityCreatedException(EntityType.TEACHER, teacher));
            logger.debug(String.format("Teacher (%s) added to the service", tempTeacher));
            return tempTeacher;
        } else {
            logger.error("Teacher couldn't be added to service as it is NULL");
            throw new NullPointerException("Teacher is NULL - can't add it to service");
        }
    }

    public boolean updateTeacherInfo(Teacher teacher) {
        if (teacher != null) {
            int result = teacherDAO.updateEntity(teacher);
            if (result == 1) {
                logger.debug(String.format("Teacher (%s) updated in the service", teacher));
                return true;
            } else {
                logger.error(String.format("Teacher (%s) couldn't be updated in the service", teacher));
                return false;
            }
        } else {
            logger.error("Teacher couldn't be updated in the service as it is NULL");
            return false;
        }
    }

    public boolean removeTeacher(Teacher teacher) {
        if (teacher != null) {
            int result = teacherDAO.removeEntity(teacher.getId());
            if (result == 1) {
                logger.debug(String.format("Teacher (%s) removed from the service", teacher));
                return true;
            } else {
                logger.error(String.format("Teacher (%s) couldn't be removed from the service", teacher));
                return false;
            }
        } else {
            logger.error("Teacher couldn't be removed from the service as it is NULL");
            return false;
        }
    }

    public List<Subject> getAllSubjectsByTeacher(Teacher teacher) {
        List<Subject> allSubjectsByTeacherId = new ArrayList<>();
        if (teacher != null) {
            allSubjectsByTeacherId = subjectDAO.getSubjectsByTeacherId(teacher.getId());
            logger.debug(String.format("Subjects of teacher (%s) retrieved from service", teacher));
        } else {
            logger.error("Subjects of teacher couldn't be retrieved from the service as teacher is NULL");
        }
        return allSubjectsByTeacherId;
    }

    public Department getDeptById(long id) throws EntityNotFoundException {
        Department dept = getBasicDeptById(id);
        dept.setTeachers(getTeachersByDeptId(id));
        return dept;
    }

    public Department getBasicDeptById(long id) throws EntityNotFoundException {
        Department tempDept = deptDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.DEPARTMENT, id));
        logger.debug(String.format("Department (id: %d) retrieved from service", id));
        return tempDept;
    }

    public Department addEmptyDept(String name) throws NoEntityCreatedException {
        Department tempDept = deptDAO
                .createEntity(new Department(name))
                .orElseThrow(() -> new NoEntityCreatedException(EntityType.DEPARTMENT, name));
        logger.debug(String.format("Department (%s) added to the service", tempDept));
        return tempDept;
    }

    public boolean removeDept(Department dept) {
        if (dept != null) {
            int result = deptDAO.removeEntity(dept.getId());
            if (result == 1) {
                logger.debug(String.format("Department (%s) removed from the service", dept));
                return true;
            } else {
                logger.error(String.format("Department (%s) couldn't be removed from the service", dept));
                return false;
            }
        } else {
            logger.error("Department couldn't be removed from the service as it is NULL");
            return false;
        }
    }

    public boolean assignTeacherToDept(Teacher teacher, Department dept) {
        if (teacher != null && dept != null) {
            int result = teacherDAO.bindTeacherToDeptId(teacher.getId(), dept.getId());
            if (result == 1) {
                logger.debug(String.format("Teacher (%s) assigned to dept (%s) in the service", teacher, dept));
                return true;
            } else {
                logger.error(String.format("Teacher (%s) couldn't be assigned to dept (%s) in the service", teacher, dept));
                return false;
            }
        } else {
            logger.error("Teacher couldn't be assigned to dept in the service as one of them is NULL");
            return false;
        }
    }

    public boolean removeTeacherFromDept(Teacher student, Department dept) {
        if (student != null && dept != null) {
            int result = teacherDAO.removeTeacherFromDeptById(student.getId(), student.getId());
            if (result == 1) {
                logger.debug(String.format("Teacher (%s) removed from department (%s) in the service", student, dept));
                return true;
            } else {
                logger.error(String.format("Teacher (%s) couldn't be removed from department (%s) in the service", student, dept));
                return false;
            }
        } else {
            logger.error("Teacher couldn't be removed from department in the service as one of them is NULL");
            return false;
        }
    }

    public List<Teacher> getTeachersByDeptId(long id) {
        List<Teacher> allTeachersByDeptId = teacherDAO.getTeachersByDeptId(id);
        for (Teacher teacher : allTeachersByDeptId) {
            teacher.setSubjects(subjectDAO.getSubjectsByTeacherId(teacher.getId()));
        }
        logger.debug(String.format("Subjects for each teacher from dept (id :%s) retrieved from service", id));
        return allTeachersByDeptId;
    }

    public List<Department> getDepartmentsWithoutBuilding() {
        List<Department> departmentsWithoutBuilding = deptDAO.getDepartmentsWithoutBuilding();
        for (Department dept : departmentsWithoutBuilding) {
            dept.setTeachers(getTeachersByDeptId(dept.getId()));
        }
        logger.debug("Departments without building assigned retrieved from service");
        return departmentsWithoutBuilding;
    }

}
