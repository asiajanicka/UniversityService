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
public class Department {

    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
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