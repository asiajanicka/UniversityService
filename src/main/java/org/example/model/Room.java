package org.example.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Room {

    private long id;
    private String number;

    public Room(String number) {
        this.number = number;
    }

}
