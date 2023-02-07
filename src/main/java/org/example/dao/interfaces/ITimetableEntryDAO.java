package org.example.dao.interfaces;

import org.example.model.TimetableEntry;

import java.util.List;

public interface ITimetableEntryDAO extends IBaseDAO<TimetableEntry> {

    List<TimetableEntry> getTimetableBySubjectId(long subjectId);

    List<TimetableEntry> getTimetableByRoomId(long roomId);

    int bindSubjectToTimetableEntry(long subjectId, long timetableEntryId);

    int bindRoomToTimetableEntry(long roomId, long timetableEntryId);

}