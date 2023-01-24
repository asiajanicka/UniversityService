package org.example.model;

import lombok.*;
import org.example.enums.WeekDay;
import org.example.utils.LocalTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@XmlRootElement(name = "timetableEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimetableEntry {

    @XmlAttribute
    private long id;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime time;
    @XmlElement
    private WeekDay weekDay;
    @XmlElement
    private Subject subject;
    @XmlElement
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
