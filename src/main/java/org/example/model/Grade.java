package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@XmlRootElement(name = "grade")
@XmlAccessorType(XmlAccessType.FIELD)
public class Grade {

    @JsonProperty
    @XmlAttribute
    private long id;

    @JsonProperty
    @XmlElement
    private int value;

    @JsonProperty
    @XmlElement
    private Subject subject;

    public Grade(int value) {
        this.value = value;
    }

    public Grade(int value, Subject subject) {
        this.value = value;
        this.subject = subject;
    }

}