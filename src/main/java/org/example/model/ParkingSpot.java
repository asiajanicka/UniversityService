package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@XmlRootElement(name = "parkingSpot")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParkingSpot {

    @JsonProperty
    @XmlAttribute
    private long id;

    @JsonProperty
    @XmlElement
    private String name;

    @JsonProperty
    @XmlElement
    private String address;

    public ParkingSpot(String name, String address) {
        this.name = name;
        this.address = address;
    }

}