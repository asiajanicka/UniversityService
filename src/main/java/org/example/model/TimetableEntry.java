package org.example.model;

import lombok.*;
import org.example.enums.WeekDay;

import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class TimetableEntry {

    private long id;
    private LocalTime time;
    private WeekDay weekDay;
    private Subject subject;
    private Room room;

    public TimetableEntry(LocalTime time, WeekDay weekDay) {
        this.time = time;
        this.weekDay = weekDay;
    }

    public TimetableEntry(LocalTime time, WeekDay weekDay, Subject subject, Room room) {
        this(time, weekDay);
        this.subject = subject;
        this.room = room;
    }

}
