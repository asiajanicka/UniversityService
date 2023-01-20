package org.example.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Room {

    private long id;
    private String number;
    private Building building;

    public Room(String number, Building building) {
        this.number = number;
        this.building = building;
    }

}
