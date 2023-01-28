package org.example.e2eTests.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Building;
import org.example.model.Department;
import org.example.model.Subject;
import org.example.model.Teacher;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IBuildingService;
import org.example.service.interfaces.IDepartmentService;
import org.example.service.interfaces.ISubjectService;
import org.example.service.jdbc.BuildingService;
import org.example.service.jdbc.DepartmentService;
import org.example.service.jdbc.SubjectService;
import org.junit.jupiter.api.Test;
import utils.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DepartmentServiceJDBCTests {

    private static final Logger logger = LogManager.getLogger(DepartmentServiceJDBCTests.class);

    @Test
    public void usecase1Test() throws NoEntityCreatedException, EntityNotFoundException {

        logger.info("Start of Department Service JDBC Tests - test case 1");
        IDepartmentService deptService = new DepartmentService();
        ISubjectService subjectService = new SubjectService();
        Teacher testTeacher = TestData.getBasicTeacher();

        Teacher actualTeacher = deptService.addTeacherWithoutSubjects(testTeacher);
        assertThat(actualTeacher.getFirstName()).isEqualTo(testTeacher.getFirstName());
        assertThat(actualTeacher.getLastName()).isEqualTo(testTeacher.getLastName());

        Department existingDept = deptService.getDeptById(1);
        assertThat(deptService.assignTeacherToDept(actualTeacher.getId(), existingDept.getId())).isTrue();
        Department dept = deptService.getDeptById(1);
        assertThat(dept.getTeachers()).contains(actualTeacher);

        Subject subject = subjectService.addNewSubject("Organic Chemistry");
        subjectService.assignSubjectToTeacher(subject.getId(), actualTeacher.getId());

        String newFName = "Mary";
        String newLName = "Violet";
        actualTeacher.setFirstName(newFName);
        actualTeacher.setLastName(newLName);
        assertThat(deptService.updateTeacherInfo(actualTeacher)).isTrue();

        Teacher updatedTeacher = deptService.getTeacherById(actualTeacher.getId());
        assertThat(updatedTeacher.getFirstName()).isEqualTo(newFName);
        assertThat(updatedTeacher.getLastName()).isEqualTo(newLName);
        assertThat(updatedTeacher.getSubjects()).contains(subject);

        assertThat(deptService.removeTeacherFromDept(updatedTeacher.getId(), dept.getId())).isTrue();
        assertThat(deptService.getDeptById(1).getTeachers()).doesNotContain(updatedTeacher);

        assertThat(deptService.removeTeacher(updatedTeacher.getId())).isTrue();
        assertThatThrownBy(() -> deptService.getTeacherById(updatedTeacher.getId())).isInstanceOf(EntityNotFoundException.class);
        assertThat(subjectService.getSubjectsWithoutTeacher()).contains(subject);

        subjectService.removeSubject(subject.getId());
        logger.info("End of Department Service JDBC Tests - test case 1");
    }

    @Test
    public void usecase2Test() throws NoEntityCreatedException, EntityNotFoundException {

        logger.info("Start of Department Service JDBC Tests - test case 2");
        IDepartmentService deptService = new DepartmentService();
        IBuildingService buildingService = new BuildingService();

        String expectedDeptName = "Chemistry";

        Department actualDept = deptService.addEmptyDept(expectedDeptName);
        assertThat(actualDept.getName()).isEqualTo(expectedDeptName);
        Building basicBuilding = buildingService.getBuildingById(1);

        buildingService.assignDeptToBuilding(actualDept.getId(), basicBuilding.getId());
        assertThat(buildingService.getDeptsInBuilding(basicBuilding.getId())).contains(actualDept);

        Teacher teacherOne = deptService.addTeacherWithoutSubjects(new Teacher("John", "One"));
        Teacher teacherTwo = deptService.addTeacherWithoutSubjects(new Teacher("Tom", "Two"));
        assertThat(deptService.assignTeacherToDept(teacherOne.getId(), actualDept.getId())).isTrue();
        assertThat(deptService.assignTeacherToDept(teacherTwo.getId(), actualDept.getId())).isTrue();

        assertThat(deptService.removeTeacher(teacherOne.getId())).isTrue();
        List<Teacher> teachersByDeptId = deptService.getTeachersByDeptId(actualDept.getId());
        assertThat(teachersByDeptId).doesNotContain(teacherOne);
        assertThat(teachersByDeptId).contains(teacherTwo);

        assertThat(deptService.removeDept(actualDept.getId())).isTrue();
        assertThatThrownBy(() -> deptService.getDeptById(actualDept.getId())).isInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> deptService.getTeacherById(teacherTwo.getId())).isInstanceOf(EntityNotFoundException.class);
        assertThat(buildingService.getDeptsInBuilding(basicBuilding.getId())).doesNotContain(actualDept);
        logger.info("End of Department Service JDBC Tests - test case 2");
    }

}