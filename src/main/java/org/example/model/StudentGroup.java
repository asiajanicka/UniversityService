package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private List<Student> students;
    @JsonProperty
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