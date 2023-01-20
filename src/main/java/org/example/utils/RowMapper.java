package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.enums.WeekDay;
import org.example.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static List<Teacher> mapToTeacherEntityList(ResultSet rs) throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        while (rs.next()) {
            Teacher teacher = new Teacher();
            teacher.setId(rs.getLong("id"));
            teacher.setFirstName(rs.getString("first_name"));
            teacher.setLastName(rs.getString("last_name"));
            teachers.add(teacher);
        }
        return teachers;
    }

    public static List<ParkingSpot> mapToParkingSpotEntityList(ResultSet rs) throws SQLException {
        List<ParkingSpot> spots = new ArrayList<>();
        while (rs.next()) {
            ParkingSpot spot = new ParkingSpot();
            spot.setId(rs.getLong("id"));
            spot.setName(rs.getString("name"));
            spot.setAddress(rs.getString("address"));
            spots.add(spot);
        }
        return spots;
    }

    public static List<Department> mapToDepartmentEntityList(ResultSet rs) throws SQLException {
        List<Department> depts = new ArrayList<>();
        while (rs.next()) {
            Department dept = new Department();
            dept.setId(rs.getLong("id"));
            dept.setName(rs.getString("name"));
            depts.add(dept);
        }
        return depts;
    }

    public static List<Building> mapToBuildingEntityList(ResultSet rs) throws SQLException {
        List<Building> buildings = new ArrayList<>();
        while (rs.next()) {
            Building building = new Building();
            building.setId(rs.getLong("id"));
            building.setName(rs.getString("name"));
            building.setAddress(rs.getString("address"));
            buildings.add(building);
        }
        return buildings;
    }

    public static List<Room> mapToRoomEntityList(ResultSet rs) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        while (rs.next()) {
            Room room = new Room();
            room.setId(rs.getLong("id"));
            room.setNumber(rs.getString("room_number"));
            rooms.add(room);
        }
        return rooms;
    }

    public static List<TimetableEntry> mapToTimetableEntityList(ResultSet rs) throws SQLException {
        List<TimetableEntry> timetableEntries = new ArrayList<>();
        while (rs.next()) {
            TimetableEntry timetableEntry = new TimetableEntry();
            timetableEntry.setId(rs.getLong("id"));
            timetableEntry.setTime(rs.getObject("week_day", LocalTime.class));
            timetableEntry.setWeekDay(WeekDay.valueOf(rs.getString("week_day").toUpperCase()));
            timetableEntries.add(timetableEntry);
        }
        return timetableEntries;
    }

    public static List<GroupsHasTimetableEntry> mapToGroupHasTimetableEntityList(ResultSet rs) throws SQLException {
        List<GroupsHasTimetableEntry> groupHasTimetableEntries = new ArrayList<>();
        while (rs.next()) {
            GroupsHasTimetableEntry entry = new GroupsHasTimetableEntry();
            entry.setGroupHasTimetableEntryId(rs.getLong("id"));
            entry.setGroupId(rs.getLong("student_group_id"));
            entry.setTimetableEntryId(rs.getLong("time_table_entry_id"));
            groupHasTimetableEntries.add(entry);
        }
        return groupHasTimetableEntries;
    }

}
