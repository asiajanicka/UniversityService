package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Room {

    @JsonProperty
    private long id;

    @JsonProperty
    private String number;

    @JsonProperty
    private Building building;

    public Room(String number, Building building) {
        this.number = number;
        this.building = building;
    }

}