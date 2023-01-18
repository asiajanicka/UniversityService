package utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.model.PortalAccount;
import org.example.model.Student;
import org.example.model.Teacher;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static Student getFullStudent(){
        PortalAccount account = new PortalAccount("abby_muse", "admin",
                LocalDate.now(), LocalDate.now().plusYears(4));

        Student student = new Student("Abby", "Muse", LocalDate.of(1985, 1, 1));
        student.setPortalAccount(account);
        return student;
    }

    public static Student getBasicStudent(){
        return new Student("Abby", "Muse",
                LocalDate.of(1985, 1, 1));
    }

    public static PortalAccount getPortalAccount(){
        return new PortalAccount("test_account", "admin",
                LocalDate.now(), LocalDate.now().plusYears(4));
    }

    public static Teacher getBasicTeacher(){
        return new Teacher("Paul", "Simon");
    }

}
