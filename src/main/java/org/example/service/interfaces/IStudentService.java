package org.example.service.interfaces;

import org.example.model.*;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.GradeNotAssignedException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.List;

public interface IStudentService {

    Student getStudentById(long id) throws EntityNotFoundException;

    Student addNewStudent(Student student) throws NoEntityCreatedException;

    boolean removeStudent(Student student);

    boolean updateStudentInfo(Student student);

    Grade addGradeToStudent(Student student, Grade grade) throws NoEntityCreatedException, GradeNotAssignedException;

    List<Grade> addGradesToStudent(Student student, List<Grade> grades) throws NoEntityCreatedException, GradeNotAssignedException;

    List<Grade> getGradesByStudent(Student student) throws EntityNotFoundException;

    boolean removeGrade(Grade grade);

    boolean assignStudentToGroup(Student student, StudentGroup group);

    boolean removeStudentFromGroup(Student student, StudentGroup group);

    StudentGroup getStudentGroupById(long id) throws EntityNotFoundException;

    StudentGroup addEmptyStudentGroup(String name) throws NoEntityCreatedException;

    boolean removeStudentGroup(StudentGroup group);

    List<StudentGroup> getGroupsAssignedToTimetableEntry(TimetableEntry ttEntry) throws EntityNotFoundException;

    PortalAccount getAccountByStudent(Student student);

}