package org.example.dao;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IStudentGroupDAO;
import org.example.model.StudentGroup;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class StudentGroupDAO implements IStudentGroupDAO {

    private static final String GET_STUDENT_GROUP = "SELECT * FROM student_groups WHERE id = ?";
    private static final String UPDATE_STUDENT_GROUP = "UPDATE student_groups SET name = ? WHERE id = ?";
    private static final String CREATE_STUDENT_GROUP = "INSERT INTO student_groups(name) VALUES (?)";
    private static final String REMOVE_STUDENT_GROUP = "DELETE FROM student_groups WHERE id = ?";
    private static final String GET_ALL_STUDENT_GROUPS = "SELECT * FROM student_groups";
    private static final Logger logger = LogManager.getLogger(StudentGroupDAO.class);
    private Connection con;

    @Override
    public Optional<StudentGroup> getEntityById(long id) {
        String desc = "get student group by id (id: %d)";
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(String.format(CONNECTED_DB + desc, id));
            try (PreparedStatement prepStmt = con.prepareStatement(GET_STUDENT_GROUP)) {
                prepStmt.setLong(1, id);
                List<StudentGroup> studentGroups = RowMapper.mapToStudentGroupEntityList(prepStmt.executeQuery());
                logger.debug(String.format(EXECUTED_QUERY + desc, id));
                return studentGroups
                        .stream()
                        .findFirst();
            } catch (SQLException e) {
                logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
                e.printStackTrace();
            } finally {
                con.close();
                logger.debug(String.format(CLOSED_CON_DB + desc, id));
            }
        } catch (SQLException e) {
            logger.error(String.format(NOT_CONNECT_DB + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(StudentGroup entity) {
        String desc = "update student group (%s)";
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(String.format(CONNECTED_DB + desc, entity));
            try (PreparedStatement prepStmt = con.prepareStatement(UPDATE_STUDENT_GROUP)) {
                prepStmt.setString(1, entity.getName());
                int result = prepStmt.executeUpdate();
                logger.debug(String.format(EXECUTED_QUERY + desc, entity));
                return result;
            } catch (SQLException e) {
                logger.error(String.format(NOT_EXECUTE_QUERY + desc, entity), e);
                e.printStackTrace();
            } finally {
                con.close();
                logger.debug(String.format(CLOSED_CON_DB + desc, entity));
            }
        } catch (SQLException e) {
            logger.error(String.format(NOT_CONNECT_DB + desc, entity), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Optional<StudentGroup> createEntity(StudentGroup entity) {
        String desc = "create student group (%s)";
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(String.format(CONNECTED_DB + desc, entity));
            try (PreparedStatement prepStmt = con.prepareStatement(CREATE_STUDENT_GROUP, Statement.RETURN_GENERATED_KEYS)) {
                prepStmt.setString(1, entity.getName());
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
            } finally {
                con.close();
                logger.debug(String.format(CLOSED_CON_DB + desc, entity));
            }
        } catch (SQLException e) {
            logger.error(String.format(NOT_CONNECT_DB + desc, entity), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int removeEntity(long id) {
        String desc = "remove student group by id (%d)";
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(String.format(CONNECTED_DB + desc, id));
            try (PreparedStatement prepStmt = con.prepareStatement(REMOVE_STUDENT_GROUP)) {
                prepStmt.setLong(1, id);
                int result = prepStmt.executeUpdate();
                logger.debug(String.format(EXECUTED_QUERY + desc, id));
                return result;
            } catch (SQLException e) {
                logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
                e.printStackTrace();
            } finally {
                con.close();
                logger.debug(String.format(CLOSED_CON_DB + desc, id));
            }
        } catch (SQLException e) {
            logger.error(String.format(NOT_CONNECT_DB + desc, id), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<StudentGroup> getAllGroups() {
        String desc = "get all student groups";
        List<StudentGroup> groups = new ArrayList<>();
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(CONNECTED_DB + desc);
            try (PreparedStatement prepStmt = con.prepareStatement(GET_ALL_STUDENT_GROUPS)) {
                groups.addAll(RowMapper.mapToStudentGroupEntityList(prepStmt.executeQuery()));
                logger.debug(EXECUTED_QUERY + desc);
            } catch (SQLException e) {
                logger.error(NOT_EXECUTE_QUERY + desc, e);
                e.printStackTrace();
            } finally {
                con.close();
                logger.debug(CLOSED_CON_DB + desc);
            }
        } catch (SQLException e) {
            logger.error(NOT_CONNECT_DB + desc, e);
            e.printStackTrace();
        }
        return groups;
    }

}
