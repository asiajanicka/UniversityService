package org.example.service.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.jdbc.GradeDAO;
import org.example.dao.jdbc.SubjectDAO;
import org.example.dao.interfaces.IGradeDAO;
import org.example.dao.interfaces.ISubjectDAO;
import org.example.enums.EntityType;
import org.example.model.Grade;
import org.example.model.Subject;
import org.example.model.Teacher;
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
    public boolean assignSubjectToTeacher(Subject subject, Teacher teacher) {
        if (subject != null && teacher != null) {
            int result = subjectDAO.bindSubjectToTeacherId(subject.getId(), teacher.getId());
            if (result == 1) {
                logger.debug(String.format("Subject (%s) assigned to teacher (%s) in the service",
                        subject, teacher));
                return true;
            } else {
                logger.error(String.format("Subject (%s) couldn't be assigned to teacher (%s) in the service",
                        subject, teacher));
                return false;
            }
        } else {
            logger.error("Subject couldn't be assigned to teacher in the service as one of them is NULL");
            return false;
        }
    }

    @Override
    public boolean removeSubjectFromTeacher(Subject subject) {
        if (subject != null) {
            int result = subjectDAO.removedTeacherFromSubject(subject.getId());
            if (result == 1) {
                logger.debug(String.format("Removed teacher from subject (%s) in the service. Subject doesn't have any " +
                        "teacher assigned", subject));
                return true;
            } else {
                logger.error(String.format("Subject (%s) couldn't be removed from teacher in the service", subject));
                return false;
            }
        } else {
            logger.error("Subject couldn't be removed from teacher in the service as subject is NULL");
            return false;
        }
    }

    @Override
    public boolean removeSubject(Subject subject) {
        if (subject != null) {
            int result = subjectDAO.removeEntity(subject.getId());
            if (result == 1) {
                logger.debug(String.format("Subject (%s) removed from the service", subject));
                return true;
            } else {
                logger.error(String.format("Subject (%s) couldn't be removed from the service", subject));
                return false;
            }
        } else {
            logger.error("Subject couldn't be removed from the service as it is NULL");
            return false;
        }
    }

    @Override
    public List<Grade> getGradesBySubject(Subject subject) throws EntityNotFoundException {
        List<Grade> allGradesBySubjectId = new ArrayList<>();
        if (subject != null) {
            allGradesBySubjectId = gradeDAO.getAllGradesBySubjectId(subject.getId());
            for (Grade grade : allGradesBySubjectId) {
                long subjectId = grade.getSubject().getId();
                Subject tempSubject = subjectDAO
                        .getEntityById(subjectId)
                        .orElseThrow(() -> new EntityNotFoundException(EntityType.SUBJECT, subjectId));
                grade.setSubject(tempSubject);
            }
            logger.debug(String.format("Grades by subject (%s) retrieved from service", subject));
        } else {
            logger.error("Grades by subject couldn't be retrieved from the service as subject is NULL");
        }
        return allGradesBySubjectId;
    }

    @Override
    public List<Subject> getSubjectsWithoutTeacher() {
        return subjectDAO.getSubjectsWithoutTeacher();
    }

}