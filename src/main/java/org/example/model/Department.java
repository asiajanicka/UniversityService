package org.example.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Department {

    private long id;
    private String name;
    private List<Teacher> teachers;

    public Department() {
        this.teachers = new ArrayList<>();
    }

    public Department(String name) {
        this.name = name;
        this.teachers = new ArrayList<>();
    }

    public Department(String name, List<Teacher> teachers) {
        this.name = name;
        this.teachers = teachers;
    }

}
