package org.example.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Department {

    private long id;
    private String name;
    private List<Teacher> teachers;

    public Department(String name) {
        this.name = name;
        this.teachers = new ArrayList<>();
    }

    public Department(String name, List<Teacher> teachers) {
        this.name = name;
        this.teachers = teachers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return Objects.equals(name, that.name)
                && Objects.equals(teachers, that.teachers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teachers);
    }

}
