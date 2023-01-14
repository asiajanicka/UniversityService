package org.example.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ParkingSpot {

    private long id;
    private String name;
    private String address;

    public ParkingSpot(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingSpot)) return false;
        ParkingSpot that = (ParkingSpot) o;
        return Objects.equals(name, that.name)
                && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

}
