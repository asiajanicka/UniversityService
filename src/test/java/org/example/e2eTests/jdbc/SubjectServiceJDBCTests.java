package org.example.e2eTests.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Grade;
import org.example.model.Student;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.GradeNotAssignedException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IDepartmentService;
import org.example.service.interfaces.IStudentService;
import org.example.service.interfaces.ISubjectService;
import org.example.service.jdbc.DepartmentService;
import org.example.service.jdbc.StudentService;
import org.example.service.jdbc.SubjectService;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubjectServiceJDBCTests {

    private static final Logger logger = LogManager.getLogger(SubjectServiceJDBCTests.class);

    @Test()
    public void usecase1Test() throws EntityNotFoundException, NoEntityCreatedException, GradeNotAssignedException {

        logger.info("Start of Subject Service JDBC Tests - test case 1");
        ISubjectService subjectService = new SubjectService();
        IDepartmentService teacherService = new DepartmentService();
        IStudentService studentService = new StudentService();
        Teacher teacherOne = teacherService.getTeacherById(1);
        Teacher teacherTwo = teacherService.getTeacherById(2);

        String expectedSubjectName = "Algorithms and Data Structures";
        Subject actualSubject = subjectService.addNewSubject(expectedSubjectName);
        assertThat(actualSubject.getName()).isEqualTo(expectedSubjectName);

        assertThat(subjectService.assignSubjectToTeacher(actualSubject.getId(), teacherOne.getId())).isTrue();
        assertThat(teacherService.getAllSubjectsByTeacher(teacherOne.getId())).contains(actualSubject);

        assertThat(subjectService.removeSubjectFromTeacher(actualSubject.getId())).isTrue();
        assertThat(teacherService.getAllSubjectsByTeacher(teacherOne.getId())).doesNotContain(actualSubject);

        subjectService.assignSubjectToTeacher(actualSubject.getId(), teacherTwo.getId());
        Grade grade = new Grade(5, actualSubject);
        Student actualStudent = studentService.getStudentById(1);
        Grade actualGrade = studentService.addGradeToStudent(actualStudent.getId(), grade);

        assertThat(subjectService.getGradesBySubject(actualSubject.getId())).contains(actualGrade);

        assertThat(subjectService.removeSubject(actualSubject.getId())).isTrue();
        assertThatThrownBy(() -> subjectService.getSubjectById(actualSubject.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        assertThat(studentService
                .getGradesByStudent(actualStudent.getId())
                .stream()
                .filter(p -> p.getId() == actualGrade.getId())
                .collect(Collectors.toList())).isEmpty();

        assertThat(teacherService.getAllSubjectsByTeacher(teacherTwo.getId())).doesNotContain(actualSubject);
        logger.info("End of Subject Service JDBC Tests - test case 1");
    }

}