package org.example.e2eTests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Grade;
import org.example.model.Student;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.DepartmentService;
import org.example.service.StudentService;
import org.example.service.SubjectService;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.GradeNotAssignedException;
import org.example.service.exception.NoEntityCreatedException;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubjectServiceTests {

    private static final Logger logger = LogManager.getLogger(SubjectServiceTests.class);

    @Test()
    public void usecase1Test() throws EntityNotFoundException, NoEntityCreatedException, GradeNotAssignedException {

//        Add new subject
//        Assign subject to existing teacher - teacherOne
//        Read teacher's subjects
//          - check if subject is added to teacher's subject list
//        Remove subject from teacherOne
//        Read teacher's subjects again
//          - check if subject is removed from teacher's subject list
//        Assign subject to another existing teacher - teacherTwo
//        Add grade to existing student from the subject
//        Remove subject from uni
//          - check if subject is removed from list
//          - check if grades are removed
//          - check if subject is removed from teacherTwo's subject list
//          - check if timetable entries for subject are removed

        logger.info("Start of Subject Service Tests - test case 1");
        SubjectService subjectService = new SubjectService();
        DepartmentService teacherService = new DepartmentService();
        StudentService studentService = new StudentService();
        Teacher teacherOne = teacherService.getTeacherById(1);
        Teacher teacherTwo = teacherService.getTeacherById(2);

        String expectedSubjectName = "Algorithms and Data Structures";
        Subject actualSubject = subjectService.addNewSubject(expectedSubjectName);
        assertThat(actualSubject.getName()).isEqualTo(expectedSubjectName);

        assertThat(subjectService.assignSubjectToTeacher(actualSubject, teacherOne)).isTrue();
        assertThat(teacherService.getAllSubjectsByTeacher(teacherOne)).contains(actualSubject);

        assertThat(subjectService.removeSubjectFromTeacher(actualSubject)).isTrue();
        assertThat(teacherService.getAllSubjectsByTeacher(teacherOne)).doesNotContain(actualSubject);

        subjectService.assignSubjectToTeacher(actualSubject, teacherTwo);
        Grade grade = new Grade(5, actualSubject);
        Student actualStudent = studentService.getStudentById(1);
        Grade actualGrade = studentService.addGradeToStudent(actualStudent, grade);

        assertThat(subjectService.getGradesBySubject(actualSubject)).contains(actualGrade);

        assertThat(subjectService.removeSubject(actualSubject)).isTrue();
        assertThatThrownBy(() -> subjectService.getSubjectById(actualSubject.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        assertThat(studentService
                .getGradesByStudent(actualStudent)
                .stream()
                .filter(p -> p.getId() == actualGrade.getId())
                .collect(Collectors.toList())).isEmpty();

        assertThat(teacherService.getAllSubjectsByTeacher(teacherTwo)).doesNotContain(actualSubject);
        // check timetable entries
        logger.info("End of Subject Service Tests - test case 1");
    }

}
