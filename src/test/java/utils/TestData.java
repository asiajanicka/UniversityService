package utils;

import org.example.model.PortalAccount;
import org.example.model.Student;

import java.time.LocalDate;

public class TestData {

    public static Student getFullStudent(){
        PortalAccount account = new PortalAccount("abby_ler", "admin",
                LocalDate.now(), LocalDate.now().plusYears(4));

        Student student = new Student("Abby", "Lerman", LocalDate.of(1985, 01, 01));
        student.setPortalAccount(account);
        return student;
    }

    public static Student getBasicStudent(){
        return new Student("Abby", "Lerman",
                LocalDate.of(1985, 01, 01));
    }

    public static PortalAccount getPortalAccount(){
        return new PortalAccount("test_account", "admin",
                LocalDate.now(), LocalDate.now().plusYears(4));
    }

}
