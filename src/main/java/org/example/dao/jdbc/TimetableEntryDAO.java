package org.example.dao.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.ITimetableEntryDAO;
import org.example.model.TimetableEntry;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class TimetableEntryDAO implements ITimetableEntryDAO {

    private static final String GET_ENTRY = "SELECT * FROM time_table_entries WHERE id = ?";
    private static final String UPDATE_ENTRY = "UPDATE time_table_entries SET time = ?, week_day = ?, subject_id =?, " +
            "room_id = ? WHERE id = ?";
    private static final String CREATE_ENTRY = "INSERT INTO time_table_entries (time, week_day, subject_id, room_id) " +
            "VALUES (?, ?, ?, ?)";
    private static final String REMOVE_ENTRY = "DELETE FROM time_table_entries WHERE id = ?";
    private static final String GET_ENTRIES_BY_SUBJECT_ID = "SELECT * FROM time_table_entries WHERE subject_id = ?";
    private static final String GET_ENTRIES_BY_ROOM_ID = "SELECT * FROM time_table_entries WHERE room_id = ?";
    private static final String BIND_ENTRY_TO_SUBJECT = "UPDATE time_table_entries SET subject_id = ? WHERE id = ?";
    private static final String BIND_ENTRY_TO_ROOM = "UPDATE time_table_entries SET room_id = ? WHERE id = ?";
    private static final Logger logger = LogManager.getLogger(TimetableEntryDAO.class);

    @Override
    public Optional<TimetableEntry> getEntityById(long id) {
        String desc = "get timetable entry by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_ENTRY)) {
            prepStmt.setLong(1, id);
            List<TimetableEntry> timetable = RowMapper.mapToTimetableEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY_LOG_TEMPLATE + desc, id));
            return timetable
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTED_QUERY_LOG_TEMPLATE + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(TimetableEntry entity) {
        String desc = "update timetable entry (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_ENTRY)) {
            prepStmt.setString(1, entity.getTime().toString());
            prepStmt.setString(2, entity.getWeekDay().name());
            prepStmt.setLong(3, entity.getSubject().getId());
            prepStmt.setLong(4, entity.getRoom().getId());
            prepStmt.setLong(5, entity.getId());
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY_LOG_TEMPLATE + desc, entity));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTED_QUERY_LOG_TEMPLATE + desc, entity), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void createEntity(TimetableEntry entity) {
        String desc = "create timetable entry (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_ENTRY, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setTime(1, Time.valueOf(entity.getTime()));
            prepStmt.setString(2, entity.getWeekDay().name());
            prepStmt.setLong(3, entity.getSubject().getId());
            prepStmt.setLong(4, entity.getRoom().getId());
            if (prepStmt.executeUpdate() == 1) {
                logger.debug(String.format(EXECUTED_QUERY_LOG_TEMPLATE + desc, entity));
                ResultSet generatedKeys = prepStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTED_QUERY_LOG_TEMPLATE + desc, entity), e);
            e.printStackTrace();
        }
    }

    @Override
    public int removeEntity(long id) {
        String desc = "remove timetable entry by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_ENTRY)) {
            prepStmt.setLong(1, id);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY_LOG_TEMPLATE + desc, id));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTED_QUERY_LOG_TEMPLATE + desc, id), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<TimetableEntry> getTimetableBySubjectId(long subjectId) {
        String desc = "get timetable by subject id (id: %d)";
        List<TimetableEntry> timetable = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_ENTRIES_BY_SUBJECT_ID)) {
            prepStmt.setLong(1, subjectId);
            timetable = RowMapper.mapToTimetableEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY_LOG_TEMPLATE + desc, subjectId));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTED_QUERY_LOG_TEMPLATE + desc, subjectId), e);
            e.printStackTrace();
        }
        return timetable;
    }

    @Override
    public List<TimetableEntry> getTimetableByRoomId(long roomId) {
        String desc = "get timetable entry by room id (id: %d)";
        List<TimetableEntry> timetable = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_ENTRIES_BY_ROOM_ID)) {
            prepStmt.setLong(1, roomId);
            timetable = RowMapper.mapToTimetableEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY_LOG_TEMPLATE + desc, roomId));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTED_QUERY_LOG_TEMPLATE + desc, roomId), e);
            e.printStackTrace();
        }
        return timetable;
    }

    @Override
    public int bindSubjectToTimetableEntry(long subjectId, long timetableEntryId) {
        String desc = "bind timetable entry (id: %d) to subject (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_ENTRY_TO_SUBJECT)) {
            prepStmt.setLong(1, subjectId);
            prepStmt.setLong(2, timetableEntryId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY_LOG_TEMPLATE + desc, timetableEntryId, subjectId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTED_QUERY_LOG_TEMPLATE + desc, timetableEntryId, subjectId), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int bindRoomToTimetableEntry(long roomId, long timetableEntryId) {
        String desc = "bind timetable entry (id: %d) to room (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_ENTRY_TO_ROOM)) {
            prepStmt.setLong(1, roomId);
            prepStmt.setLong(2, timetableEntryId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY_LOG_TEMPLATE + desc, timetableEntryId, roomId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTED_QUERY_LOG_TEMPLATE + desc, timetableEntryId, roomId), e);
            e.printStackTrace();
        }
        return 0;
    }

}