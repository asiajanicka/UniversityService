package org.example.dao.interfaces;

import org.example.model.GroupsHasTimetableEntry;

import java.util.List;

public interface IGroupsHasTimetableEntriesDAO extends IBaseDAO<GroupsHasTimetableEntry> {

    int removeEntityById(long groupId, long ttEntityId);

    List<Long> getStudentGroupIdsByTimetableEntryId(long ttEntryId);

    List<Long> getTimetableEntryIdsByGroupId(long groupId);

}
