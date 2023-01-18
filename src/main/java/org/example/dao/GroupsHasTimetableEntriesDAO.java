package org.example.dao;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IGroupsHasTimetableEntriesDAO;
import org.example.model.GroupsHasTimetableEntry;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class GroupsHasTimetableEntriesDAO implements IGroupsHasTimetableEntriesDAO {

    private static final String GET_ENTRY = "SELECT * FROM groups_has_time_table_entries WHERE id = ?";
    private static final String UPDATE_ENTRY = "UPDATE groups_has_time_table_entries SET student_group_id = ?, " +
            "time_table_entry_id = ? WHERE id = ?";
    private static final String CREATE_ENTRY = "INSERT INTO groups_has_time_table_entries (student_group_id," +
            " time_table_entry_id) VALUES (?, ?)";
    private static final String REMOVE_ENTRY = "DELETE FROM groups_has_time_table_entries WHERE id = ?";
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

}
