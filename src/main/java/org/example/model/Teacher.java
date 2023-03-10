package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@XmlRootElement(name = "teacher")
@XmlAccessorType(XmlAccessType.FIELD)
public class Teacher extends Person {

    @JsonProperty
    @XmlElement
    private ParkingSpot parkingSpot;

    @XmlElementWrapper(name = "subjects")
    @XmlElement(name = "subject")
    @JsonProperty
    private List<Subject> subjects;

    public Teacher() {
        this.subjects = new ArrayList<>();
    }

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
        this.subjects = new ArrayList<>();
    }

    public Teacher(String firstName, String lastName, ParkingSpot parkingSpot) {
        this(firstName, lastName);
        this.parkingSpot = parkingSpot;
    }

    public Teacher(String firstName, String lastName, ParkingSpot parkingSpot, List<Subject> subjects) {
        this(firstName, lastName, parkingSpot);
        this.subjects = subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(parkingSpot, teacher.parkingSpot)
                && Objects.equals(subjects, teacher.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parkingSpot, subjects);
    }

    @Override
    public String toString() {
        return String.format("Teacher{id=%d firstName=%s lastName=%s parkingSpot=%s subjects=%s}", +
                getId(), getFirstName(), getLastName(), parkingSpot, subjects);
    }

}