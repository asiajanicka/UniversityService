package org.example.model;

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

    @XmlAttribute
    private long id;
    @XmlElement
    private String number;
    @XmlElement
    private Building building;

    public Room(String number, Building building) {
        this.number = number;
        this.building = building;
    }

}
