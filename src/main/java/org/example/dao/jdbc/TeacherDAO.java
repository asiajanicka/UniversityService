package org.example.dao.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.ITeacherDAO;
import org.example.model.Teacher;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class TeacherDAO implements ITeacherDAO {

    private static final String GET_TEACHER = "SELECT * FROM teachers WHERE id = ?";
    private static final String UPDATE_TEACHER = "UPDATE teachers SET first_name = ?, last_name = ? WHERE id = ?";
    private static final String CREATE_TEACHER = "INSERT INTO teachers (first_name, last_name) VALUES (?, ?)";
    private static final String REMOVE_TEACHER = "DELETE FROM teachers WHERE id = ?";
    private static final String GET_TEACHERS_BY_DEPT_ID = "SELECT * FROM teachers WHERE department_id = ?";
    private static final String BIND_TEACHER_TO_DEPT = "UPDATE teachers SET department_id = ? WHERE id = ?";
    private static final String REMOVE_TEACHER_FROM_DEPT = "UPDATE teachers SET department_id = null WHERE id = ?";
    private static final Logger logger = LogManager.getLogger(TeacherDAO.class);

    @Override
    public Optional<Teacher> getEntityById(long id) {
        String desc = "get teacher by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_TEACHER)) {
            prepStmt.setLong(1, id);
            List<Teacher> teachers = RowMapper.mapToTeacherEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return teachers
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(Teacher entity) {
        String desc = "update teacher (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_TEACHER)) {
            prepStmt.setString(1, entity.getFirstName());
            prepStmt.setString(2, entity.getLastName());
            prepStmt.setLong(3, entity.getId());
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
    public Optional<Teacher> createEntity(Teacher entity) {
        String desc = "create teacher (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_TEACHER, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setString(1, entity.getFirstName());
            prepStmt.setString(2, entity.getLastName());
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
        String desc = "remove teacher by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_TEACHER)) {
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
    public List<Teacher> getTeachersByDeptId(long deptId) {
        String desc = "get all teachers by dept id (id: %d)";
        List<Teacher> teachers = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_TEACHERS_BY_DEPT_ID)) {
            prepStmt.setLong(1, deptId);
            teachers.addAll(RowMapper.mapToTeacherEntityList(prepStmt.executeQuery()));
            logger.debug(String.format(EXECUTED_QUERY + desc, deptId));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, deptId), e);
            e.printStackTrace();
        }
        return teachers;
    }

    @Override
    public int bindTeacherToDeptId(long teacherId, long deptId) {
        String desc = "bind teacher (id: %d) to department (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_TEACHER_TO_DEPT)) {
            prepStmt.setLong(1, deptId);
            prepStmt.setLong(2, teacherId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, teacherId, deptId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, teacherId, deptId), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int removeTeacherFromDeptById(long teacherId, long deptId) {
        String desc = "remove teacher (id: %d) from department (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_TEACHER_FROM_DEPT)) {
            prepStmt.setLong(1, teacherId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, teacherId, deptId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, teacherId, deptId), e);
            e.printStackTrace();
        }
        return 0;
    }

}
