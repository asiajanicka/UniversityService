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
@EqualsAndHashCode
@ToString
@XmlRootElement(name = "building")
@XmlAccessorType(XmlAccessType.FIELD)
public class Building {

    @JsonProperty
    @XmlAttribute
    private long id;

    @JsonProperty
    @XmlElement
    private String name;

    @JsonProperty
    @XmlElement
    private String address;

    @JsonProperty
    @XmlElementWrapper(name = "departments")
    @XmlElement(name = "department")
    private List<Department> departments;

    public Building() {
        this.departments = new ArrayList<>();
    }

    public Building(String name, String address) {
        this.name = name;
        this.address = address;
        this.departments = new ArrayList<>();
    }

    public Building(String name, String address, List<Department> departments) {
        this.name = name;
        this.address = address;
        this.departments = departments;
    }

}