package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "studentGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class StudentGroup {

    @JsonProperty
    @XmlAttribute
    private long id;

    @JsonProperty
    @XmlElement
    private String name;

    @JsonProperty
    @XmlElement
    private List<Student> students;

    @JsonProperty
    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    private List<Student> students;

    @JsonProperty
    @XmlElementWrapper(name = "timetable")
    @XmlElement(name = "timetableEntry")
    private List<TimetableEntry> timetable;

    public StudentGroup() {
        students = new ArrayList<>();
        timetable = new ArrayList<>();
    }

    public StudentGroup(String name) {
        this();
        this.name = name;
    }

}