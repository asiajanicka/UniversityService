package org.example.dao.jdbc;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IRoomDAO;
import org.example.model.Room;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class RoomDAO implements IRoomDAO {

    private static final String GET_ROOM = "SELECT * FROM rooms WHERE id = ?";
    private static final String UPDATE_ROOM = "UPDATE rooms SET room_number = ?, building_id = ? WHERE id = ?";
    private static final String CREATE_ROOM = "INSERT INTO rooms (room_number, building_id) VALUES (?, ?)";
    private static final String REMOVE_ROOM = "DELETE FROM rooms WHERE id = ?";
    private static final String GET_ROOMS_BY_BUILDING_ID = "SELECT * FROM rooms WHERE building_id = ?";
    private static final String BIND_ROOM_TO_BUILDING = "UPDATE rooms SET building_id = ? WHERE id = ?";
    private static final String REMOVE_ROOMS_FROM_BUILDING = "DELETE FROM rooms WHERE building_id = ?";
    private static final Logger logger = LogManager.getLogger(PortalAccountDAO.class);

    @Override
    public Optional<Room> getEntityById(long id) {
        String desc = "get room by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_ROOM)) {
            prepStmt.setLong(1, id);
            List<Room> rooms = RowMapper.mapToRoomEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return rooms
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(Room entity) {
        String desc = "update room (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_ROOM)) {
            prepStmt.setString(1, entity.getNumber());
            prepStmt.setLong(2, entity.getBuilding().getId());
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
    public Optional<Room> createEntity(Room entity) {
        String desc = "create room (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_ROOM, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setString(1, entity.getNumber());
            prepStmt.setLong(2, entity.getBuilding().getId());
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
        String desc = "remove room by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_ROOM)) {
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
    public int bindRoomToBuildingId(long roomId, long buildingId) {
        String desc = "bind room (id: %d) to building (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_ROOM_TO_BUILDING)) {
            prepStmt.setLong(1, buildingId);
            prepStmt.setLong(2, roomId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, roomId, buildingId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, roomId, buildingId), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Room> getRoomsByBuildingId(long buildingId) {
        String desc = "get rooms by building id (id: %d)";
        List<Room> rooms = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_ROOMS_BY_BUILDING_ID)) {
            prepStmt.setLong(1, buildingId);
            rooms = RowMapper.mapToRoomEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, buildingId));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, buildingId), e);
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public int removeRoomsByBuildingId(long buildingId) {
        String desc = "remove rooms by building id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_ROOMS_FROM_BUILDING)) {
            prepStmt.setLong(1, buildingId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, buildingId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, buildingId), e);
            e.printStackTrace();
        }
        return 0;
    }

}
