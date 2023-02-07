package org.example.service.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IGradeDAO;
import org.example.dao.interfaces.ISubjectDAO;
import org.example.enums.EntityType;
import org.example.model.Grade;
import org.example.model.Subject;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.ISubjectService;
import org.example.utils.MyBatisDaoFactory;

import java.util.ArrayList;
import java.util.List;

public class SubjectServiceImpl implements ISubjectService {

    private static final SqlSessionFactory SESSION_FACTORY = MyBatisDaoFactory.getSqlSessionFactory();
    private static final Logger logger = LogManager.getLogger(SubjectServiceImpl.class);

    @Override
    public Subject getSubjectById(long id) throws EntityNotFoundException {
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            Subject tempSubject;
            ISubjectDAO subjectDAO = sqlSession.getMapper(ISubjectDAO.class);
            tempSubject = subjectDAO
                    .getEntityById(id)
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.SUBJECT, id));
            logger.debug(String.format("Subject with %d retrieved from service", id));
            return tempSubject;
        }
    }

    @Override
    public Subject addNewSubject(String subjectName) throws NoEntityCreatedException {
        if (subjectName != null) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                Subject subjectToAdd = new Subject(subjectName);
                ISubjectDAO subjectDAO = sqlSession.getMapper(ISubjectDAO.class);
                try {
                    subjectDAO.createEntity(subjectToAdd);
                    sqlSession.commit();
                    logger.debug(String.format("Portal account %s added to the service", subjectToAdd));
                    return subjectToAdd;
                } catch (Exception e) {
                    sqlSession.rollback();
                    logger.error(e.getMessage(), e);
                    throw new NoEntityCreatedException(EntityType.SUBJECT, subjectName);
                }
            }
        } else {
            throw new NoEntityCreatedException(EntityType.STUDENT);
        }
    }

    @Override
    public boolean assignSubjectToTeacher(long subjectId, long teacherId) {
        if (subjectId > 0 && teacherId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                ISubjectDAO subjectDAO = sqlSession.getMapper(ISubjectDAO.class);
                int result = 0;
                try {
                    result = subjectDAO.bindSubjectToTeacherId(subjectId, teacherId);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Subject (%d) assigned to teacher (%d) in the service",
                            subjectId, teacherId));
                    return true;
                } else {
                    logger.error(String.format("Subject (%d) couldn't be assigned to teacher (%d) in the service",
                            subjectId, teacherId));
                    return false;
                }
            }
        } else {
            logger.error("Subject couldn't be assigned to teacher in the service as one of its id is invalid");
            return false;
        }
    }

    @Override
    public boolean removeSubjectFromTeacher(long subjectId) {
        if (subjectId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                ISubjectDAO subjectDAO = sqlSession.getMapper(ISubjectDAO.class);
                int result = 0;
                try {
                    result = subjectDAO.removedTeacherFromSubject(subjectId);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Removed teacher from subject (%d) in the service. Subject doesn't have any " +
                            "teacher assigned", subjectId));
                    return true;
                } else {
                    logger.error(String.format("Subject (%d) couldn't be removed from teacher in the service", subjectId));
                    return false;
                }
            }
        } else {
            logger.error("Subject couldn't be removed from teacher in the service as subject's id is invalid");
            return false;
        }
    }

    @Override
    public boolean removeSubject(long id) {
        if (id > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                ISubjectDAO subjectDAO = sqlSession.getMapper(ISubjectDAO.class);
                int result = 0;
                try {
                    result = subjectDAO.removeEntity(id);
                    sqlSession.commit();
                } catch (Exception e) {
                    sqlSession.rollback();
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                if (result == 1) {
                    logger.debug(String.format("Subject (%d) removed from the service", id));
                    return true;
                } else {
                    logger.error(String.format("Subject (%d) couldn't be removed from the service", id));
                    return false;
                }
            }
        } else {
            logger.error("Subject couldn't be removed from the service as its id is invalid");
            return false;
        }
    }

    @Override
    public List<Grade> getGradesBySubject(long subjectId) throws EntityNotFoundException {
        List<Grade> allGradesBySubjectId = new ArrayList<>();
        if (subjectId > 0) {
            try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
                IGradeDAO gradeDAO = sqlSession.getMapper(IGradeDAO.class);
                allGradesBySubjectId = gradeDAO.getAllGradesBySubjectId(subjectId);
                logger.debug(String.format("Grades by subject (%s) retrieved from service", subjectId));
            }
        } else {
            logger.error("Grades by subject couldn't be retrieved from the service as subject's id is invalid");
        }
        return allGradesBySubjectId;
    }

    @Override
    public List<Subject> getSubjectsWithoutTeacher() {
        try (SqlSession sqlSession = SESSION_FACTORY.openSession()) {
            ISubjectDAO subjectDAO = sqlSession.getMapper(ISubjectDAO.class);
            return subjectDAO.getSubjectsWithoutTeacher();
        }
    }

}