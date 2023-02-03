package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GroupsHasTimetableEntry {

    @JsonProperty
    private long groupHasTimetableEntryId;

    @JsonProperty
    private long groupId;

    @JsonProperty
    private long timetableEntryId;

    public GroupsHasTimetableEntry(long groupId, long timetableEntryId) {
        this.groupId = groupId;
        this.timetableEntryId = timetableEntryId;
    }

}