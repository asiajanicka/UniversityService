package org.example.service.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IGroupsHasTimetableEntriesDAO;
import org.example.dao.interfaces.ITimetableEntryDAO;
import org.example.dao.jdbc.GroupsHasTimetableEntriesDAO;
import org.example.dao.jdbc.TimetableEntryDAO;
import org.example.enums.EntityType;
import org.example.enums.WeekDay;
import org.example.model.*;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.ITimetableService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class TimetableService implements ITimetableService {

    private final ITimetableEntryDAO timetableDAO = new TimetableEntryDAO();
    private final IGroupsHasTimetableEntriesDAO groupsHasTTEntriesDAO = new GroupsHasTimetableEntriesDAO();
    private final SubjectService subjectService = new SubjectService();
    private final BuildingService buildingService = new BuildingService();
    private static final Logger logger = LogManager.getLogger(TimetableService.class);

    @Override
    public TimetableEntry getTimetableEntryById(long id) throws EntityNotFoundException {
        TimetableEntry tempTTEntry = timetableDAO
                .getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TIME_TABLE_ENTRY, id));
        tempTTEntry.setSubject(subjectService.getSubjectById(tempTTEntry.getSubject().getId()));
        tempTTEntry.setRoom(buildingService.getRoomById(tempTTEntry.getRoom().getId()));
        logger.debug(String.format("Timetable entry (id: %d) retrieved from service", id));
        return tempTTEntry;
    }

    @Override
    public TimetableEntry addNewTimetableEntry(TimetableEntry ttEntry) throws NoEntityCreatedException {
        if (ttEntry != null) {
            TimetableEntry tempTTEntry = timetableDAO
                    .createEntity(ttEntry)
                    .orElseThrow(() -> new NoEntityCreatedException(EntityType.TIME_TABLE_ENTRY, ttEntry));
            tempTTEntry.setSubject(ttEntry.getSubject());
            tempTTEntry.setRoom(ttEntry.getRoom());
            logger.debug(String.format("Timetable entry %s added to the service", tempTTEntry));
            return tempTTEntry;
        } else {
            logger.error("Timetable entry couldn't be added to service as it is NULL");
            throw new NullPointerException("Timetable entry is NULL - can't add it to service");
        }
    }

    @Override
    public boolean updateTimeslotForTimetableEntry(LocalTime time, WeekDay day, TimetableEntry ttEntry) {
        if (ttEntry != null) {
            ttEntry.setTime(time);
            ttEntry.setWeekDay(day);
            return updateTimetableEntity(ttEntry);
        } else {
            logger.error("Couldn't update timeslot for time table entry in the service as it is NULL");
            return false;
        }
    }

    @Override
    public boolean updateRoomForTimetableEntry(Room room, TimetableEntry ttEntry) {
        if (room != null && ttEntry != null) {
            ttEntry.setRoom(room);
            return updateTimetableEntity(ttEntry);
        } else {
            logger.error("Timetable entry couldn't be assigned to room in the service as one of them is NULL");
            return false;
        }
    }

    @Override
    public boolean updateSubjectForTimetableEntry(Subject subject, TimetableEntry ttEntry) {
        if (subject != null && ttEntry != null) {
            ttEntry.setSubject(subject);
            return updateTimetableEntity(ttEntry);
        } else {
            logger.error("Timetable entry couldn't be assigned to subject in the service as one of them is NULL");
            return false;
        }
    }

    private boolean updateTimetableEntity(TimetableEntry ttEntry) {
        int result = timetableDAO.updateEntity(ttEntry);
        if (result == 1) {
            logger.debug(String.format("Timetable entry (%s) updated in the service", ttEntry));
            return true;
        } else {
            logger.error(String.format("Timetable entry (%s) couldn't be updated in the service", ttEntry));
            return false;
        }
    }

    @Override
    public boolean removeTimetableEntry(TimetableEntry ttEntry) {
        if (ttEntry != null) {
            int result = timetableDAO.removeEntity(ttEntry.getId());
            if (result == 1) {
                logger.debug(String.format("Timetable entry (%s) removed from the service", ttEntry));
                return true;
            } else {
                logger.error(String.format("Timetable entry (%s) couldn't be removed from the service", ttEntry));
                return false;
            }
        } else {
            logger.error("Timetable entry couldn't be removed from the service as it is NULL");
            return false;
        }
    }

    @Override
    public GroupsHasTimetableEntry assignTimetableEntryToGroup(TimetableEntry ttEntry, StudentGroup group) throws NoEntityCreatedException {
        if (ttEntry != null && group != null) {
            GroupsHasTimetableEntry groupsHasTimetableEntryToAdd = new GroupsHasTimetableEntry(group.getId(), ttEntry.getId());
            GroupsHasTimetableEntry tempGroupsHasTimetableEntry = groupsHasTTEntriesDAO
                    .createEntity(groupsHasTimetableEntryToAdd)
                    .orElseThrow(() -> new NoEntityCreatedException(EntityType.GROUP_HAS_TIMETABLE_ENTRY,
                            groupsHasTimetableEntryToAdd));
            logger.debug(String.format("Timetable entry %s assigned to group (%s) to the service", ttEntry, group));
            return tempGroupsHasTimetableEntry;
        } else {
            logger.error("Timetable entry couldn't be assigned to group in the service as one of them is NULL");
            throw new NullPointerException("Timetable entry or student group is NULL - can't assign it in service");
        }
    }

    @Override
    public boolean removeTimetableEntryFromGroup(TimetableEntry ttEntry, StudentGroup group) {
        if (ttEntry != null && group != null) {
            int result = groupsHasTTEntriesDAO.removeEntityById(group.getId(), ttEntry.getId());
            if (result == 1) {
                logger.debug(String.format("Timetable entry (%s) assigned to group (%s) from the service", ttEntry, group));
                return true;
            } else {
                logger.error(String.format("Timetable entry (%s) couldn't be assigned to group (%s) in the the service",
                        ttEntry, group));
                return false;
            }
        } else {
            logger.error("Timetable entry couldn't be assigned to group in the service as one of them is NULL");
            return false;
        }
    }

    @Override
    public List<TimetableEntry> getTimetableForStudentGroup(StudentGroup group) throws EntityNotFoundException {
        List<TimetableEntry> timetableByGroupId = new ArrayList<>();
        if (group != null) {
            List<Long> ids = groupsHasTTEntriesDAO.getTimetableEntryIdsByGroupId(group.getId());
            for (long id : ids) {
                timetableByGroupId.add(getTimetableEntryById(id));
            }
        } else {
            logger.error("Time table entries assigned to group couldn't be retrieved from the service as timetable entry is NULL");
        }
        return timetableByGroupId;
    }

}