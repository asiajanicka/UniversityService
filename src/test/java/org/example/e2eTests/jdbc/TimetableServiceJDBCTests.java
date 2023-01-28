package org.example.e2eTests.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.WeekDay;
import org.example.model.Room;
import org.example.model.StudentGroup;
import org.example.model.Subject;
import org.example.model.TimetableEntry;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IBuildingService;
import org.example.service.interfaces.IStudentService;
import org.example.service.interfaces.ISubjectService;
import org.example.service.interfaces.ITimetableService;
import org.example.service.jdbc.BuildingService;
import org.example.service.jdbc.StudentService;
import org.example.service.jdbc.SubjectService;
import org.example.service.jdbc.TimetableService;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TimetableServiceJDBCTests {

    private static final Logger logger = LogManager.getLogger(TimetableServiceJDBCTests.class);

    @Test
    public void usecase1Test() throws EntityNotFoundException, NoEntityCreatedException {

        logger.info("Start of Timetable Service JDBC Tests - test case 1");
        ISubjectService subjectService = new SubjectService();
        IBuildingService buildingService = new BuildingService();
        ITimetableService timetableService = new TimetableService();
        IStudentService studentService = new StudentService();
        StudentGroup existingGroupOne = studentService.getStudentGroupById(1);
        StudentGroup existingGroupTwo = studentService.getStudentGroupById(2);
        Subject existingSubject = subjectService.getSubjectById(1);
        Room existingRoom = buildingService.getRoomById(1);
        LocalTime expectedTime = LocalTime.of(11, 15);
        WeekDay expectedDay = WeekDay.MONDAY;
        TimetableEntry ttEntry = new TimetableEntry(expectedTime, expectedDay, existingSubject, existingRoom);

        TimetableEntry actualTTEntry = timetableService.addNewTimetableEntry(ttEntry);
        assertThat(actualTTEntry.getTime()).isEqualTo(expectedTime);
        assertThat(actualTTEntry.getWeekDay()).isEqualTo(expectedDay);
        assertThat(actualTTEntry.getSubject()).isEqualTo(existingSubject);
        assertThat(actualTTEntry.getRoom()).isEqualTo(existingRoom);

        timetableService.assignTimetableEntryToGroup(actualTTEntry.getId(), existingGroupOne.getId());
        timetableService.assignTimetableEntryToGroup(actualTTEntry.getId(), existingGroupTwo.getId());
        existingGroupOne = studentService.getStudentGroupById(existingGroupOne.getId());
        existingGroupTwo = studentService.getStudentGroupById(existingGroupTwo.getId());
        assertThat(studentService.getGroupsAssignedToTimetableEntry(actualTTEntry.getId()))
                .contains(existingGroupOne)
                .contains(existingGroupTwo);

        Subject newSubject = subjectService.addNewSubject("Biology");
        Room newRoom = buildingService.addRoom(new Room("123", buildingService.getBuildingById(2)));
        assertThat(timetableService.updateSubjectForTimetableEntry(newSubject, actualTTEntry.getId())).isTrue();
        assertThat(timetableService.updateRoomForTimetableEntry(newRoom, actualTTEntry.getId())).isTrue();

        LocalTime updatedTime = LocalTime.of(8, 15);
        WeekDay updatedDay = WeekDay.FRIDAY;
        assertThat(timetableService.updateTimeslotForTimetableEntry(updatedTime, updatedDay, actualTTEntry.getId())).isTrue();

        actualTTEntry = timetableService.getTimetableEntryById(actualTTEntry.getId());
        assertThat(actualTTEntry.getTime()).isEqualTo(updatedTime);
        assertThat(actualTTEntry.getWeekDay()).isEqualTo(updatedDay);
        assertThat(actualTTEntry.getSubject()).isEqualTo(newSubject);
        assertThat(actualTTEntry.getRoom()).isEqualTo(newRoom);

        assertThat(timetableService.removeTimetableEntryFromGroup(actualTTEntry.getId(), existingGroupOne.getId())).isTrue();
        assertThat(studentService.getGroupsAssignedToTimetableEntry(actualTTEntry.getId()))
                .doesNotContain(existingGroupOne);

        assertThat(subjectService.removeSubject(newSubject.getId())).isFalse();
        assertThat(buildingService.removeRoom(newRoom.getId())).isFalse();

        assertThat(timetableService.removeTimetableEntry(actualTTEntry.getId())).isTrue();
        assertThat(timetableService.getTimetableForStudentGroup(existingGroupTwo.getId()))
                .doesNotContain(actualTTEntry);

        assertThat(subjectService.removeSubject(newSubject.getId())).isTrue();
        assertThat(buildingService.removeRoom(newRoom.getId())).isTrue();
        logger.info("End of Timetable Service JDBC Tests - test case 1");
    }

}