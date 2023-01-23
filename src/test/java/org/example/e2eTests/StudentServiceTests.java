package org.example.e2eTests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.WeekDay;
import org.example.model.*;
import org.example.service.*;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.GradeNotAssignedException;
import org.example.service.exception.NoEntityCreatedException;
import org.junit.jupiter.api.Test;
import utils.TestData;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StudentServiceTests {

    private static final Logger logger = LogManager.getLogger(StudentServiceTests.class);

    @Test
    public void usecase1Test() throws NoEntityCreatedException, EntityNotFoundException, GradeNotAssignedException {

//    Add an empty group
//    Add a new timetable entry for the group, existing subject and existing room
//        - check if group has tt entry assigned
//    Add a new student
//	    - check if student and portal account are added
//    Assign student to group
//	    - check if student is added to group (read group)
//    Give student a grade
//	    - check if grade is assigned to student(read student)
//    Remove student from group
//	    - check if student is removed from group (read group)
//    Remove student from uni
//	    - check is student, portal account and grade is removed too
//    Remove group
//	    - check if group is removed from uni
//	    - check if group is removed from timetable
//      - check if timetable entry is still available
//     Remove timetable entry (clean up)

        logger.info("Start of Student Service Tests - test case 1");
        StudentService studentService = new StudentService();
        PortalAccountService accountService = new PortalAccountService();
        SubjectService subjectService = new SubjectService();
        BuildingService buildingService = new BuildingService();
        Subject existingSubject = subjectService.getSubjectById(1);
        Room existingRoom = buildingService.getRoomById(1);
        TimetableService ttService = new TimetableService();
        Student testStudent = TestData.getFullStudent();
        String expectedGroupName = "group89";

        StudentGroup actualGroup = studentService.addEmptyStudentGroup(expectedGroupName);
        assertThat(actualGroup.getName()).isEqualTo(expectedGroupName);

        TimetableEntry ttEntry = new TimetableEntry(LocalTime.of(8, 0, 0), WeekDay.THURSDAY,
                existingSubject, existingRoom);
        TimetableEntry actualTTEntry = ttService.addNewTimetableEntry(ttEntry);
        ttService.assignTimetableEntryToGroup(actualTTEntry, actualGroup);

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

        assertThat(studentService.assignStudentToGroup(actualStudent, actualGroup)).isTrue();
        assertThat(studentService.getStudentGroupById(actualGroup.getId()).getStudents()).contains(actualStudent);

        Grade grade = new Grade(5, subjectService.getSubjectById(1));
        Grade actualGrade = studentService.addGradeToStudent(actualStudent, grade);
        assertThat(actualGrade.getValue()).isEqualTo(grade.getValue());
        assertThat(actualGrade.getSubject()).isEqualTo(grade.getSubject());
        assertThat(studentService.getGradesByStudent(actualStudent)).contains(actualGrade);

        assertThat(studentService.removeStudentFromGroup(actualStudent, actualGroup)).isTrue();
        assertThat(studentService.getStudentGroupById(actualGroup.getId()).getStudents()).doesNotContain(actualStudent);

        assertThat(studentService.removeStudent(actualStudent)).isTrue();
        assertThatThrownBy(() -> studentService.getStudentById(actualStudent.getId())).isInstanceOf(EntityNotFoundException.class);
        long accountId = actualStudent.getPortalAccount().getId();
        assertThatThrownBy(() -> accountService.getAccountById(accountId)).isInstanceOf(EntityNotFoundException.class);
        assertThat(studentService.getGradesByStudent(actualStudent)).isEmpty();

        assertThat(studentService.removeStudentGroup(actualGroup)).isTrue();
        StudentGroup finalActualGroup = actualGroup;
        assertThatThrownBy(() -> studentService.getStudentGroupById(finalActualGroup.getId())).isInstanceOf(EntityNotFoundException.class);

        assertThat(ttService.getTimetableForStudentGroup(actualGroup)).isEmpty();
        assertThat(studentService.getGroupsAssignedToTimetableEntry(actualTTEntry)).doesNotContain(actualGroup);
        assertThat(ttService.getTimetableEntryById(actualTTEntry.getId())).isEqualTo(actualTTEntry);

        ttService.removeTimetableEntry(actualTTEntry);
        logger.info("End of Student Service Tests - test case 1");
    }

    @Test
    public void usecase2Test() throws NoEntityCreatedException, EntityNotFoundException {

//    Add a new student
//    Assign student to existing group
//    UpdateStudentInfo : change fname, lname, dateOfBirth
//	    - check if student is updated
//    Remove student from uni
//	    - check if student is removed from group too

        logger.info("Start of Student Service Tests - test case 2");
        StudentService studentService = new StudentService();
        Student testStudent = TestData.getBasicStudent();

        Student actualStudent = studentService.addNewStudent(testStudent);
        StudentGroup existingGroup = studentService.getStudentGroupById(2);
        assertThat(studentService.assignStudentToGroup(actualStudent, existingGroup)).isTrue();

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

        studentService.removeStudent(updatedStudent);
        assertThat(studentService.getStudentGroupById(existingGroup.getId()).getStudents()).doesNotContain(updatedStudent);
        logger.info("End of Student Service Tests - test case 1");
    }

}
