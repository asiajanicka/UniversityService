package org.example.service.interfaces;

import org.example.model.PortalAccount;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.time.LocalDate;

public interface IPortalAccountService {

    PortalAccount addNewAccount(PortalAccount account) throws NoEntityCreatedException;

    boolean bindAccountToStudent(long accountId, long studentId);

    PortalAccount getAccountById(long id) throws EntityNotFoundException;

    boolean removeAccount(long accountId);

    boolean changePasswordForStudent(long studentId, String newPassword) throws EntityNotFoundException;

    boolean changeExpDateForStudent(long studentId, LocalDate date) throws EntityNotFoundException;

}