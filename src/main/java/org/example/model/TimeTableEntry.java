package org.example.model;

import lombok.*;
import org.example.enums.WeekDay;

import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TimeTableEntry {

    private long id;
    private LocalTime time;
    private WeekDay weekDay;
    private Subject subject;
    private Room room;

    public TimeTableEntry(LocalTime time, WeekDay weekDay) {
        this.time = time;
        this.weekDay = weekDay;
    }

    public TimeTableEntry(LocalTime time, WeekDay weekDay, Subject subject, Room room) {
        this(time, weekDay);
        this.subject = subject;
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeTableEntry)) return false;
        TimeTableEntry that = (TimeTableEntry) o;
        return Objects.equals(time, that.time)
                && weekDay == that.weekDay
                && Objects.equals(subject, that.subject)
                && Objects.equals(room, that.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, weekDay, subject, room);
    }

}
