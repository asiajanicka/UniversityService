package org.example.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StudentGroup {

    private long id;
    private String name;
    private List<Student> students;
//    private List<TimeTableEntry> timetable;

    public StudentGroup() {
        students = new ArrayList<>();
    }

    public StudentGroup(String name) {
        this.name = name;
        students = new ArrayList<>();
    }

}
