package org.example.dao.interfaces;

import org.example.model.PortalAccount;

import java.util.Optional;

public interface IPortalAccountDAO extends IBaseDAO<PortalAccount> {

    Optional<PortalAccount> getAccountByStudentId(long id);

    int bindAccountToStudentId(long account_id, long student_id);

}
