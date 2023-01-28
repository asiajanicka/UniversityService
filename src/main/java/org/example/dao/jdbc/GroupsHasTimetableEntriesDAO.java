package org.example.dao.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IGroupsHasTimetableEntriesDAO;
import org.example.model.GroupsHasTimetableEntry;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GroupsHasTimetableEntriesDAO implements IGroupsHasTimetableEntriesDAO {

    private static final String GET_ENTRY = "SELECT * FROM groups_has_time_table_entries WHERE id = ?";
    private static final String UPDATE_ENTRY = "UPDATE groups_has_time_table_entries SET student_group_id = ?, " +
            "time_table_entry_id = ? WHERE id = ?";
    private static final String CREATE_ENTRY = "INSERT INTO groups_has_time_table_entries (student_group_id," +
            " time_table_entry_id) VALUES (?, ?)";
    private static final String REMOVE_ENTRY = "DELETE FROM groups_has_time_table_entries WHERE id = ?";
    private static final String REMOVE_ENTITY_FROM_GROUP = "DELETE FROM groups_has_time_table_entries WHERE student_group_id = ? " +
            "AND time_table_entry_id = ?";
    private static final String GET_GROUP_IDS_BY_TIMETABLE_ENTITY_ID = "SELECT * FROM groups_has_time_table_entries " +
            "WHERE time_table_entry_id = ?";
    private static final String GET_TIMETABLE_ENTITY_IDS_BY_GROUP_ID = "SELECT * FROM groups_has_time_table_entries " +
            "WHERE student_group_id = ?";
    private static final Logger logger = LogManager.getLogger(GroupsHasTimetableEntriesDAO.class);

    @Override
    public Optional<GroupsHasTimetableEntry> getEntityById(long id) {
        String desc = "get 'group has timetable entity' by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_ENTRY)) {
            prepStmt.setLong(1, id);
            List<GroupsHasTimetableEntry> entries = RowMapper.mapToGroupHasTimetableEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return entries
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(GroupsHasTimetableEntry entity) {
        String desc = "update 'group has timetable entity' (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_ENTRY)) {
            prepStmt.setLong(1, entity.getGroupId());
            prepStmt.setLong(2, entity.getTimetableEntryId());
            prepStmt.setLong(3, entity.getGroupHasTimetableEntryId());
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
    public Optional<GroupsHasTimetableEntry> createEntity(GroupsHasTimetableEntry entity) {
        String desc = "create 'group has timetable entity' (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_ENTRY, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setLong(1, entity.getGroupId());
            prepStmt.setLong(2, entity.getTimetableEntryId());
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
        String desc = "remove 'group has timetable entity' by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_ENTRY)) {
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
    public int removeEntityById(long groupId, long ttEntityId) {
        String desc = "remove 'group has timetable entity'";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_ENTITY_FROM_GROUP)) {
            prepStmt.setLong(1, groupId);
            prepStmt.setLong(2, ttEntityId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Long> getStudentGroupIdsByTimetableEntryId(long ttEntryId) {
        String desc = "get student group ids by timetable entity id (%d)";
        List<Long> ids = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_GROUP_IDS_BY_TIMETABLE_ENTITY_ID)) {
            prepStmt.setLong(1, ttEntryId);
            List<GroupsHasTimetableEntry> groupsHasTimetableEntries = RowMapper.mapToGroupHasTimetableEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, ttEntryId));
            return groupsHasTimetableEntries
                    .stream()
                    .map(p -> p.getGroupId())
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, ttEntryId), e);
            e.printStackTrace();
        }
        return ids;
    }

    @Override
    public List<Long> getTimetableEntryIdsByGroupId(long groupId) {
        String desc = "get timetable entry ids by group id (%d)";
        List<Long> ids = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_TIMETABLE_ENTITY_IDS_BY_GROUP_ID)) {
            prepStmt.setLong(1, groupId);
            List<GroupsHasTimetableEntry> groupsHasTimetableEntries = RowMapper.mapToGroupHasTimetableEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, groupId));
            return groupsHasTimetableEntries
                    .stream()
                    .map(p -> p.getTimetableEntryId())
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, groupId), e);
            e.printStackTrace();
        }
        return ids;
    }

}