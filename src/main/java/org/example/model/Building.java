package org.example.model;

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

    private long id;
    private String name;
    private String address;
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
