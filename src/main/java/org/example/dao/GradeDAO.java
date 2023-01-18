package org.example.dao;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IGradeDAO;
import org.example.model.Grade;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class GradeDAO implements IGradeDAO {

    private static final String GET_GRADE = "SELECT * FROM grades WHERE id = ?";
    private static final String UPDATE_GRADE = "UPDATE grades SET value = ?, subject_id = ? WHERE id = ?";
    private static final String CREATE_GRADE = "INSERT INTO grades (value, subject_id) VALUES (?, ?)";
    private static final String REMOVE_GRADE = "DELETE FROM grades WHERE id = ?";
    private static final String GET_GRADES_BY_STUDENT_ID = "SELECT * FROM grades WHERE student_id = ?";
    private static final String GET_GRADES_BY_SUBJECT_ID = "SELECT * FROM grades WHERE subject_id = ?";
    private static final String BIND_GRADE_TO_STUDENT = "UPDATE grades SET student_id = ? WHERE id = ?";
    private static final Logger logger = LogManager.getLogger(GradeDAO.class);

    @Override
    public Optional<Grade> getEntityById(long id) {
        String desc = "get grade by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_GRADE)) {
            prepStmt.setLong(1, id);
            List<Grade> grades = RowMapper.mapToGradeEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return grades
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(Grade entity) {
        String desc = "update grade (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_GRADE)) {
            prepStmt.setInt(1, entity.getValue());
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
    public Optional<Grade> createEntity(Grade entity) {
        String desc = "create grade (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_GRADE, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setInt(1, entity.getValue());
            prepStmt.setLong(2, entity.getSubject().getId());
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
        String desc = "remove grade by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_GRADE)) {
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
    public List<Grade> getAllGradesByStudentId(long studentId) {
        String desc = "get all grades by student id (id: %d)";
        List<Grade> grades = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_GRADES_BY_STUDENT_ID)) {
            prepStmt.setLong(1, studentId);
            grades.addAll(RowMapper.mapToGradeEntityList(prepStmt.executeQuery()));
            logger.debug(String.format(EXECUTED_QUERY + desc, studentId));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, studentId), e);
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> getAllGradesBySubjectId(long subjectId) {
        String desc = "get all grades by student id (id: %d)";
        List<Grade> grades = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_GRADES_BY_SUBJECT_ID)) {
            prepStmt.setLong(1, subjectId);
            grades.addAll(RowMapper.mapToGradeEntityList(prepStmt.executeQuery()));
            logger.debug(String.format(EXECUTED_QUERY + desc, subjectId));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, subjectId), e);
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public int bindGradeToStudentId(long gradeId, long studentId) {
        String desc = "bind grade (id: %d) to student (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_GRADE_TO_STUDENT)) {
            prepStmt.setLong(1, studentId);
            prepStmt.setLong(2, gradeId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, gradeId, studentId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, gradeId, studentId), e);
            e.printStackTrace();
        }
        return 0;
    }

}
