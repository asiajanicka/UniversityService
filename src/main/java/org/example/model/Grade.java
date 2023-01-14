package org.example.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Grade {

    private long id;
    private int value;
    private Subject subject;

    public Grade(int value, Subject subject) {
        this.value = value;
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grade)) return false;
        Grade grade = (Grade) o;
        return value == grade.value
                && Objects.equals(subject, grade.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, subject);
    }

}
