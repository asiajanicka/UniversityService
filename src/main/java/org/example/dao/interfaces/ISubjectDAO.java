package org.example.dao.interfaces;

import org.example.model.Subject;

import java.util.List;

public interface ISubjectDAO extends IBaseDAO<Subject> {

    int bindSubjectToTeacherId(long subjectId, long teacherId);

    int removedTeacherFromSubject(long subjectId);

    List<Subject> getSubjectsByTeacherId(long teacherId);

    List<Subject> getSubjectsWithoutTeacher();

}
