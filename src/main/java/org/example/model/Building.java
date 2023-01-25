package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Building {

    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String address;
    @JsonProperty
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