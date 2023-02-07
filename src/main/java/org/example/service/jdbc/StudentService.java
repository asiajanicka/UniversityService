package org.example.service.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.*;
import org.example.dao.jdbc.*;
import org.example.enums.EntityType;
import org.example.model.Grade;
import org.example.model.PortalAccount;
import org.example.model.Student;
import org.example.model.StudentGroup;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.GradeNotAssignedException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IStudentService;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class StudentService implements IStudentService {

    private final IStudentGroupDAO groupDAO = new StudentGroupDAO();
    private final IStudentDAO studentDAO = new StudentDAO();
    private final IPortalAccountDAO accountDAO = new PortalAccountDAO();
    private final IGradeDAO gradeDAO = new GradeDAO();
    private final IGroupsHasTimetableEntriesDAO groupsHasTTEntriesDAO = new GroupsHasTimetableEntriesDAO();
    private final SubjectService subjectService = new SubjectService();
    private final TimetableService timetableService = new TimetableService();
    private static final Logger logger = LogManager.getLogger(StudentService.class);

    @Override
    public Student getStudentById(long id) throws EntityNotFoundException {
        Student tempStudent = getBasicStudentById(id);
        tempStudent.setPortalAccount(getAccountByStudent(id));
        tempStudent.setGrades(getGradesByStudent(id));
        return tempStudent;
    }

    @Override
    public Student addNewStudent(Student student) throws NoEntityCreatedException {
        if (student != null) {
            if (student.getFirstName() != null && student.getLastName() != null) {
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
                throw new NoEntityCreatedException(EntityType.STUDENT, student);
            }
        } else {
            logger.error("Student couldn't be added to the service as it is NULL");
            throw new NullPointerException("Student is NULL - can't add student to the service");
        }
    }

    @Override
    public boolean removeStudent(long id) {
            int result = studentDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Student (%d) removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Student (%d) couldn't be removed from the service", id));
                return false;
            }
    }

    @Override
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

    @Override
    public Grade addGradeToStudent(long studentId, Grade grade) throws NoEntityCreatedException, GradeNotAssignedException {
        if (studentId > 0 && grade != null) {
            Grade tempGrade = addGrade(grade);
            int result = gradeDAO.bindGradeToStudentId(tempGrade.getId(), studentId);
            if (result == 1) {
                logger.debug(String.format("Grade (%s) assigned to student (%d) in the service", tempGrade, studentId));
                return tempGrade;
            } else {
                logger.error(String.format("Grade (%s) couldn't be assigned to student (%d) in the service", tempGrade, studentId));
                throw new GradeNotAssignedException(tempGrade, studentId);
            }
        } else {
            logger.error("Grade couldn't be assigned to student in the service as either student id is invalid or grade is NULL");
            throw new NoEntityCreatedException(EntityType.GRADE, grade);
        }
    }

    @Override
    public List<Grade> getGradesByStudent(long id) throws EntityNotFoundException {
        List<Grade> allGradesByStudentId = new ArrayList<>();
        if (id > 0) {
            allGradesByStudentId = gradeDAO.getAllGradesByStudentId(id);
            for (Grade grade : allGradesByStudentId) {
                long subjectId = grade.getSubject().getId();
                subjectService.getSubjectById(subjectId);
                grade.setSubject(subjectService.getSubjectById(subjectId));
            }
            logger.debug(String.format("Grades of student (%d) retrieved from service", id));
        } else {
            logger.error("Grades of student couldn't be retrieved from the service as student id is invalid");
        }
        return allGradesByStudentId;
    }

    @Override
    public boolean removeGrade(long id) {
            int result = gradeDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Grade (%d) removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Grade (%d) couldn't be removed from the service", id));
                return false;
            }
    }

    @Override
    public boolean assignStudentToGroup(long studentId, long groupId) {
        if (studentId > 0 && groupId > 0) {
            int result = studentDAO.bindStudentToGroupById(studentId, groupId);
            if (result == 1) {
                logger.debug(String.format("Student (%d) assigned to group (%d) in the service", studentId, groupId));
                return true;
            } else {
                logger.error(String.format("Student (%d) couldn't be assigned to group (%d) in the service", studentId, groupId));
                return false;
            }
        } else {
            logger.error("Student couldn't be assigned to group in the service as one of its id is invalid");
            return false;
        }
    }

    @Override
    public boolean removeStudentFromGroup(long studentId, long groupId) {
        if (studentId > 0 && groupId > 0) {
            int result = studentDAO.removeStudentFromGroupById(studentId, groupId);
            if (result == 1) {
                logger.debug(String.format("Student (%d) removed from group (%d) in the service", studentId, groupId));
                return true;
            } else {
                logger.error(String.format("Student (%d) couldn't be removed from group (%d) in the service", studentId, groupId));
                return false;
            }
        } else {
            logger.error("Student couldn't be removed from group in the service as one of its ids is invalid");
            return false;
        }
    }

    @Override
    public StudentGroup getStudentGroupById(long id) throws EntityNotFoundException {
        StudentGroup tempGroup = getBasicStudentGroupById(id);
        List<Student> allStudentByGroupId = studentDAO.getAllStudentsByGroupId(tempGroup.getId());
        ArrayList<Student> tmpSts = new ArrayList<>();
        for (Student student : allStudentByGroupId) {
            Student tempStudent = getStudentById(student.getId());
            tmpSts.add(tempStudent);
        }
        tempGroup.setStudents(tmpSts);
        tempGroup.setTimetable(timetableService.getTimetableForStudentGroup(id));
        logger.debug(String.format("All students from group (id: %d) retrieved from service", id));
        return tempGroup;
    }

    @Override
    public StudentGroup addEmptyStudentGroup(String name) throws NoEntityCreatedException {
        if (name != null) {
            StudentGroup tempGroup = new StudentGroup(name);
            groupDAO.createEntity(tempGroup);
            logger.debug(String.format("Group (%s) added to the service", tempGroup));
            return tempGroup;
        } else {
            throw new NoEntityCreatedException(EntityType.BUILDING);
        }
    }

    @Override
    public boolean removeStudentGroup(long id) {
            int result = groupDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Student group (%d) was removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Student group (%d) couldn't be removed from the service", id));
                return false;
            }
    }

    @Override
    public List<StudentGroup> getGroupsAssignedToTimetableEntry(long ttEntryId) throws EntityNotFoundException {
        List<StudentGroup> groupsByTimetableEntryId = new ArrayList<>();
        if (ttEntryId > 0) {
            List<Long> ids = groupsHasTTEntriesDAO.getStudentGroupIdsByTimetableEntryId(ttEntryId);
            for (long id : ids) {
                groupsByTimetableEntryId.add(getStudentGroupById(id));
            }
        } else {
            logger.error("Groups assigned to timetable entry couldn't be retrieved from the service as timetable entry " +
                    "id is invalid");
        }
        return groupsByTimetableEntryId;
    }

    private Student getBasicStudentById(long id) throws EntityNotFoundException {
        Student tempStudent = studentDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.STUDENT, id));
        logger.debug(String.format("Student (id: %d) retrieved from service", id));
        return tempStudent;
    }

    private Student addBasicStudent(Student student) {
        if (student != null) {
            studentDAO.createEntity(student);
            logger.debug(String.format("Student (%s) added to the service", student));
            return student;
        } else {
            logger.error("Student couldn't be added to service as it is NULL");
            throw new NullPointerException("Student is NULL - can't add it to service");
        }
    }

    private PortalAccount addPortalAccount(PortalAccount account) throws NoEntityCreatedException {
        if (account != null) {
            if (account.getLogin() != null && account.getPassword() != null
                    && account.getExpiryDate() != null && account.getIssueDate() != null) {
                accountDAO.createEntity(account);
                logger.debug(String.format("Portal account %s added to the service", account));
                return account;
            } else {
                throw new NoEntityCreatedException(EntityType.PORTAL_ACCOUNT, account);
            }
        } else {
            logger.error("Portal account couldn't be added to service as it is NULL");
            throw new NullPointerException("Portal account is NULL - can't add it to service");
        }
    }

    private Grade addGrade(Grade grade) {
        if (grade != null) {
            gradeDAO.createEntity(grade);
            logger.debug(String.format("Grade (%s) added to the service", grade));
            return grade;
        } else {
            logger.error("Grade couldn't be added to service as it is NULL");
            throw new NullPointerException("Grade is NULL - can't add it to service");
        }
    }

    private PortalAccount getAccountByStudent(long id) {
        PortalAccount tempAccount = accountDAO
                .getAccountByStudentId(id)
                .orElseGet(() -> {
                    logger.error(String.format("Portal account of student (%d) couldn't be retrieved from service", id));
                    return null;
                });
        logger.debug(String.format("Portal account of student (%d) retrieved from service", id));
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