package org.example.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Subject {

    private long id;
    private String name;

    public Subject(String name) {
        this.name = name;
    }

}
