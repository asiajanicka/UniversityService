package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StudentGroup {

    private long id;
    private String name;
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
