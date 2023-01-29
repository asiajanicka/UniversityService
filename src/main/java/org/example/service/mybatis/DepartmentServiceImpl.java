package org.example.service.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IDeptDAO;
import org.example.dao.interfaces.ISubjectDAO;
import org.example.dao.interfaces.ITeacherDAO;
import org.example.enums.EntityType;
import org.example.model.Department;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IDepartmentService;
import org.example.utils.MyBatisDaoFactory;

import java.util.ArrayList;
import java.util.List;

public class DepartmentServiceImpl implements IDepartmentService {

    private static final SqlSessionFactory SESSION_FACTORY = MyBatisDaoFactory.getSqlSessionFactory();
    private static final Logger logger = LogManager.getLogger(DepartmentServiceImpl.class);

    @Override
    public Teacher getTeacherById(long id) throws EntityNotFoundException {
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            Teacher tempTeacher;
            ITeacherDAO teacherDAO = sqlSession.getMapper(ITeacherDAO.class);
            tempTeacher = teacherDAO
                    .getEntityById(id)
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.TEACHER, id));
            logger.debug(String.format("Teacher with %d retrieved from service", id));
            return tempTeacher;
        }
    }

    @Override
    public Teacher addTeacherWithoutSubjects(Teacher teacher) throws NoEntityCreatedException {
        if (teacher != null) {
            if (teacher.getFirstName() != null && teacher.getLastName() != null) {
                try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                    ITeacherDAO teacherDAO = sqlSession.getMapper(ITeacherDAO.class);
                    try {
                        teacherDAO.createEntity(teacher);
                        logger.debug(String.format("Teacher (%s) added to the service", teacher));
                        sqlSession.commit();
                        return teacher;
                    } catch (Exception e) {
                        sqlSession.rollback();
                        logger.error(e.getMessage(), e);
                        throw new NoEntityCreatedException(EntityType.TEACHER, teacher);
                    }
                }
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
                try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                    ITeacherDAO teacherDAO = sqlSession.getMapper(ITeacherDAO.class);
                    int result = 0;
                    try {
                        result = teacherDAO.updateEntity(teacher);
                        sqlSession.commit();
                    } catch (Exception e) {
                        sqlSession.rollback();
                        e.printStackTrace();
                        logger.error(e.getMessage(), e);
                    }
                    if (result == 1) {
                        logger.debug(String.format("Teacher (%s) updated in the service", teacher));
                        return true;
                    } else {
                        logger.error(String.format("Teacher (%s) couldn't be updated in the service", teacher));
                        return false;
                    }
                }
            } else {
                logger.error(String.format("Teacher (%s) couldn't be updated in the service as some of its fields is incorrect or NULL",
                        teacher));
                return false;
            }
        } else {
            logger.error("Teacher couldn't be updated in the service as it is NULL");
            return false;
        }
    }

    @Override
    public boolean removeTeacher(long id) {
        if (id > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                ITeacherDAO teacherDAO = sqlSession.getMapper(ITeacherDAO.class);
                int result = 0;
                try {
                    result = teacherDAO.removeEntity(id);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Teacher (%d) removed from the service", id));
                    return true;
                } else {
                    logger.error(String.format("Teacher (%d) couldn't be removed from the service", id));
                    return false;
                }
            }
        } else {
            logger.error("Teacher couldn't be removed from the service as its id is invalid");
            return false;
        }
    }

    @Override
    public List<Subject> getAllSubjectsByTeacher(long teacherId) {
        List<Subject> allSubjectsByTeacherId = new ArrayList<>();
        if (teacherId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                ISubjectDAO subjectDAO = sqlSession.getMapper(ISubjectDAO.class);
                allSubjectsByTeacherId = subjectDAO.getSubjectsByTeacherId(teacherId);
                logger.debug(String.format("Subjects of teacher (%d) retrieved from service", teacherId));
            }
        } else {
            logger.error("Subjects of teacher couldn't be retrieved from the service as its id is invalid");
        }
        return allSubjectsByTeacherId;
    }

    @Override
    public Department getDeptById(long id) throws EntityNotFoundException {
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            IDeptDAO deptDAO = sqlSession.getMapper(IDeptDAO.class);
            Department tempDept = deptDAO
                    .getEntityById(id)
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.DEPARTMENT, id));
            logger.debug(String.format("Department (id: %d) retrieved from service", id));
            return tempDept;
        }
    }

    @Override
    public Department addEmptyDept(String name) throws NoEntityCreatedException {
        if (name != null) {
            Department deptToAdd = new Department(name);
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IDeptDAO deptDAO = sqlSession.getMapper(IDeptDAO.class);
                try {
                    deptDAO.createEntity(deptToAdd);
                    sqlSession.commit();
                    logger.debug(String.format("Department (%s) added to the service", deptToAdd));
                    return deptToAdd;
                } catch (Exception e) {
                    sqlSession.rollback();
                    logger.error(e.getMessage(), e);
                    throw new NoEntityCreatedException(EntityType.DEPARTMENT, name);
                }
            }
        } else {
            throw new NoEntityCreatedException(EntityType.DEPARTMENT);
        }
    }

    @Override
    public boolean removeDept(long id) {
        if (id > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IDeptDAO deptDAO = sqlSession.getMapper(IDeptDAO.class);
                int result = 0;
                try {
                    result = deptDAO.removeEntity(id);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Department (%d) removed from the service", id));
                    return true;
                } else {
                    logger.error(String.format("Department (%d) couldn't be removed from the service", id));
                    return false;
                }
            }
        } else {
            logger.error("Department couldn't be removed from the service as its id is invalid");
            return false;
        }
    }

    @Override
    public boolean assignTeacherToDept(long teacherId, long deptId) {
        if (teacherId > 0 && deptId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                ITeacherDAO teacherDAO = sqlSession.getMapper(ITeacherDAO.class);
                int result = 0;
                try {
                    result = teacherDAO.bindTeacherToDeptId(teacherId, deptId);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Teacher (%d) assigned to dept (%d) in the service", teacherId, deptId));
                    return true;
                } else {
                    logger.error(String.format("Teacher (%d) couldn't be assigned to dept (%d) in the service", teacherId, deptId));
                    return false;
                }
            }
        } else {
            logger.error("Teacher couldn't be assigned to dept in the service as one of its id is invalid");
            return false;
        }
    }

    @Override
    public boolean removeTeacherFromDept(long teacherId, long deptId) {
        if (teacherId > 0 && deptId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                ITeacherDAO teacherDAO = sqlSession.getMapper(ITeacherDAO.class);
                int result = 0;
                try {
                    result = teacherDAO.removeTeacherFromDeptById(teacherId, deptId);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Teacher (%d) removed from department (%d) in the service", teacherId, deptId));
                    return true;
                } else {
                    logger.error(String.format("Teacher (%d) couldn't be removed from department (%d) in the service", teacherId, deptId));
                    return false;
                }
            }
        } else {
            logger.error("Teacher couldn't be removed from department in the service as one of its id is invalid");
            return false;
        }
    }

    @Override
    public List<Teacher> getTeachersByDeptId(long id) {
        List<Teacher> allTeachersByDeptId;
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            ITeacherDAO teacherDAO = sqlSession.getMapper(ITeacherDAO.class);
            allTeachersByDeptId = new ArrayList<>(teacherDAO.getTeachersByDeptId(id));
        }
        logger.debug(String.format("Subjects for each teacher from dept (id :%d) retrieved from service", id));
        return allTeachersByDeptId;
    }

    @Override
    public List<Department> getDepartmentsWithoutBuilding() {
        List<Department> departmentsWithoutBuilding;
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            IDeptDAO deptDAO = sqlSession.getMapper(IDeptDAO.class);
            departmentsWithoutBuilding = new ArrayList<>(deptDAO.getDepartmentsWithoutBuilding());
        }
        logger.debug("Departments without building assigned retrieved from service");
        return departmentsWithoutBuilding;
    }

}