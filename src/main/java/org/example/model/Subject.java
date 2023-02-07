package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty
    @XmlAttribute
    private long id;

    @JsonProperty
    @XmlElement
    private String name;

    public Subject(String name) {
        this.name = name;
    }

}