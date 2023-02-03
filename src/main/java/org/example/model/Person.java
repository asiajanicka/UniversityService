package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Person {

    @JsonProperty
    @XmlAttribute
    private long id;

    @JsonProperty
    @XmlElemen
    private String firstName;

    @JsonProperty
    @XmlElement
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}