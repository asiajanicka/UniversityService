package org.example.model;

import lombok.*;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@XmlRootElement(name = "subject")
@XmlAccessorType(XmlAccessType.FIELD)
public class Subject {

    @XmlAttribute
    private long id;
    @XmlElement
    private String name;

    public Subject(String name) {
        this.name = name;
    }

}
