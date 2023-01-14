package org.example.dao.interfaces;

import org.example.model.Subject;

public interface ISubjectDAO extends IBaseDAO<Subject> {

    int bindSubjectToTeacherId(long subjectId, long teacherId);

}
