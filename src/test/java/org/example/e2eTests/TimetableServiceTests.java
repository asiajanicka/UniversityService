package org.example.e2eTests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.WeekDay;
import org.example.model.Room;
import org.example.model.StudentGroup;
import org.example.model.Subject;
import org.example.model.TimetableEntry;
import org.example.service.BuildingService;
import org.example.service.StudentService;
import org.example.service.SubjectService;
import org.example.service.TimetableService;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TimetableServiceTests {

    private static final Logger logger = LogManager.getLogger(TimetableServiceTests.class);

    @Test
    public void usecase1Test() throws EntityNotFoundException, NoEntityCreatedException {

        logger.info("Start of Timetable Service Tests - test case 1");
        SubjectService subjectService = new SubjectService();
        BuildingService buildingService = new BuildingService();
        TimetableService timetableService = new TimetableService();
        StudentService studentService = new StudentService();
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

        timetableService.assignTimetableEntryToGroup(actualTTEntry, existingGroupOne);
        timetableService.assignTimetableEntryToGroup(actualTTEntry, existingGroupTwo);
        existingGroupOne = studentService.getStudentGroupById(existingGroupOne.getId());
        existingGroupTwo = studentService.getStudentGroupById(existingGroupTwo.getId());
        assertThat(studentService.getGroupsAssignedToTimetableEntry(actualTTEntry))
                .contains(existingGroupOne)
                .contains(existingGroupTwo);

        Subject newSubject = subjectService.addNewSubject("Biology");
        Room newRoom = buildingService.addRoom(new Room("123", buildingService.getBuildingById(2)));
        assertThat(timetableService.updateSubjectForTimetableEntry(newSubject, actualTTEntry)).isTrue();
        assertThat(timetableService.updateRoomForTimetableEntry(newRoom, actualTTEntry)).isTrue();

        LocalTime updatedTime = LocalTime.of(8, 15);
        WeekDay updatedDay = WeekDay.FRIDAY;
        assertThat(timetableService.updateTimeslotForTimetableEntry(updatedTime, updatedDay, actualTTEntry)).isTrue();

        actualTTEntry = timetableService.getTimetableEntryById(actualTTEntry.getId());
        assertThat(actualTTEntry.getTime()).isEqualTo(updatedTime);
        assertThat(actualTTEntry.getWeekDay()).isEqualTo(updatedDay);
        assertThat(actualTTEntry.getSubject()).isEqualTo(newSubject);
        assertThat(actualTTEntry.getRoom()).isEqualTo(newRoom);

        assertThat(timetableService.removeTimetableEntryFromGroup(actualTTEntry, existingGroupOne)).isTrue();
        assertThat(studentService.getGroupsAssignedToTimetableEntry(actualTTEntry))
                .doesNotContain(existingGroupOne);

        assertThat(subjectService.removeSubject(newSubject)).isFalse();
        assertThat(buildingService.removeRoom(newRoom)).isFalse();

        assertThat(timetableService.removeTimetableEntry(actualTTEntry)).isTrue();
        assertThat(timetableService.getTimetableForStudentGroup(existingGroupTwo))
                .doesNotContain(actualTTEntry);

        assertThat(subjectService.removeSubject(newSubject)).isTrue();
        assertThat(buildingService.removeRoom(newRoom)).isTrue();
        logger.info("End of Timetable Service Tests - test case 1");
    }

}