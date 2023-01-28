package org.example.service.interfaces;

import org.example.enums.WeekDay;
import org.example.model.*;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.time.LocalTime;
import java.util.List;

public interface ITimetableService {

    TimetableEntry getTimetableEntryById(long id) throws EntityNotFoundException;

    TimetableEntry addNewTimetableEntry(TimetableEntry ttEntry) throws NoEntityCreatedException;

    boolean updateTimeslotForTimetableEntry(LocalTime time, WeekDay day, TimetableEntry ttEntry);

    boolean updateRoomForTimetableEntry(Room room, TimetableEntry ttEntry);

    boolean updateSubjectForTimetableEntry(Subject subject, TimetableEntry ttEntry);

    public boolean removeTimetableEntry(TimetableEntry ttEntry);

    GroupsHasTimetableEntry assignTimetableEntryToGroup(TimetableEntry ttEntry, StudentGroup group) throws NoEntityCreatedException;

    boolean removeTimetableEntryFromGroup(TimetableEntry ttEntry, StudentGroup group);

    List<TimetableEntry> getTimetableForStudentGroup(StudentGroup group) throws EntityNotFoundException;

}