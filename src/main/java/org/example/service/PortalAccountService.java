package org.example.service;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.PortalAccountDAO;
import org.example.dao.interfaces.IPortalAccountDAO;
import org.example.enums.EntityType;
import org.example.model.PortalAccount;
import org.example.model.Student;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;

import java.time.LocalDate;

@NoArgsConstructor
public class PortalAccountService {

    private final IPortalAccountDAO accountDAO = new PortalAccountDAO();
    private static final Logger logger = LogManager.getLogger(StudentService.class);

    public PortalAccount addNewAccount(PortalAccount account) throws NoEntityCreatedException {
        if (account != null) {
            PortalAccount tempAccount = accountDAO
                    .createEntity(account)
                    .orElseThrow(() -> new NoEntityCreatedException(EntityType.PORTAL_ACCOUNT, account));
            logger.debug(String.format("Portal account %s added to the service", tempAccount));
            return tempAccount;
        } else {
            logger.error("Portal account couldn't be added to the service as it is NULL");
            throw new NullPointerException("Portal account is NULL - can't add account to the service");
        }
    }

    public boolean bindAccountToStudent(PortalAccount account, Student student) {
        if (account != null && student != null) {
            int result = accountDAO.bindAccountToStudentId(account.getId(), student.getId());
            if (result == 1) {
                logger.debug(String.format("Portal account (%s) assigned to student (%s) in the service",
                        account, student));
                return true;
            } else {
                logger.error(String.format("Portal account (%s) couldn't be assigned to student (%s) in the service",
                        account, student));
                return false;
            }
        } else {
            logger.error("Portal account couldn't be assigned to student in the service as one of them is NULL");
            return false;
        }
    }

    public PortalAccount getAccountById(long id) throws EntityNotFoundException {
        PortalAccount tempAccount = accountDAO.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.PORTAL_ACCOUNT, id));
        logger.debug(String.format("Portal account with %d retrieved from service", id));
        return tempAccount;
    }

    public boolean removeAccount(PortalAccount account) {
        if (account != null) {
            int result = accountDAO.removeEntity(account.getId());
            if (result == 1) {
                logger.debug(String.format("Portal account (%s) removed from the service", account));
                return true;
            } else {
                logger.error(String.format("Portal account (%s) couldn't be removed from the service", account));
                return false;
            }
        } else {
            logger.error("Portal account couldn't be removed from the service as it is NULL");
            return false;
        }
    }

    public boolean changePasswordForStudent(Student student, String newPassword) throws EntityNotFoundException {
        if (student != null) {
            PortalAccount account = accountDAO
                    .getAccountByStudentId(student.getId())
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.PORTAL_ACCOUNT, student.getPortalAccount().getId()));
            logger.debug(String.format("Retrieved account by student id (%s) from the service", student));
            account.setPassword(newPassword);
            int result = accountDAO.updateEntity(account);
            if (result == 1) {
                logger.debug(String.format("Password changed for portal account of student (%s) in the service", student));
                return true;
            } else {
                logger.error(String.format("Password to portal account of student (%s) couldn't be be changed in the service",
                        student));
                return false;
            }
        } else {
            logger.error("Password to student's portal account couldn't be be changed in the service as student is NULL");
            return false;
        }
    }

    public boolean changeExpDateForStudent(Student student, LocalDate date) throws EntityNotFoundException {
        if (student != null && date != null) {
            if (date.isAfter(LocalDate.now())) {
                PortalAccount account = accountDAO
                        .getAccountByStudentId(student.getId())
                        .orElseThrow(() -> new EntityNotFoundException(EntityType.PORTAL_ACCOUNT));
                logger.debug(String.format("Retrieved account by student id (%s) from the service", student));
                account.setExpiryDate(date);
                int result = accountDAO.updateEntity(account);
                if (result == 1) {
                    logger.debug(String.format("Expiry date changed for portal account of student (%s) in the service",
                            student));
                    return true;
                } else {
                    logger.error(String.format("Expiry date for portal account of student (%s) couldn't be be changed in the service",
                            student));
                    return false;
                }
            } else {
                logger.error("Expiry date of student's portal account couldn't be be changed in the service as new date is the past");
                return false;
            }
        } else {
            logger.error("Expiry date of student's portal account couldn't be be changed in the service as student or new date is NULL");
            return false;
        }
    }

}
