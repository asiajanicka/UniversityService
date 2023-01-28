package org.example.service.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IGradeDAO;
import org.example.dao.interfaces.ISubjectDAO;
import org.example.dao.jdbc.GradeDAO;
import org.example.dao.jdbc.SubjectDAO;
import org.example.enums.EntityType;
import org.example.model.Grade;
import org.example.model.Subject;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.ISubjectService;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class SubjectService implements ISubjectService {

    private final ISubjectDAO subjectDAO = new SubjectDAO();
    private final IGradeDAO gradeDAO = new GradeDAO();
    private static final Logger logger = LogManager.getLogger(SubjectService.class);

    @Override
    public Subject getSubjectById(long id) throws EntityNotFoundException {
        Subject tempSubject = subjectDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.SUBJECT, id));
        logger.debug(String.format("Subject with %d retrieved from service", id));
        return tempSubject;
    }

    @Override
    public Subject addNewSubject(String subjectName) throws NoEntityCreatedException {
        Subject tempSubject = subjectDAO
                .createEntity(new Subject(subjectName))
                .orElseThrow(() -> new NoEntityCreatedException(EntityType.SUBJECT, subjectName));
        logger.debug(String.format("Portal account %s added to the service", tempSubject));
        return tempSubject;
    }

    @Override
    public boolean assignSubjectToTeacher(long subjectId, long teacherId) {
        if (subjectId > 0 && teacherId > 0) {
            int result = subjectDAO.bindSubjectToTeacherId(subjectId, teacherId);
            if (result == 1) {
                logger.debug(String.format("Subject (%d) assigned to teacher (%d) in the service",
                        subjectId, teacherId));
                return true;
            } else {
                logger.error(String.format("Subject (%d) couldn't be assigned to teacher (%d) in the service",
                        subjectId, teacherId));
                return false;
            }
        } else {
            logger.error("Subject couldn't be assigned to teacher in the service as one of its id is invalid");
            return false;
        }
    }

    @Override
    public boolean removeSubjectFromTeacher(long subjectId) {
        if (subjectId > 0) {
            int result = subjectDAO.removedTeacherFromSubject(subjectId);
            if (result == 1) {
                logger.debug(String.format("Removed teacher from subject (%d) in the service. Subject doesn't have any " +
                        "teacher assigned", subjectId));
                return true;
            } else {
                logger.error(String.format("Subject (%d) couldn't be removed from teacher in the service", subjectId));
                return false;
            }
        } else {
            logger.error("Subject couldn't be removed from teacher in the service as subject's id is invalid");
            return false;
        }
    }

    @Override
    public boolean removeSubject(long id) {
        if (id > 0) {
            int result = subjectDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Subject (%d) removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Subject (%d) couldn't be removed from the service", id));
                return false;
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
            allGradesBySubjectId = gradeDAO.getAllGradesBySubjectId(subjectId);
            for (Grade grade : allGradesBySubjectId) {
                grade.setSubject(getSubjectById(subjectId));
            }
            logger.debug(String.format("Grades by subject (%d) retrieved from service", subjectId));
        } else {
            logger.error("Grades by subject couldn't be retrieved from the service as subject's id is invalid");
        }
        return allGradesBySubjectId;
    }

    @Override
    public List<Subject> getSubjectsWithoutTeacher() {
        return subjectDAO.getSubjectsWithoutTeacher();
    }

}