package org.example.e2eTests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.PortalAccount;
import org.example.model.Student;
import org.example.service.PortalAccountService;
import org.example.service.StudentService;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.exception.NoEntityCreatedException;
import org.junit.jupiter.api.Test;
import utils.TestData;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PortalAccountServiceTests {

    private static final Logger logger = LogManager.getLogger(PortalAccountServiceTests.class);

    @Test
    public void test1() throws NoEntityCreatedException, EntityNotFoundException {

//    Add a new student
//    Add a new portal account
//    Assign portal account to student
//    Change password for account
//    Change exp date for account
//	    - check changes (read account)
//    Remove portal account
//	    - check is portal account is removed
//	    - check is student doesn't have any portal account
//    Remove student from uni

        logger.info("Start of Portal Account Service Tests - test case 1");
        StudentService studentService = new StudentService();
        PortalAccount expectedAccount = TestData.getPortalAccount();
        PortalAccountService accountService = new PortalAccountService();

        Student student = studentService.addNewStudent(TestData.getBasicStudent());
        PortalAccount account = accountService.addNewAccount(expectedAccount);
        assertThat(accountService.bindAccountToStudent(account, student)).isTrue();

        String newPassword = "new_password";
        LocalDate newExpDate = LocalDate.now().plusYears(4);
        assertThat(accountService.changePasswordForStudent(student, newPassword)).isTrue();
        assertThat(accountService.changeExpDateForStudent(student, newExpDate)).isTrue();

        Student studentWithAccount = studentService.getStudentById(student.getId());
        assertThat(studentWithAccount.getPortalAccount().getPassword()).isEqualTo(newPassword);
        assertThat(studentWithAccount.getPortalAccount().getExpiryDate()).isEqualTo(newExpDate);

        assertThat(accountService.removeAccount(account)).isTrue();
        assertThatThrownBy(() -> accountService.getAccountById(account.getId())).isInstanceOf(EntityNotFoundException.class);
        assertThat(studentService.getStudentById(student.getId()).getPortalAccount()).isNull();

        studentService.removeStudent(student);
        logger.info("End of Portal Account Service Tests - test case 1");

    }

}
