package org.example.dao.interfaces;

import org.example.model.Student;

import java.util.List;

public interface IStudentDAO extends IBaseDAO<Student> {

    List<Student> getAllStudentsByGroupId(long groupId);

    int bindStudentToGroupById(long studentId, long groupId);

    int removeStudentFromGroupById(long studentId, long groupId);

}
