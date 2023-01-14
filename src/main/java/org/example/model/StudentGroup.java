package org.example.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StudentGroup {

    private long id;
    private String name;
    private List<Student> students;

    public StudentGroup(String name) {
        this.name = name;
        students = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentGroup)) return false;
        StudentGroup group = (StudentGroup) o;
        return Objects.equals(name, group.name) && Objects.equals(students, group.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, students);
    }

}
