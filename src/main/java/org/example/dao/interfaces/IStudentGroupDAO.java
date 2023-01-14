package org.example.dao.interfaces;

import org.example.model.StudentGroup;

import java.util.List;

public interface IStudentGroupDAO extends IBaseDAO<StudentGroup> {

    List<StudentGroup> getAllGroups();

}
