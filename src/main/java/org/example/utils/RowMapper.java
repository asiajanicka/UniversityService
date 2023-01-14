package org.example.utils;

import org.example.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RowMapper {

    public static List<StudentGroup> mapToStudentGroupEntityList(ResultSet rs) throws SQLException {
        List<StudentGroup> groups = new ArrayList<>();
        while (rs.next()) {
            StudentGroup group = new StudentGroup();
            group.setId(rs.getLong("id"));
            group.setName(rs.getString("name"));
            groups.add(group);
        }
        return groups;
    }

    public static List<Student> mapToStudentEntityList(ResultSet rs) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (rs.next()) {
            Student student = new Student();
            student.setId(rs.getLong("id"));
            student.setFirstName(rs.getString("first_name"));
            student.setLastName(rs.getString("last_name"));
            student.setDateOfBirth(rs.getObject("date_of_birth", LocalDate.class));
            students.add(student);
        }
        return students;
    }

    public static List<PortalAccount> mapToPortalAccountEntityList(ResultSet rs) throws SQLException {
        List<PortalAccount> accounts = new ArrayList<>();
        while (rs.next()) {
            PortalAccount account = new PortalAccount();
            account.setId(rs.getLong("id"));
            account.setLogin(rs.getString("login"));
            account.setPassword(rs.getString("password"));
            account.setIssueDate(LocalDate.parse(rs.getString("issue_date")));
            account.setExpiryDate(LocalDate.parse(rs.getString("expiry_date")));
            accounts.add(account);
        }
        return accounts;
    }

    public static List<Subject> mapToSubjectEntityList(ResultSet rs) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        while (rs.next()) {
            Subject subject = new Subject();
            subject.setId(rs.getLong("id"));
            subject.setName(rs.getString("name"));
            subjects.add(subject);
        }
        return subjects;
    }

    public static List<Grade> mapToGradeEntityList(ResultSet rs) throws SQLException {
        List<Grade> grades = new ArrayList<>();
        while (rs.next()) {
            Grade grade = new Grade();
            grade.setId(rs.getLong("id"));
            grade.setValue(rs.getInt("value"));
            Subject subject = new Subject();
            subject.setId(rs.getLong("subject_id"));
            grade.setSubject(subject);
            grades.add(grade);
        }
        return grades;
    }

}
