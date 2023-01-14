package org.example.dao;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.ISubjectDAO;
import org.example.model.Subject;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class SubjectDAO implements ISubjectDAO {

    private static final String GET_SUBJECT = "SELECT * FROM subjects WHERE id = ?";
    private static final String UPDATE_SUBJECT = "UPDATE subjects SET name = ? WHERE id = ?";
    private static final String CREATE_SUBJECT = "INSERT INTO subjects (name) VALUES (?)";
    private static final String REMOVE_SUBJECT = "DELETE FROM subjects WHERE id = ?";
    private static final String BIND_SUBJECT_TO_TEACHER = "UPDATE subjects SET teacher_id = ? WHERE id = ?";
    private static final Logger logger = LogManager.getLogger(SubjectDAO.class);
    private Connection con;

    @Override
    public Optional<Subject> getEntityById(long id) {
        String desc = "get subject by id (id: %d)";
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(String.format(CONNECTED_DB + desc, id));
            try (PreparedStatement prepStmt = con.prepareStatement(GET_SUBJECT)) {
                prepStmt.setLong(1, id);
                List<Subject> subjects = RowMapper.mapToSubjectEntityList(prepStmt.executeQuery());
                logger.debug(String.format(EXECUTED_QUERY + desc, id));
                return subjects
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
    public int updateEntity(Subject entity) {
        String desc = "update subject (%s)";
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(String.format(CONNECTED_DB + desc, entity));
            try (PreparedStatement prepStmt = con.prepareStatement(UPDATE_SUBJECT)) {
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
    public Optional<Subject> createEntity(Subject entity) {
        String desc = "create subject (%s)";
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(String.format(CONNECTED_DB + desc, entity));
            try (PreparedStatement prepStmt = con.prepareStatement(CREATE_SUBJECT, Statement.RETURN_GENERATED_KEYS)) {
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
        String desc = "remove subject by id (id: %d)";
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(String.format(CONNECTED_DB + desc, id));
            try (PreparedStatement prepStmt = con.prepareStatement(REMOVE_SUBJECT)) {
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
    public int bindSubjectToTeacherId(long subjectId, long teacherId) {
        String desc = "bind subject (id: %d) to teacher (id: %d)";
        try {
            con = ConnectionPool.getInstance().getConnection();
            logger.debug(String.format(CONNECTED_DB + desc, subjectId, teacherId));
            try (PreparedStatement prepStmt = con.prepareStatement(BIND_SUBJECT_TO_TEACHER)) {
                prepStmt.setLong(1, teacherId);
                prepStmt.setLong(2, subjectId);
                int result = prepStmt.executeUpdate();
                logger.debug(String.format(EXECUTED_QUERY + desc, subjectId, teacherId));
                return result;
            } catch (SQLException e) {
                logger.error(String.format(NOT_EXECUTE_QUERY + desc, subjectId, teacherId), e);
                e.printStackTrace();
            } finally {
                con.close();
                logger.debug(String.format(CLOSED_CON_DB + desc, subjectId, teacherId));
            }
        } catch (SQLException e) {
            logger.error(String.format(NOT_CONNECT_DB + desc, subjectId, teacherId), e);
            e.printStackTrace();
        }
        return 0;
    }
}
