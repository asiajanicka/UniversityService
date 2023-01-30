package org.example.dao.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IStudentDAO;
import org.example.model.Student;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class StudentDAO implements IStudentDAO {

    private static final String GET_STUDENT = "SELECT * FROM students WHERE id = ?";
    private static final String UPDATE_STUDENT = "UPDATE students SET first_name = ?, last_name = ?, " +
            "date_of_birth =? WHERE id = ?";
    private static final String CREATE_STUDENT = "INSERT INTO students(first_name, last_name, date_of_birth)" +
            " VALUES (?, ?, ?)";
    private static final String REMOVE_STUDENT = "DELETE FROM students WHERE id = ?";
    private static final String GET_ALL_STUDENTS_BY_GROUP_ID = "SELECT * FROM students WHERE student_group_id = ?";
    private static final String BIND_STUDENT_TO_GROUP = "UPDATE students SET student_group_id = ? WHERE id = ?";
    private static final String REMOVE_STUDENT_FROM_GROUP = "UPDATE students SET student_group_id = null WHERE id = ?";
    private static final Logger logger = LogManager.getLogger(StudentDAO.class);

    @Override
    public Optional<Student> getEntityById(long id) {
        String desc = "get student by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_STUDENT)) {
            prepStmt.setLong(1, id);
            List<Student> students = RowMapper.mapToStudentEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return students
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(Student entity) {
        String desc = "update student (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_STUDENT)) {
            prepStmt.setString(1, entity.getFirstName());
            prepStmt.setString(2, entity.getLastName());
            prepStmt.setDate(3, Date.valueOf(entity.getDateOfBirth()));
            prepStmt.setLong(4, entity.getId());
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
    public void createEntity(Student entity) {
        String desc = "create student (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_STUDENT, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setString(1, entity.getFirstName());
            prepStmt.setString(2, entity.getLastName());
            prepStmt.setDate(3, Date.valueOf(entity.getDateOfBirth()));
            if (prepStmt.executeUpdate() == 1) {
                logger.debug(String.format(EXECUTED_QUERY + desc, entity));
                ResultSet generatedKeys = prepStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                   entity.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, entity), e);
            e.printStackTrace();
        }
    }

    @Override
    public int removeEntity(long id) {
        String desc = "remove student by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_STUDENT)) {
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
    public List<Student> getAllStudentsByGroupId(long groupId) {
        String desc = "get all students by group id (id: %d)";
        List<Student> students = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_ALL_STUDENTS_BY_GROUP_ID)) {
            prepStmt.setLong(1, groupId);
            students.addAll(RowMapper.mapToStudentEntityList(prepStmt.executeQuery()));
            logger.debug(String.format(EXECUTED_QUERY + desc, groupId));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, groupId), e);
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public int bindStudentToGroupById(long studentId, long groupId) {
        String desc = "bind student (id: %d) to group (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_STUDENT_TO_GROUP)) {
            prepStmt.setLong(1, groupId);
            prepStmt.setLong(2, studentId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, studentId, groupId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, studentId, groupId), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int removeStudentFromGroupById(long studentId, long groupId) {
        String desc = "remove student (id: %d) from group (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_STUDENT_FROM_GROUP)) {
            prepStmt.setLong(1, studentId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, studentId, groupId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, studentId, groupId), e);
            e.printStackTrace();
        }
        return 0;
    }

}
