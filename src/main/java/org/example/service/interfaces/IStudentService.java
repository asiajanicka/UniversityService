package org.example.service.interfaces;

import org.example.model.*;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.GradeNotAssignedException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.List;

public interface IStudentService {

    Student getStudentById(long id) throws EntityNotFoundException;

    Student addNewStudent(Student student) throws NoEntityCreatedException;

    boolean removeStudent(long id);

    boolean updateStudentInfo(Student student);

    Grade addGradeToStudent(long studentId, Grade grade) throws NoEntityCreatedException, GradeNotAssignedException;

    List<Grade> getGradesByStudent(long id) throws EntityNotFoundException;

    boolean removeGrade(long id);

    boolean assignStudentToGroup(long studentId, long groupId);

    boolean removeStudentFromGroup(long studentId, long groupId);

    StudentGroup getStudentGroupById(long id) throws EntityNotFoundException;

    StudentGroup addEmptyStudentGroup(String name) throws NoEntityCreatedException;

    boolean removeStudentGroup(long id);

    List<StudentGroup> getGroupsAssignedToTimetableEntry(long ttEntryId) throws EntityNotFoundException;

}