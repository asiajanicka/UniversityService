package org.example.dao.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IStudentGroupDAO;
import org.example.model.StudentGroup;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class StudentGroupDAO implements IStudentGroupDAO {

    private static final String GET_STUDENT_GROUP = "SELECT * FROM student_groups WHERE id = ?";
    private static final String UPDATE_STUDENT_GROUP = "UPDATE student_groups SET name = ? WHERE id = ?";
    private static final String CREATE_STUDENT_GROUP = "INSERT INTO student_groups(name) VALUES (?)";
    private static final String REMOVE_STUDENT_GROUP = "DELETE FROM student_groups WHERE id = ?";
    private static final Logger logger = LogManager.getLogger(StudentGroupDAO.class);

    @Override
    public Optional<StudentGroup> getEntityById(long id) {
        String desc = "get student group by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_STUDENT_GROUP)) {
            prepStmt.setLong(1, id);
            List<StudentGroup> studentGroups = RowMapper.mapToStudentGroupEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return studentGroups
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(StudentGroup entity) {
        String desc = "update student group (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_STUDENT_GROUP)) {
            prepStmt.setString(1, entity.getName());
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
    public Optional<StudentGroup> createEntity(StudentGroup entity) {
        String desc = "create student group (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_STUDENT_GROUP, Statement.RETURN_GENERATED_KEYS)) {
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
        }
        return Optional.empty();
    }

    @Override
    public int removeEntity(long id) {
        String desc = "remove student group by id (%d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_STUDENT_GROUP)) {
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

}
