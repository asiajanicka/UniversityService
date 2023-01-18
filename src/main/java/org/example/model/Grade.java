package org.example.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Grade {

    private long id;
    private int value;
    private Subject subject;

    public Grade(int value) {
        this.value = value;
    }

    public Grade(int value, Subject subject) {
        this.value = value;
        this.subject = subject;
    }

}
