package org.example.dao;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.ISubjectDAO;
import org.example.model.Subject;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class SubjectDAO implements ISubjectDAO {

    private static final String GET_SUBJECT = "SELECT * FROM subjects WHERE id = ?";
    private static final String UPDATE_SUBJECT = "UPDATE subjects SET name = ? WHERE id = ?";
    private static final String CREATE_SUBJECT = "INSERT INTO subjects (name) VALUES (?)";
    private static final String REMOVE_SUBJECT = "DELETE FROM subjects WHERE id = ?";
    private static final String BIND_SUBJECT_TO_TEACHER = "UPDATE subjects SET teacher_id = ? WHERE id = ?";
    private static final String GET_SUBJECTS_BY_TEACHER = "SELECT * FROM subjects WHERE teacher_id = ?";
    private static final String GET_SUBJECTS_WITHOUT_TEACHER = "SELECT * FROM subjects WHERE teacher_id is null";
    private static final Logger logger = LogManager.getLogger(SubjectDAO.class);

    @Override
    public Optional<Subject> getEntityById(long id) {
        String desc = "get subject by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_SUBJECT)) {
            prepStmt.setLong(1, id);
            List<Subject> subjects = RowMapper.mapToSubjectEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return subjects
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(Subject entity) {
        String desc = "update subject (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_SUBJECT)) {
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
    public Optional<Subject> createEntity(Subject entity) {
        String desc = "create subject (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_SUBJECT, Statement.RETURN_GENERATED_KEYS)) {
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
        String desc = "remove subject by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_SUBJECT)) {
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
    public int bindSubjectToTeacherId(long subjectId, long teacherId) {
        String desc = "bind subject (id: %d) to teacher (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_SUBJECT_TO_TEACHER)) {
            prepStmt.setLong(1, teacherId);
            prepStmt.setLong(2, subjectId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, subjectId, teacherId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, subjectId, teacherId), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int removedTeacherFromSubject(long subjectId) {
        String desc = "remove teacher from subject (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_SUBJECT_TO_TEACHER)) {
            prepStmt.setString(1, null);
            prepStmt.setLong(2, subjectId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, subjectId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, subjectId), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Subject> getSubjectsByTeacherId(long teacherId) {
        String desc = "get all subjects by teacher id (id: %d)";
        List<Subject> subjects = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_SUBJECTS_BY_TEACHER)) {
            prepStmt.setLong(1, teacherId);
            subjects.addAll(RowMapper.mapToSubjectEntityList(prepStmt.executeQuery()));
            logger.debug(String.format(EXECUTED_QUERY + desc, teacherId));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, teacherId), e);
            e.printStackTrace();
        }
        return subjects;
    }

    @Override
    public List<Subject> getSubjectsWithoutTeacher() {
        String desc = "get all subjects without teacher assigned";
        List<Subject> subjects = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_SUBJECTS_WITHOUT_TEACHER)) {
            subjects.addAll(RowMapper.mapToSubjectEntityList(prepStmt.executeQuery()));
            logger.debug(String.format(EXECUTED_QUERY + desc));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc), e);
            e.printStackTrace();
        }
        return subjects;
    }

}
