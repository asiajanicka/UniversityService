package org.example.model;

import lombok.Getter;
import lombok.Setter;
import org.example.utils.LocalDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@XmlRootElement(name = "student")
@XmlAccessorType(XmlAccessType.FIELD)
public class Student extends Person {

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateOfBirth;
    @XmlElement
    private PortalAccount portalAccount;
    @XmlElementWrapper(name = "grades")
    @XmlElement(name = "grade")
    private List<Grade> grades;

    public Student() {
        super();
        this.grades = new ArrayList<>();
    }

    public Student(String firstName, String lastName, LocalDate dateOfBirth) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.grades = new ArrayList<>();
    }

    public Student(String firstName, String lastName, LocalDate dateOfBirth, PortalAccount portalAccount) {
        this(firstName, lastName, dateOfBirth);
        this.portalAccount = portalAccount;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(dateOfBirth, student.dateOfBirth)
                && Objects.equals(portalAccount, student.portalAccount)
                && Objects.equals(grades, student.grades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateOfBirth, portalAccount, grades);
    }

    @Override
    public String toString() {
        return String.format("Student{id=%d firstName=%s lastName=%s dateOfBirth=%s portalAccount=%s grades=%s}", +
                getId(), getFirstName(), getLastName(), dateOfBirth, portalAccount, grades);
    }

}
