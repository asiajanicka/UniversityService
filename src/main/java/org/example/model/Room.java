package org.example.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Room {

    private long id;
    private String number;

    public Room(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return Objects.equals(number, room.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

}
