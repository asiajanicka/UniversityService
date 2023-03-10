package org.example.e2eTests.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.WeekDay;
import org.example.model.*;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.GradeNotAssignedException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.*;
import org.example.service.jdbc.*;
import org.junit.jupiter.api.Test;
import utils.TestData;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StudentServiceJDBCTests {

    private static final Logger logger = LogManager.getLogger(StudentServiceJDBCTests.class);

    @Test
    public void usecase1Test() throws NoEntityCreatedException, EntityNotFoundException, GradeNotAssignedException {

        logger.info("Start of Student Service JDBC Tests - test case 1");
        IStudentService studentService = new StudentService();
        IPortalAccountService accountService = new PortalAccountService();
        ISubjectService subjectService = new SubjectService();
        IBuildingService buildingService = new BuildingService();
        ITimetableService ttService = new TimetableService();
        Subject existingSubject = subjectService.getSubjectById(1);
        Room existingRoom = buildingService.getRoomById(1);
        Student testStudent = TestData.getFullStudent();
        String expectedGroupName = "group89";

        StudentGroup actualGroup = studentService.addEmptyStudentGroup(expectedGroupName);
        assertThat(actualGroup.getName()).isEqualTo(expectedGroupName);

        TimetableEntry ttEntry = new TimetableEntry(LocalTime.of(8, 0, 0), WeekDay.THURSDAY,
                existingSubject, existingRoom);
        TimetableEntry actualTTEntry = ttService.addNewTimetableEntry(ttEntry);
        ttService.assignTimetableEntryToGroup(actualTTEntry.getId(), actualGroup.getId());

        actualGroup = studentService.getStudentGroupById(actualGroup.getId());
        assertThat(actualGroup.getTimetable()).contains(actualTTEntry);

        Student actualStudent = studentService.addNewStudent(testStudent);
        assertThat(actualStudent.getFirstName()).isEqualTo(testStudent.getFirstName());
        assertThat(actualStudent.getLastName()).isEqualTo(testStudent.getLastName());
        assertThat(actualStudent.getDateOfBirth()).isEqualTo(testStudent.getDateOfBirth());
        assertThat(actualStudent.getPortalAccount().getLogin()).isEqualTo(testStudent.getPortalAccount().getLogin());
        assertThat(actualStudent.getPortalAccount().getPassword()).isEqualTo(testStudent.getPortalAccount().getPassword());
        assertThat(actualStudent.getPortalAccount().getIssueDate()).isEqualTo(testStudent.getPortalAccount().getIssueDate());
        assertThat(actualStudent.getPortalAccount().getExpiryDate()).isEqualTo(testStudent.getPortalAccount().getExpiryDate());

        assertThat(studentService.getStudentById(actualStudent.getId())).isEqualTo(actualStudent);

        assertThat(studentService.assignStudentToGroup(actualStudent.getId(), actualGroup.getId())).isTrue();
        assertThat(studentService.getStudentGroupById(actualGroup.getId()).getStudents()).contains(actualStudent);

        Grade grade = new Grade(5, subjectService.getSubjectById(1));
        Grade actualGrade = studentService.addGradeToStudent(actualStudent.getId(), grade);
        assertThat(actualGrade.getValue()).isEqualTo(grade.getValue());
        assertThat(actualGrade.getSubject()).isEqualTo(grade.getSubject());
        assertThat(studentService.getGradesByStudent(actualStudent.getId())).contains(actualGrade);

        assertThat(studentService.removeStudentFromGroup(actualStudent.getId(), actualGroup.getId())).isTrue();
        assertThat(studentService.getStudentGroupById(actualGroup.getId()).getStudents()).doesNotContain(actualStudent);

        assertThat(studentService.removeStudent(actualStudent.getId())).isTrue();
        assertThatThrownBy(() -> studentService.getStudentById(actualStudent.getId())).isInstanceOf(EntityNotFoundException.class);
        long accountId = actualStudent.getPortalAccount().getId();
        assertThatThrownBy(() -> accountService.getAccountById(accountId)).isInstanceOf(EntityNotFoundException.class);
        assertThat(studentService.getGradesByStudent(actualStudent.getId())).isEmpty();

        assertThat(studentService.removeStudentGroup(actualGroup.getId())).isTrue();
        StudentGroup finalActualGroup = actualGroup;
        assertThatThrownBy(() -> studentService.getStudentGroupById(finalActualGroup.getId())).isInstanceOf(EntityNotFoundException.class);

        assertThat(ttService.getTimetableForStudentGroup(actualGroup.getId())).isEmpty();
        assertThat(studentService.getGroupsAssignedToTimetableEntry(actualTTEntry.getId())).doesNotContain(actualGroup);
        assertThat(ttService.getTimetableEntryById(actualTTEntry.getId())).isEqualTo(actualTTEntry);

        ttService.removeTimetableEntry(actualTTEntry.getId());
        logger.info("End of Student Service JDBC Tests - test case 1");
    }

    @Test
    public void usecase2Test() throws NoEntityCreatedException, EntityNotFoundException {

        logger.info("Start of Student Service JDBC Tests - test case 2");
        IStudentService studentService = new StudentService();
        Student testStudent = TestData.getBasicStudent();

        Student actualStudent = studentService.addNewStudent(testStudent);
        StudentGroup existingGroup = studentService.getStudentGroupById(2);
        assertThat(studentService.assignStudentToGroup(actualStudent.getId(), existingGroup.getId())).isTrue();

        String newFirstName = "Tom";
        String newLastName = "Johnson";
        LocalDate newDateOfBirth = LocalDate.now().minusYears(18);

        actualStudent.setFirstName(newFirstName);
        actualStudent.setLastName(newLastName);
        actualStudent.setDateOfBirth(newDateOfBirth);

        assertThat(studentService.updateStudentInfo(actualStudent)).isTrue();
        Student updatedStudent = studentService.getStudentById(actualStudent.getId());
        assertThat(updatedStudent.getFirstName()).isEqualTo(newFirstName);
        assertThat(updatedStudent.getLastName()).isEqualTo(newLastName);
        assertThat(updatedStudent.getDateOfBirth()).isEqualTo(newDateOfBirth);

        studentService.removeStudent(updatedStudent.getId());
        assertThat(studentService.getStudentGroupById(existingGroup.getId()).getStudents()).doesNotContain(updatedStudent);
        logger.info("End of Student Service JDBC Tests - test case 1");
    }

}