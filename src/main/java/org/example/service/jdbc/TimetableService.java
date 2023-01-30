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
import org.example.model.GroupsHasTimetableEntry;
import org.example.model.Room;
import org.example.model.Subject;
import org.example.model.TimetableEntry;
import org.example.service.exception.EntityNotFoundException;
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
    public TimetableEntry addNewTimetableEntry(TimetableEntry ttEntry) {
        if (ttEntry != null) {
            timetableDAO.createEntity(ttEntry);
            ttEntry.setSubject(ttEntry.getSubject());
            ttEntry.setRoom(ttEntry.getRoom());
            logger.debug(String.format("Timetable entry %s added to the service", ttEntry));
            return ttEntry;
        } else {
            logger.error("Timetable entry couldn't be added to service as it is NULL");
            throw new NullPointerException("Timetable entry is NULL - can't add it to service");
        }
    }

    @Override
    public boolean updateTimeslotForTimetableEntry(LocalTime time, WeekDay day, long ttEntryId) throws EntityNotFoundException {
        if (ttEntryId > 0) {
            TimetableEntry timetableEntryById = getTimetableEntryById(ttEntryId);
            timetableEntryById.setTime(time);
            timetableEntryById.setWeekDay(day);
            return updateTimetableEntity(timetableEntryById);
        } else {
            logger.error("Couldn't update timeslot for time table entry in the service as its id is invalid");
            return false;
        }
    }

    @Override
    public boolean updateRoomForTimetableEntry(Room room, long ttEntryId) throws EntityNotFoundException {
        if (room != null && ttEntryId > 0) {
            TimetableEntry timetableEntryById = getTimetableEntryById(ttEntryId);
            timetableEntryById.setRoom(room);
            return updateTimetableEntity(timetableEntryById);
        } else {
            logger.error("Timetable entry couldn't be assigned to room in the service as eider room is null or timetable" +
                    " entry is is invalid");
            return false;
        }
    }

    @Override
    public boolean updateSubjectForTimetableEntry(Subject subject, long ttEntryId) throws EntityNotFoundException {
        if (subject != null && ttEntryId > 0) {
            TimetableEntry timetableEntryById = getTimetableEntryById(ttEntryId);
            timetableEntryById.setSubject(subject);
            return updateTimetableEntity(timetableEntryById);
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
    public boolean removeTimetableEntry(long id) {
        if (id > 0) {
            int result = timetableDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Timetable entry (%d) removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Timetable entry (%d) couldn't be removed from the service", id));
                return false;
            }
        } else {
            logger.error("Timetable entry couldn't be removed from the service as its id is invalid");
            return false;
        }
    }

    @Override
    public GroupsHasTimetableEntry assignTimetableEntryToGroup(long ttEntryId, long groupId) {
        if (ttEntryId > 0 && groupId > 0) {
            GroupsHasTimetableEntry groupsHasTimetableEntryToAdd = new GroupsHasTimetableEntry(groupId, ttEntryId);
            groupsHasTTEntriesDAO.createEntity(groupsHasTimetableEntryToAdd);
            logger.debug(String.format("Timetable entry (%d) assigned to group (%d) to the service", ttEntryId, groupId));
            return groupsHasTimetableEntryToAdd;
        } else {
            logger.error("Timetable entry couldn't be assigned to group in the service as one of its id is invalid");
            throw new NullPointerException("Timetable entry or student group id is invalid - can't assign it in service");
        }
    }

    @Override
    public boolean removeTimetableEntryFromGroup(long ttEntryId, long groupId) {
        if (ttEntryId > 0 && groupId > 0) {
            int result = groupsHasTTEntriesDAO.removeEntityById(groupId, ttEntryId);
            if (result == 1) {
                logger.debug(String.format("Timetable entry (%d) assigned to group (%d) from the service", ttEntryId, groupId));
                return true;
            } else {
                logger.error(String.format("Timetable entry (%d) couldn't be assigned to group (%d) in the the service",
                        ttEntryId, groupId));
                return false;
            }
        } else {
            logger.error("Timetable entry couldn't be assigned to group in the service as one of its id is invalid");
            return false;
        }
    }

    @Override
    public List<TimetableEntry> getTimetableForStudentGroup(long groupId) throws EntityNotFoundException {
        List<TimetableEntry> timetableByGroupId = new ArrayList<>();
        if (groupId > 0) {
            List<Long> ids = groupsHasTTEntriesDAO.getTimetableEntryIdsByGroupId(groupId);
            for (long id : ids) {
                timetableByGroupId.add(getTimetableEntryById(id));
            }
        } else {
            logger.error("Time table entries assigned to group couldn't be retrieved from the service as timetable entry " +
                    "id is invalid");
        }
        return timetableByGroupId;
    }

}