package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "department")
@XmlAccessorType(XmlAccessType.FIELD)
public class Department {

    @JsonProperty
    @XmlAttribute
    private long id;

    @JsonProperty
    @XmlElement
    private String name;

    @JsonProperty
    @XmlElementWrapper(name = "teachers")
    @XmlElement(name = "teacher")
    private List<Teacher> teachers;

    public Department() {
        this.teachers = new ArrayList<>();
    }

    public Department(String name) {
        this.name = name;
        this.teachers = new ArrayList<>();
    }

    public Department(String name, List<Teacher> teachers) {
        this.name = name;
        this.teachers = teachers;
    }

}