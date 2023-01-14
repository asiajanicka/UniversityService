package org.example.e2eTests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Grade;
import org.example.model.Student;
import org.example.model.StudentGroup;
import org.example.service.PortalAccountService;
import org.example.service.StudentService;
import org.example.service.SubjectService;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.GradeNotAssignedException;
import org.example.service.exception.NoEntityCreatedException;
import org.junit.jupiter.api.Test;
import utils.TestData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StudentServiceTests {

    private static final Logger logger = LogManager.getLogger(StudentServiceTests.class);

    @Test
    public void test1() throws NoEntityCreatedException, EntityNotFoundException, GradeNotAssignedException {

//    Add an empty group
//    Add a new student
//	    - check if student and portal account are added
//    Assign student to group
//	    - check if student is added to group (read group)
//    Give student 3 grades
//	    - check if grades are assigned to student(read student)
//    Remove student from group
//	    - check if student is removed from group (read group)
//    Remove student from uni
//	    - check is student, portal account and grades are removed too
//    Remove group
//	    - check if group is removed from uni
//	    - check if group is removed from timetable (TBD when TimeTableService implemented) !!!!!

        logger.info("Start of Student Service Tests - test case 1");
        StudentService studentService = new StudentService();
        PortalAccountService accountService = new PortalAccountService();
        SubjectService subjectService = new SubjectService();
        Student expectedStudent = TestData.getFullStudent();
        String expectedGroupName = "group89";

        StudentGroup group = studentService.addEmptyStudentGroup(expectedGroupName);
        assertThat(group.getName()).isEqualTo(expectedGroupName);

        Student actualStudent = studentService.addNewStudent(expectedStudent);
        assertThat(studentService.getStudentById(actualStudent.getId())).isEqualTo(expectedStudent);

        assertThat(studentService.assignStudentToGroup(actualStudent, group)).isTrue();
        assertThat(studentService.getStudentGroupById(group.getId()).getStudents()).contains(actualStudent);

        List<Grade> gradesToAdd = new ArrayList<>();
        gradesToAdd.add(new Grade(5, subjectService.getSubjectById(1)));
        gradesToAdd.add(new Grade(4, subjectService.getSubjectById(1)));
        gradesToAdd.add(new Grade(3, subjectService.getSubjectById(2)));

        assertThat(studentService.addGradesToStudent(actualStudent, gradesToAdd)).containsExactlyInAnyOrderElementsOf(gradesToAdd);
        assertThat(studentService.getGradesByStudent(actualStudent)).containsExactlyInAnyOrderElementsOf(gradesToAdd);

        assertThat(studentService.removeStudentFromGroup(actualStudent, group)).isTrue();
        assertThat(studentService.getStudentGroupById(group.getId()).getStudents()).doesNotContain(actualStudent);

        assertThat(studentService.removeStudent(actualStudent)).isTrue();
        assertThatThrownBy(() -> studentService.getStudentById(actualStudent.getId())).isInstanceOf(EntityNotFoundException.class);
        long accountId = actualStudent.getPortalAccount().getId();
        assertThatThrownBy(() -> accountService.getAccountById(accountId)).isInstanceOf(EntityNotFoundException.class);
        assertThat(studentService.getGradesByStudent(actualStudent)).isEmpty();

        assertThat(studentService.removeStudentGroup(group)).isTrue();
        assertThatThrownBy(() -> studentService.getStudentGroupById(group.getId())).isInstanceOf(EntityNotFoundException.class);
        logger.info("End of Student Service Tests - test case 1");
    }

    @Test
    public void test2() throws NoEntityCreatedException, EntityNotFoundException {

//    Add a new student
//    Assign student to existing group
//    UpdateStudentInfo : change fname, lname, dateOfBirth
//	    - check if student is updated
//    Remove student from uni
//	    - check if student is removed from group too

        logger.info("Start of Student Service Tests - test case 2");
        StudentService studentService = new StudentService();
        Student student = TestData.getBasicStudent();

        Student expectedStudent = studentService.addNewStudent(student);
        StudentGroup group = studentService.getStudentGroupById(2);
        assertThat(studentService.assignStudentToGroup(expectedStudent, group)).isTrue();

        String newFirstName = "Tom";
        String newLastName = "Johnson";
        LocalDate newDateOfBirth = LocalDate.now().minusYears(18);

        expectedStudent.setFirstName(newFirstName);
        expectedStudent.setLastName(newLastName);
        expectedStudent.setDateOfBirth(newDateOfBirth);

        assertThat(studentService.updateStudentInfo(expectedStudent)).isTrue();
        Student actualStudent = studentService.getStudentById(expectedStudent.getId());
        assertThat(actualStudent.getFirstName()).isEqualTo(newFirstName);
        assertThat(actualStudent.getLastName()).isEqualTo(newLastName);
        assertThat(actualStudent.getDateOfBirth()).isEqualTo(newDateOfBirth);

        studentService.removeStudent(actualStudent);
        assertThat(studentService.getStudentGroupById(group.getId()).getStudents()).doesNotContain(actualStudent);
        logger.info("End of Student Service Tests - test case 1");
    }

}
