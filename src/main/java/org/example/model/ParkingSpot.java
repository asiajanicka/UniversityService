package org.example.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ParkingSpot {

    private long id;
    private String name;
    private String address;

    public ParkingSpot(String name, String address) {
        this.name = name;
        this.address = address;
    }

}
