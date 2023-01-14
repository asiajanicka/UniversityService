package org.example.service;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.GradeDAO;
import org.example.dao.interfaces.IGradeDAO;
import org.example.dao.interfaces.ISubjectDAO;
import org.example.dao.SubjectDAO;
import org.example.enums.EntityType;
import org.example.model.*;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.List;

@NoArgsConstructor
public class SubjectService {

    private final ISubjectDAO subjectDAO = new SubjectDAO();
    private final IGradeDAO gradeDAO = new GradeDAO();
    private static final Logger logger = LogManager.getLogger(SubjectService.class);

    public Subject getSubjectById(long id) throws EntityNotFoundException {
        Subject tempSubject = subjectDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.SUBJECT, id));
        logger.debug(String.format("Subject with %d retrieved from service", id));
        return tempSubject;
    }

    public Subject addNewSubject(Subject subject) throws NoEntityCreatedException {
        if(subject != null) {
            Subject tempSubject = subjectDAO
                    .createEntity(subject)
                    .orElseThrow(() -> new NoEntityCreatedException(EntityType.SUBJECT, subject));
            logger.debug(String.format("Portal account %s added to the service", tempSubject));
            return tempSubject;
        } else {
            logger.error("Subject couldn't be added to the service as it is NULL");
            throw new NullPointerException("Subject is NULL - can't add subject to the service");
        }
    }


    public boolean bindSubjectToTeacher(Subject subject, Teacher teacher) {
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

    public List<Grade> getGradesBySubject(Subject subject) throws EntityNotFoundException {
        if (subject != null) {
            List<Grade> allGradesBySubjectId = gradeDAO.getAllGradesBySubjectId(subject.getId());
            for (Grade grade : allGradesBySubjectId) {
                long subjectId = grade.getSubject().getId();
                Subject tempSubject = subjectDAO
                        .getEntityById(subjectId)
                        .orElseThrow(() -> new EntityNotFoundException(EntityType.SUBJECT, subjectId));
                grade.setSubject(tempSubject);
            }
            logger.debug(String.format("Grades by subject (%s) retrieved from service", subject));
            return allGradesBySubjectId;
        } else {
            logger.error("Grades by subject couldn't be retrieved from the service as subject is NULL");
            throw new NullPointerException("Subject is NULL - can't retrieve grades in the service");
        }
    }

}
