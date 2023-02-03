package org.example.model;

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

    @XmlAttribute
    private long id;

    @XmlElement
    private int value;

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