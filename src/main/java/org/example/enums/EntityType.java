package org.example.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EntityType {

    STUDENT_GROUP("student group"),
    PORTAL_ACCOUNT("portal account"),
    STUDENT("student"),
    GRADE("grade"),
    TIME_TABLE_ENTRY("time table entry"),
    ROOM("room"),
    BUILDING("building"),
    DEPARTMENT("department"),
    TEACHER("teacher"),
    PARKING_SPOT("parking spot"),
    SUBJECT("subject"),
    GROUP_HAS_TIMETABLE_ENTRY("group has timetable entity");

    private final String displayName;

}
