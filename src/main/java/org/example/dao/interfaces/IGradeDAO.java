package org.example.dao.interfaces;

import org.apache.ibatis.annotations.Param;
import org.example.model.Grade;

import java.util.List;

public interface IGradeDAO extends IBaseDAO<Grade> {

    List<Grade> getAllGradesByStudentId(long id);

    List<Grade> getAllGradesBySubjectId(long id);

    int bindGradeToStudentId(@Param("gradeId") long gradeId, @Param("studentId") long studentId);

}
