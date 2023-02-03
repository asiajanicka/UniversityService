package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
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

    @JsonProperty
    @XmlAttribute
    private long id;
    
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH-mm-ss")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime time;

    @JsonProperty
    @XmlElement
    private WeekDay weekDay;

    @JsonProperty
    @XmlElement
    private Subject subject;

    @JsonProperty
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