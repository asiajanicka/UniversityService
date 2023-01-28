package org.example.service.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IPortalAccountDAO;
import org.example.dao.jdbc.PortalAccountDAO;
import org.example.enums.EntityType;
import org.example.model.PortalAccount;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.example.service.interfaces.IPortalAccountService;

import java.time.LocalDate;

@NoArgsConstructor
public class PortalAccountService implements IPortalAccountService {

    private final IPortalAccountDAO accountDAO = new PortalAccountDAO();
    private static final Logger logger = LogManager.getLogger(StudentService.class);

    @Override
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

    @Override
    public boolean bindAccountToStudent(long accountId, long studentId) {
        if (accountId > 0 && studentId > 0) {
            int result = accountDAO.bindAccountToStudentId(accountId, studentId);
            if (result == 1) {
                logger.debug(String.format("Portal account (%d) assigned to student (%d) in the service",
                        accountId, studentId));
                return true;
            } else {
                logger.error(String.format("Portal account (%d) couldn't be assigned to student (%d) in the service",
                        accountId, studentId));
                return false;
            }
        } else {
            logger.error("Portal account couldn't be assigned to student in the service as one of its id is invalid");
            return false;
        }
    }

    @Override
    public PortalAccount getAccountById(long id) throws EntityNotFoundException {
        PortalAccount tempAccount = accountDAO.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.PORTAL_ACCOUNT, id));
        logger.debug(String.format("Portal account with %d retrieved from service", id));
        return tempAccount;
    }

    @Override
    public boolean removeAccount(long id) {
        if (id > 0) {
            int result = accountDAO.removeEntity(id);
            if (result == 1) {
                logger.debug(String.format("Portal account (%d) removed from the service", id));
                return true;
            } else {
                logger.error(String.format("Portal account (%d) couldn't be removed from the service", id));
                return false;
            }
        } else {
            logger.error("Portal account couldn't be removed from the service as its id is invalid");
            return false;
        }
    }

    @Override
    public boolean changePasswordForStudent(long studentId, String newPassword) throws EntityNotFoundException {
        if (studentId > 0) {
            PortalAccount account = accountDAO
                    .getAccountByStudentId(studentId)
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.PORTAL_ACCOUNT));
            logger.debug(String.format("Retrieved account by student id (%d) from the service", studentId));
            account.setPassword(newPassword);
            int result = accountDAO.updateEntity(account);
            if (result == 1) {
                logger.debug(String.format("Password changed for portal account of student (%d) in the service", studentId));
                return true;
            } else {
                logger.error(String.format("Password to portal account of student (%d) couldn't be be changed in the service",
                        studentId));
                return false;
            }
        } else {
            logger.error("Password to student's portal account couldn't be be changed in the service as student id is invalid");
            return false;
        }
    }

    @Override
    public boolean changeExpDateForStudent(long studentId, LocalDate date) throws EntityNotFoundException {
        if (studentId > 0 && date != null) {
            if (date.isAfter(LocalDate.now())) {
                PortalAccount account = accountDAO
                        .getAccountByStudentId(studentId)
                        .orElseThrow(() -> new EntityNotFoundException(EntityType.PORTAL_ACCOUNT));
                logger.debug(String.format("Retrieved account by student id (%d) from the service", studentId));
                account.setExpiryDate(date);
                int result = accountDAO.updateEntity(account);
                if (result == 1) {
                    logger.debug(String.format("Expiry date changed for portal account of student (%d) in the service",
                            studentId));
                    return true;
                } else {
                    logger.error(String.format("Expiry date for portal account of student (%d) couldn't be be changed in the service",
                            studentId));
                    return false;
                }
            } else {
                logger.error("Expiry date of student's portal account couldn't be be changed in the service as new date is the past");
                return false;
            }
        } else {
            logger.error("Expiry date of student's portal account couldn't be be changed in the service as student id is invalid or new date is NULL");
            return false;
        }
    }

}