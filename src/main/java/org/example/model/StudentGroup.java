package org.example.model;

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

    @XmlAttribute
    private long id;
    @XmlElement
    private String name;

    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    private List<Student> students;
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