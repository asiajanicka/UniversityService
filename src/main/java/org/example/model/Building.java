package org.example.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Building {

    private long id;
    private String name;
    private String address;
    private List<Room> rooms;
    private List<Department> departments;

    public Building() {
        this.rooms = new ArrayList<>();
        this.departments = new ArrayList<>();
    }

    public Building(String name, String address) {
        this.name = name;
        this.address = address;
        this.rooms = new ArrayList<>();
        this.departments = new ArrayList<>();
    }

    public Building(String name, String address, List<Room> rooms, List<Department> departments) {
        this.name = name;
        this.address = address;
        this.rooms = rooms;
        this.departments = departments;
    }

}
