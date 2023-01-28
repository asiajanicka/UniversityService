package org.example.service.interfaces;

import org.example.model.Grade;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.List;

public interface ISubjectService {

    Subject getSubjectById(long id) throws EntityNotFoundException;

    Subject addNewSubject(String subjectName) throws NoEntityCreatedException;

    boolean assignSubjectToTeacher(long subjectId, long teacherId);

    boolean removeSubjectFromTeacher(long subjectId);

    boolean removeSubject(long id);

    List<Grade> getGradesBySubject(long subjectId) throws EntityNotFoundException;

    List<Subject> getSubjectsWithoutTeacher();

}