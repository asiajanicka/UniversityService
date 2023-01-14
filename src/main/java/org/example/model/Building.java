package org.example.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Building {

    private long id;
    private String name;
    private String address;
    private List<Room> rooms;
    private List<Department> departments;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;
        Building building = (Building) o;
        return Objects.equals(name, building.name)
                && Objects.equals(address, building.address)
                && Objects.equals(rooms, building.rooms)
                && Objects.equals(departments, building.departments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, rooms, departments);
    }
}
