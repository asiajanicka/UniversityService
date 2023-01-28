package org.example.service.interfaces;

import org.example.model.PortalAccount;
import org.example.model.Student;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.time.LocalDate;

public interface IPortalAccountService {

    PortalAccount addNewAccount(PortalAccount account) throws NoEntityCreatedException;

    boolean bindAccountToStudent(PortalAccount account, Student student);

    PortalAccount getAccountById(long id) throws EntityNotFoundException;

    boolean removeAccount(PortalAccount account);

    boolean changePasswordForStudent(Student student, String newPassword) throws EntityNotFoundException;

    boolean changeExpDateForStudent(Student student, LocalDate date) throws EntityNotFoundException;

}