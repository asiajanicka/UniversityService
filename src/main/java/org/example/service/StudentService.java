package org.example.service;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.*;
import org.example.dao.interfaces.*;
import org.example.enums.EntityType;
import org.example.model.*;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.GradeNotAssignedException;
import org.example.service.exception.NoEntityCreatedException;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class StudentService {

    private final IStudentGroupDAO groupDAO = new StudentGroupDAO();
    private final IStudentDAO studentDAO = new StudentDAO();
    private final IPortalAccountDAO accountDAO = new PortalAccountDAO();
    private final IGradeDAO gradeDAO = new GradeDAO();
    private final ISubjectDAO subjectDAO = new SubjectDAO();
    private static final Logger logger = LogManager.getLogger(StudentService.class);

    public Student getStudentById(long id) throws EntityNotFoundException {
        Student tempStudent = getBasicStudentById(id);
        tempStudent.setPortalAccount(getAccountByStudentId(id));
        tempStudent.setGrades(getGradesByStudent(tempStudent));
        return tempStudent;
    }

    public Student addNewStudent(Student student) throws NoEntityCreatedException {
        if (student != null) {
            Student tempStudent = addBasicStudent(student);
            if (student.getPortalAccount() != null) {
                PortalAccount tempAccount = addPortalAccount(student.getPortalAccount());
                accountDAO.bindAccountToStudentId(tempAccount.getId(), tempStudent.getId());
                tempStudent.setPortalAccount(tempAccount);
                logger.debug(String.format("Portal account assigned to student (%s) in the service", tempStudent));
            }
            List<Grade> tempGrades = new ArrayList<>();
            if (student.getGrades() != null && student.getGrades().size() > 0) {
                for (Grade grade : student.getGrades()) {
                    grade.setId(addGrade(grade).getId());
                    logger.debug(String.format("Grade (%s) added to the service", grade));
                    gradeDAO.bindGradeToStudentId(student.getId(), grade.getId());
                    logger.debug(String.format("Grade (%s) assigned to student (%s) in the service", grade, student));
                    tempGrades.add(grade);
                }
            }
            tempStudent.setGrades(tempGrades);
            return tempStudent;
        } else {
            logger.error("Student couldn't be added to the service as it is NULL");
            throw new NullPointerException("Student is NULL - can't add student to the service");
        }
    }

    public boolean removeStudent(Student student) {
        if (student != null) {
            int result = studentDAO.removeEntity(student.getId());
            if (result == 1) {
                logger.debug(String.format("Student (%s) removed from the service", student));
                return true;
            } else {
                logger.error(String.format("Student (%s) couldn't be removed from the service", student));
                return false;
            }
        } else {
            logger.error("Student couldn't be removed from the service as it is NULL");
            return false;
        }
    }

    public boolean updateStudentInfo(Student student) {
        if (student != null) {
            int result = studentDAO.updateEntity(student);
            if (result == 1) {
                logger.debug(String.format("Student (%s) updated in the service", student));
                return true;
            } else {
                logger.error(String.format("Student (%s) couldn't be updated in the service", student));
                return false;
            }
        } else {
            logger.error("Student couldn't be updated in the service as it is NULL");
            return false;
        }
    }

    public Grade addGradeToStudent(Student student, Grade grade) throws NoEntityCreatedException, GradeNotAssignedException {
        if (student != null && grade != null) {
            Grade tempGrade = addGrade(grade);
            int result = gradeDAO.bindGradeToStudentId(tempGrade.getId(), student.getId());
            if (result == 1) {
                tempGrade.setSubject(grade.getSubject());
                logger.debug(String.format("Grade (%s) assigned to student (%s) in the service", grade, student));
                return tempGrade;
            } else {
                logger.error(String.format("Grade (%s) couldn't be assigned to student (%s) in the service", grade, student));
                throw new GradeNotAssignedException(tempGrade, student);
            }
        } else {
            logger.error("Grade couldn't be assigned to student in the service as one of them is NULL");
            throw new NullPointerException("Student or grade is NULL - can't add grade to student in the service");
        }
    }

    public List<Grade> addGradesToStudent(Student student, List<Grade> grades) throws NoEntityCreatedException, GradeNotAssignedException {
        if (student != null && grades != null) {
            List<Grade> tempGrades = new ArrayList<>();
            for (Grade grade : grades) {
                tempGrades.add(addGradeToStudent(student, grade));
            }
            return tempGrades;
        } else {
            logger.error("Grades couldn't be assigned to student in the service as student or list of grades is NULL");
            throw new NullPointerException("Student or list of grades is NULL - can't add grades to student in the service");
        }
    }

    public List<Grade> getGradesByStudent(Student student) throws EntityNotFoundException {
        if (student != null) {
            List<Grade> allGradesByStudentId = gradeDAO.getAllGradesByStudentId(student.getId());
            for (Grade grade : allGradesByStudentId) {
                long subjectId = grade.getSubject().getId();
                Subject subject = subjectDAO
                        .getEntityById(subjectId)
                        .orElseThrow(() -> new EntityNotFoundException(EntityType.SUBJECT, subjectId));
                grade.setSubject(subject);
            }
            logger.debug(String.format("Grades of student (%s) retrieved from service", student.getId()));
            return allGradesByStudentId;
        } else {
            logger.error("Grades of student couldn't be retrieved from the service as student is NULL");
            throw new NullPointerException("Student is NULL - can't retrieve grades in the service");
        }
    }

    public boolean assignStudentToGroup(Student student, StudentGroup group) {
        if (student != null && group != null) {
            int result = studentDAO.bindStudentToGroupById(student.getId(), group.getId());
            if (result == 1) {
                logger.debug(String.format("Student (%s) assigned to group (%s) in the service", student, group));
                return true;
            } else {
                logger.error(String.format("Student (%s) couldn't be assigned to group (%s) in the service", student, group));
                return false;
            }
        } else {
            logger.error("Student couldn't be assigned to group in the service as one of them is NULL");
            return false;
        }
    }

    public boolean removeStudentFromGroup(Student student, StudentGroup group) {
        if (student != null && group != null) {
            int result = studentDAO.removeStudentFromGroupById(student.getId(), student.getId());
            if (result == 1) {
                logger.debug(String.format("Student (%s) removed from group (%s) in the service", student, group));
                return true;
            } else {
                logger.error(String.format("Student (%s) couldn't be removed from group (%s) in the service", student, group));
                return false;
            }
        } else {
            logger.error("Student couldn't be removed from group in the service as one of them is NULL");
            return false;
        }
    }

    public StudentGroup getStudentGroupById(long id) throws EntityNotFoundException {
        StudentGroup tempGroup = getBasicStudentGroupById(id);
        List<Student> allStudentByGroupId = studentDAO.getAllStudentsByGroupId(tempGroup.getId());
        for (Student student : allStudentByGroupId) {
            Student tempStudent = getStudentById(student.getId());
            student.setPortalAccount(tempStudent.getPortalAccount());
            student.setGrades(tempStudent.getGrades());
        }
        tempGroup.setStudents(allStudentByGroupId);
        logger.debug(String.format("All students from group (id: %d) retrieved from service", id));
        return tempGroup;
    }

    public StudentGroup addEmptyStudentGroup(String name) throws NoEntityCreatedException {
        StudentGroup tempGroup = groupDAO
                .createEntity(new StudentGroup(name))
                .orElseThrow(() -> new NoEntityCreatedException(EntityType.STUDENT_GROUP, name));
        logger.debug(String.format("Group (%s) added to the service", tempGroup));
        return tempGroup;
    }

    public boolean removeStudentGroup(StudentGroup group) {
        if (group != null) {
            int result = groupDAO.removeEntity(group.getId());
            if (result == 1) {
                logger.debug(String.format("Student group (%s) was removed from the service", group));
                return true;
            } else {
                logger.error(String.format("Student group (%s) couldn't be removed from the service", group));
                return false;
            }
        } else {
            logger.error("Student group couldn't be removed from the service as it is NULL");
            return false;
        }
    }

    public List<TimeTableEntry> getTimeTableByStudentGroup(StudentGroup group) {
        // to be implemented when TimeTableEntryDAO available
        return null;
    }

    private Student getBasicStudentById(long id) throws EntityNotFoundException {
        Student tempStudent = studentDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.STUDENT, id));
        logger.debug(String.format("Student (id: %d) retrieved from service", id));
        return tempStudent;
    }

    private Student addBasicStudent(Student student) throws NoEntityCreatedException {
        if (student != null) {
            Student tempStudent = studentDAO
                    .createEntity(student)
                    .orElseThrow(() -> new NoEntityCreatedException(EntityType.STUDENT, student));
            logger.debug(String.format("Student (%s) added to the service", tempStudent));
            return tempStudent;
        } else {
            logger.error("Student couldn't be added to service as it is NULL");
            throw new NullPointerException("Student is NULL - can't add it to service");
        }
    }

    private PortalAccount addPortalAccount(PortalAccount account) throws NoEntityCreatedException {
        if (account != null) {
            PortalAccount tempAccount = accountDAO
                    .createEntity(account)
                    .orElseThrow(() -> new NoEntityCreatedException(EntityType.PORTAL_ACCOUNT, account));
            logger.debug(String.format("Portal account %s added to the service", tempAccount));
            return tempAccount;
        } else {
            logger.error("Portal account couldn't be added to service as it is NULL");
            throw new NullPointerException("Portal account is NULL - can't add it to service");
        }
    }

    private Grade addGrade(Grade grade) throws NoEntityCreatedException {
        if (grade != null) {
            Grade tempGrade = gradeDAO
                    .createEntity(grade)
                    .orElseThrow(() -> new NoEntityCreatedException(EntityType.GRADE, grade));
            tempGrade.getSubject().setName(grade.getSubject().getName());
            logger.debug(String.format("Grade (%s) added to the service", grade));
            return tempGrade;
        } else {
            logger.error("Grade couldn't be added to service as it is NULL");
            throw new NullPointerException("Grade is NULL - can't add it to service");
        }
    }

    private PortalAccount getAccountByStudentId(long studentId) {
        PortalAccount tempAccount = accountDAO
                .getAccountByStudentId(studentId)
                .orElseGet(() -> {
                    logger.error(String.format("Portal account of student (id: %d) couldn't be retrieved from service",
                            studentId));
                    return null;
                });
        logger.debug(String.format("Portal account of student (id: %d) retrieved from service", studentId));
        return tempAccount;
    }

    private StudentGroup getBasicStudentGroupById(long id) throws EntityNotFoundException {
        StudentGroup tempGroup = groupDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.STUDENT_GROUP, id));
        logger.debug(String.format("Student group (id: %d) retrieved from service", id));
        return tempGroup;
    }

}
