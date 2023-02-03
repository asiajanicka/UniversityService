package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
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

    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateOfBirth;

    @JsonProperty
    @XmlElement
    private PortalAccount portalAccount;

    @JsonProperty
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