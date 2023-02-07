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

    boolean updateTimeslotForTimetableEntry(LocalTime time, WeekDay day, long ttEntryId) throws EntityNotFoundException;

    boolean updateRoomForTimetableEntry(Room room, long ttEntryId) throws EntityNotFoundException;

    boolean updateSubjectForTimetableEntry(Subject subject, long ttEntryId) throws EntityNotFoundException;

    boolean removeTimetableEntry(long id);

    GroupsHasTimetableEntry assignTimetableEntryToGroup(long ttEntryId, long groupId) throws NoEntityCreatedException;

    boolean removeTimetableEntryFromGroup(long ttEntryId, long groupId);

    List<TimetableEntry> getTimetableForStudentGroup(long groupId) throws EntityNotFoundException;

}