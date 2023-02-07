package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@XmlRootElement(name = "room")
@XmlAccessorType(XmlAccessType.FIELD)
public class Room {

    @JsonProperty
    @XmlAttribute
    private long id;

    @JsonProperty
    @XmlElement
    private String number;

    @JsonProperty
    @XmlElement
    private Building building;

    public Room(String number, Building building) {
        this.number = number;
        this.building = building;
    }

}