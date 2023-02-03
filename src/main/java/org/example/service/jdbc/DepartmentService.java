package org.example.service.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IDeptDAO;
import org.example.dao.interfaces.IParkingSpotDAO;
import org.example.dao.interfaces.ISubjectDAO;
import org.example.dao.interfaces.ITeacherDAO;
import org.example.dao.jdbc.DeptDAO;
import org.example.dao.jdbc.ParkingSpotDAO;
import org.example.dao.jdbc.SubjectDAO;
import org.example.dao.jdbc.TeacherDAO;
import org.example.enums.EntityType;
import org.example.model.Department;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IDepartmentService;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class DepartmentService implements IDepartmentService {

    private final ITeacherDAO teacherDAO = new TeacherDAO();
    private final ISubjectDAO subjectDAO = new SubjectDAO();
    private final IDeptDAO deptDAO = new DeptDAO();
    private final IParkingSpotDAO spotDAO = new ParkingSpotDAO();
    private static final Logger logger = LogManager.getLogger(DepartmentService.class);

    @Override
    public Teacher getTeacherById(long id) throws EntityNotFoundException {
        Teacher tempTeacher = getBasicTeacherById(id);
        tempTeacher.setSubjects(subjectDAO.getSubjectsByTeacherId(id));
        tempTeacher.setParkingSpot(spotDAO.getSpotByTeacherId(id).orElse(null));
        return tempTeacher;
    }

    private Teacher getBasicTeacherById(long id) throws EntityNotFoundException {
        Teacher tempTeacher = teacherDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TEACHER, id));
        logger.debug(String.format("Teacher (id: %d) retrieved from service", id));
        return tempTeacher;
    }

    @Override
    public Teacher addNewTeacher(Teacher teacher) throws NoEntityCreatedException {
        if (teacher != null) {
            if (teacher.getFirstName() != null && teacher.getLastName() != null) {
                teacherDAO.createEntity(teacher);
                logger.debug(String.format("Teacher (%s) added to the service", teacher));
                return teacher;
            } else {
                throw new NoEntityCreatedException(EntityType.TEACHER, teacher);
            }
        } else {
            logger.error("Teacher couldn't be added to service as it is NULL");
            throw new NullPointerException("Teacher is NULL - can't add it to service");
        }
    }

    @Override
    public boolean updateTeacherInfo(Teacher teacher) {
        if (teacher != null) {
            if (teacher.getFirstName() != null && teacher.getLastName() != null && teacher.getId() > 0) {
                int result = teacherDAO.updateEntity(teacher);
                if (result == 1) {
                    logger.debug(String.format("Teacher (%s) updated in the service", teacher));
                    return true;
                } else {
                    logger.error(String.format("Teacher (%s) couldn't be updated in the service", teacher));
                    return false;
                }
            } else {
                logger.error(String.format("Teacher (%s) couldn't be updated in the service", teacher));
                return false;
            }
        } else {
            logger.error("Teacher couldn't be updated in the service as it is NULL");
            return false;
        }
    }

    @Override
    public boolean removeTeacher(long id) {
            int result = teacherDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Teacher (%d) removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Teacher (%d) couldn't be removed from the service", id));
                return false;
            }
    }

    @Override
    public List<Subject> getAllSubjectsByTeacher(long teacherId) {
        List<Subject> allSubjectsByTeacherId = new ArrayList<>();
        if (teacherId > 0) {
            allSubjectsByTeacherId = subjectDAO.getSubjectsByTeacherId(teacherId);
            logger.debug(String.format("Subjects of teacher (%d) retrieved from service", teacherId));
        } else {
            logger.error("Subjects of teacher couldn't be retrieved from the service as its id is invalid");
        }
        return allSubjectsByTeacherId;
    }

    @Override
    public Department getDeptById(long id) throws EntityNotFoundException {
        Department dept = getBasicDeptById(id);
        dept.setTeachers(getTeachersByDeptId(id));
        return dept;
    }

    private Department getBasicDeptById(long id) throws EntityNotFoundException {
        Department tempDept = deptDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.DEPARTMENT, id));
        logger.debug(String.format("Department (id: %d) retrieved from service", id));
        return tempDept;
    }

    @Override
    public Department addEmptyDept(String name) throws NoEntityCreatedException {
        if (name != null) {
            Department departmentToAdd = new Department(name);
            deptDAO.createEntity(departmentToAdd);
            logger.debug(String.format("Department (%s) added to the service", departmentToAdd));
            return departmentToAdd;
        } else {
            throw new NoEntityCreatedException(EntityType.DEPARTMENT);
        }
    }

    @Override
    public boolean removeDept(long id) {
            int result = deptDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Department (%d) removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Department (%d) couldn't be removed from the service", id));
                return false;
            }
    }

    @Override
    public boolean assignTeacherToDept(long teacherId, long deptId) {
        if (teacherId > 0 && deptId > 0) {
            int result = teacherDAO.bindTeacherToDeptId(teacherId, deptId);
            if (result == 1) {
                logger.debug(String.format("Teacher (%d) assigned to dept (%d) in the service", teacherId, deptId));
                return true;
            } else {
                logger.error(String.format("Teacher (%d) couldn't be assigned to dept (%d) in the service", teacherId, deptId));
                return false;
            }
        } else {
            logger.error("Teacher couldn't be assigned to dept in the service as one of its id is invalid");
            return false;
        }
    }

    @Override
    public boolean removeTeacherFromDept(long teacherId, long deptId) {
        if (teacherId > 0 && deptId > 0) {
            int result = teacherDAO.removeTeacherFromDeptById(teacherId, deptId);
            if (result == 1) {
                logger.debug(String.format("Teacher (%d) removed from department (%d) in the service", teacherId, deptId));
                return true;
            } else {
                logger.error(String.format("Teacher (%d) couldn't be removed from department (%d) in the service", teacherId, deptId));
                return false;
            }
        } else {
            logger.error("Teacher couldn't be removed from department in the service as one of its id is invalid");
            return false;
        }
    }

    @Override
    public List<Teacher> getTeachersByDeptId(long id) {
        List<Teacher> allTeachersByDeptId = teacherDAO.getTeachersByDeptId(id);
        for (Teacher teacher : allTeachersByDeptId) {
            teacher.setSubjects(subjectDAO.getSubjectsByTeacherId(teacher.getId()));
        }
        logger.debug(String.format("Subjects for each teacher from dept (id :%d) retrieved from service", id));
        return allTeachersByDeptId;
    }

    @Override
    public List<Department> getDepartmentsWithoutBuilding() {
        List<Department> departmentsWithoutBuilding = deptDAO.getDepartmentsWithoutBuilding();
        for (Department dept : departmentsWithoutBuilding) {
            dept.setTeachers(getTeachersByDeptId(dept.getId()));
        }
        logger.debug("Departments without building assigned retrieved from service");
        return departmentsWithoutBuilding;
    }

}