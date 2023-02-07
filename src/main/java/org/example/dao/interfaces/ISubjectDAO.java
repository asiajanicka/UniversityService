package org.example.dao.interfaces;

import org.apache.ibatis.annotations.Param;
import org.example.model.Subject;

import java.util.List;

public interface ISubjectDAO extends IBaseDAO<Subject> {

    int bindSubjectToTeacherId(@Param("subjectId") long subjectId, @Param("teacherId") long teacherId);

    int removedTeacherFromSubject(@Param("subjectId") long subjectId);

    List<Subject> getSubjectsByTeacherId(@Param("teacherId") long teacherId);

    List<Subject> getSubjectsWithoutTeacher();

}
