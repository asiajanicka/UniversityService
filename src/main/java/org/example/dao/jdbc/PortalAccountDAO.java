package org.example.dao.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IPortalAccountDAO;
import org.example.model.PortalAccount;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class PortalAccountDAO implements IPortalAccountDAO {

    private static final String GET_ACCOUNT = "SELECT * FROM portal_accounts WHERE id = ?";
    private static final String UPDATE_ACCOUNT = "UPDATE portal_accounts SET login = ?, password = ?," +
            " issue_date =?, expiry_date = ? WHERE id = ?";
    private static final String CREATE_ACCOUNT = "INSERT INTO portal_accounts (login, password, issue_date, expiry_date)" +
            " VALUES (?, ?, ?, ?)";
    private static final String REMOVE_ACCOUNT = "DELETE FROM portal_accounts WHERE id = ?";
    private static final String GET_ACCOUNT_BY_STUDENT_ID = "SELECT * FROM portal_accounts WHERE student_id = ?";
    private static final String BIND_ACCOUNT_TO_STUDENT = "UPDATE portal_accounts SET student_id = ? WHERE id = ?";
    private static final Logger logger = LogManager.getLogger(PortalAccountDAO.class);

    @Override
    public Optional<PortalAccount> getEntityById(long id) {
        String desc = "get portal account by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_ACCOUNT)) {
            prepStmt.setLong(1, id);
            List<PortalAccount> accounts = RowMapper.mapToPortalAccountEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return accounts
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(PortalAccount entity) {
        String desc = "update portal account (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_ACCOUNT)) {
            prepStmt.setString(1, entity.getLogin());
            prepStmt.setString(2, entity.getPassword());
            prepStmt.setDate(3, Date.valueOf(entity.getIssueDate()));
            prepStmt.setDate(4, Date.valueOf(entity.getExpiryDate()));
            prepStmt.setLong(5, entity.getId());
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, entity));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, entity), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Optional<PortalAccount> createEntity(PortalAccount entity) {
        String desc = "create portal account (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setString(1, entity.getLogin());
            prepStmt.setString(2, entity.getPassword());
            prepStmt.setDate(3, Date.valueOf(entity.getIssueDate()));
            prepStmt.setDate(4, Date.valueOf(entity.getExpiryDate()));
            if (prepStmt.executeUpdate() == 1) {
                logger.debug(String.format(EXECUTED_QUERY + desc, entity));
                ResultSet generatedKeys = prepStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return getEntityById(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, entity), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int removeEntity(long id) {
        String desc = "remove portal account by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_ACCOUNT)) {
            prepStmt.setLong(1, id);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Optional<PortalAccount> getAccountByStudentId(long studentId) {
        String desc = "get portal account by student id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_ACCOUNT_BY_STUDENT_ID)) {
            prepStmt.setLong(1, studentId);
            List<PortalAccount> accounts = RowMapper.mapToPortalAccountEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, studentId));
            return accounts
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, studentId), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int bindAccountToStudentId(long accountId, long studentId) {
        String desc = "bind portal account (id: %d) to student (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_ACCOUNT_TO_STUDENT)) {
            prepStmt.setLong(1, studentId);
            prepStmt.setLong(2, accountId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, accountId, studentId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, accountId, studentId), e);
            e.printStackTrace();
        }
        return 0;
    }

}
