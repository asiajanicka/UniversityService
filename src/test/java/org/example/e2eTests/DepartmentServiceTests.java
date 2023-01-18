package org.example.e2eTests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Building;
import org.example.model.Department;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.BuildingService;
import org.example.service.DepartmentService;
import org.example.service.SubjectService;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.junit.jupiter.api.Test;
import utils.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DepartmentServiceTests {

    private static final Logger logger = LogManager.getLogger(DepartmentServiceTests.class);

    @Test
    public void usecase1Test() throws NoEntityCreatedException, EntityNotFoundException {

//        Add a new teacher
//        Assign teacher to existing dept
//          - check if teacher is assigned to dept
//        Add a subject
//        Assign subject to teacher
//        Update teacher info
//          - check if info is updated and teacher has subject assigned
//        Remove teacher from dept
//          - check if teacher is removed from dept staff
//        Remove teacher from uni
//          - check if teacher is removed
//          - check if subject doesn't have teacher assigned
//        Remove subject (clean up)

        logger.info("Start of Department Service Tests - test case 1");
        DepartmentService deptService = new DepartmentService();
        SubjectService subjectService = new SubjectService();
        Teacher testTeacher = TestData.getBasicTeacher();

        Teacher actualTeacher = deptService.addTeacherWithoutSubjects(testTeacher);
        assertThat(actualTeacher.getFirstName()).isEqualTo(testTeacher.getFirstName());
        assertThat(actualTeacher.getLastName()).isEqualTo(testTeacher.getLastName());

        Department existingDept = deptService.getDeptById(1);
        assertThat(deptService.assignTeacherToDept(actualTeacher, existingDept)).isTrue();
        Department dept = deptService.getDeptById(1);
        assertThat(dept.getTeachers()).contains(actualTeacher);

        Subject subject = subjectService.addNewSubject("Organic Chemistry");
        subjectService.assignSubjectToTeacher(subject, actualTeacher);

        String newFName = "Mary";
        String newLName = "Violet";
        actualTeacher.setFirstName(newFName);
        actualTeacher.setLastName(newLName);
        assertThat(deptService.updateTeacherInfo(actualTeacher)).isTrue();

        Teacher updatedTeacher = deptService.getTeacherById(actualTeacher.getId());
        assertThat(updatedTeacher.getFirstName()).isEqualTo(newFName);
        assertThat(updatedTeacher.getLastName()).isEqualTo(newLName);
        assertThat(updatedTeacher.getSubjects()).contains(subject);

        assertThat(deptService.removeTeacherFromDept(updatedTeacher, dept)).isTrue();
        assertThat(deptService.getDeptById(1).getTeachers()).doesNotContain(updatedTeacher);

        assertThat(deptService.removeTeacher(updatedTeacher)).isTrue();
        assertThatThrownBy(() -> deptService.getTeacherById(updatedTeacher.getId())).isInstanceOf(EntityNotFoundException.class);
        assertThat(subjectService.getSubjectsWithoutTeacher()).contains(subject);

        subjectService.removeSubject(subject);
        logger.info("End of Department Service Tests - test case 1");
    }

    @Test
    public void usecase2Test() throws NoEntityCreatedException, EntityNotFoundException {

//        Add an empty dept
//        Assign dept to existing building
//          - check if dept is assigned to building
//        Add two new teachers (One, Two)
//        Assign teachers to the dept
//        Remove teacher One from uni
//          - check if teacher is removed from dept too
//        Remove dept (with remaining teacher Two) from uni
//          - check if dept is removed
//          - check if teacher Two is removed too
//          - check if dept is removed from the building

        logger.info("Start of Department Service Tests - test case 2");
        DepartmentService deptService = new DepartmentService();
        BuildingService buildingService = new BuildingService();

        String expectedDeptName = "Chemistry";

        Department actualDept = deptService.addEmptyDept(expectedDeptName);
        assertThat(actualDept.getName()).isEqualTo(expectedDeptName);
        Building basicBuilding = buildingService.getBasicBuildingById(1);

        buildingService.assignDeptToBuilding(actualDept, basicBuilding);
        assertThat(buildingService.getDeptsInBuilding(basicBuilding)).contains(actualDept);

        Teacher teacherOne = deptService.addTeacherWithoutSubjects(new Teacher("John", "One"));
        Teacher teacherTwo = deptService.addTeacherWithoutSubjects(new Teacher("Tom", "Two"));
        assertThat(deptService.assignTeacherToDept(teacherOne, actualDept)).isTrue();
        assertThat(deptService.assignTeacherToDept(teacherTwo, actualDept)).isTrue();

        assertThat(deptService.removeTeacher(teacherOne)).isTrue();
        List<Teacher> teachersByDeptId = deptService.getTeachersByDeptId(actualDept.getId());
        assertThat(teachersByDeptId).doesNotContain(teacherOne);
        assertThat(teachersByDeptId).contains(teacherTwo);

        assertThat(deptService.removeDept(actualDept)).isTrue();
        assertThatThrownBy(() -> deptService.getBasicDeptById(actualDept.getId())).isInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> deptService.getBasicTeacherById(teacherTwo.getId())).isInstanceOf(EntityNotFoundException.class);
        assertThat(buildingService.getDeptsInBuilding(basicBuilding)).doesNotContain(actualDept);
        logger.info("End of Department Service Tests - test case 2");
    }

}
