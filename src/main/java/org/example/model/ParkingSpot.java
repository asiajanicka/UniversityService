package org.example.model;

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

    @XmlAttribute
    private long id;
    @XmlElement
    private String name;
    @XmlElement
    private String address;

    public ParkingSpot(String name, String address) {
        this.name = name;
        this.address = address;
    }

}
