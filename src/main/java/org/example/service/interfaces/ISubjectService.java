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

    boolean assignSubjectToTeacher(Subject subject, Teacher teacher);

    boolean removeSubjectFromTeacher(Subject subject);

    boolean removeSubject(Subject subject);

    List<Grade> getGradesBySubject(Subject subject) throws EntityNotFoundException;

    List<Subject> getSubjectsWithoutTeacher();

}