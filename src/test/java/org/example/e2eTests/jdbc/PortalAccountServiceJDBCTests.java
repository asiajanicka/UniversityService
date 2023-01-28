package org.example.e2eTests.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.PortalAccount;
import org.example.model.Student;
import org.example.service.interfaces.IPortalAccountService;
import org.example.service.interfaces.IStudentService;
import org.example.service.jdbc.PortalAccountService;
import org.example.service.jdbc.StudentService;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.junit.jupiter.api.Test;
import utils.TestData;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PortalAccountServiceJDBCTests {

    private static final Logger logger = LogManager.getLogger(PortalAccountServiceJDBCTests.class);

    @Test
    public void usecase1Test() throws NoEntityCreatedException, EntityNotFoundException {

        logger.info("Start of Portal Account Service JDBC Tests - test case 1");
        IStudentService studentService = new StudentService();
        IPortalAccountService accountService = new PortalAccountService();
        PortalAccount expectedAccount = TestData.getPortalAccount();

        Student actualStudent = studentService.addNewStudent(TestData.getBasicStudent());
        PortalAccount actualAccount = accountService.addNewAccount(expectedAccount);
        assertThat(accountService.bindAccountToStudent(actualAccount, actualStudent)).isTrue();

        String newPassword = "new_password";
        LocalDate newExpDate = LocalDate.now().plusYears(4);
        assertThat(accountService.changePasswordForStudent(actualStudent, newPassword)).isTrue();
        assertThat(accountService.changeExpDateForStudent(actualStudent, newExpDate)).isTrue();

        Student studentWithAccount = studentService.getStudentById(actualStudent.getId());
        assertThat(studentWithAccount.getPortalAccount().getPassword()).isEqualTo(newPassword);
        assertThat(studentWithAccount.getPortalAccount().getExpiryDate()).isEqualTo(newExpDate);

        assertThat(accountService.removeAccount(actualAccount)).isTrue();
        assertThatThrownBy(() -> accountService.getAccountById(actualAccount.getId())).isInstanceOf(EntityNotFoundException.class);
        assertThat(studentService.getStudentById(actualStudent.getId()).getPortalAccount()).isNull();

        studentService.removeStudent(actualStudent);
        logger.info("End of Portal Account Service JDBC Tests - test case 1");
    }

}