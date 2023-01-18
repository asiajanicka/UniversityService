package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GroupsHasTimetableEntry {

    private long groupHasTimetableEntryId;
    private long groupId;
    private long timetableEntryId;

}
