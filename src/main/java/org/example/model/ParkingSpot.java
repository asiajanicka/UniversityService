package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ParkingSpot {

    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String address;

    public ParkingSpot(String name, String address) {
        this.name = name;
        this.address = address;
    }

}