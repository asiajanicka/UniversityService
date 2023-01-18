package org.example.dao.interfaces;

import org.example.model.Grade;

import java.util.List;

public interface IGradeDAO extends IBaseDAO<Grade> {

    List<Grade> getAllGradesByStudentId(long id);

    List<Grade> getAllGradesBySubjectId(long id);

    int bindGradeToStudentId(long gradeId, long studentId);

}
