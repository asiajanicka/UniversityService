package org.example.dao;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IParkingSpotDAO;
import org.example.model.ParkingSpot;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class ParkingSpotDAO implements IParkingSpotDAO {

    private static final String GET_SPOT = "SELECT * FROM parking_spots WHERE id = ?";
    private static final String UPDATE_SPOT = "UPDATE parking_spots SET name = ?, address = ? WHERE id = ?";
    private static final String CREATE_SPOT = "INSERT INTO parking_spots (name, address) VALUES (?, ?)";
    private static final String REMOVE_SPOT = "DELETE FROM parking_spots WHERE id = ?";
    private static final String GET_SPOT_BY_TEACHER_ID = "SELECT * FROM parking_spots WHERE teacher_id = ?";
    private static final String BIND_SPOT_TO_TEACHER = "UPDATE parking_spots SET teacher_id = ? WHERE id = ?";
    private static final String SET_SPOT_FREE = "UPDATE parking_spots SET teacher_id = null WHERE id = ?";
    private static final String GET_FREE_SPOTS = "SELECT * FROM parking_spots WHERE teacher_id is NULL";
    private static final Logger logger = LogManager.getLogger(ParkingSpotDAO.class);

    @Override
    public Optional<ParkingSpot> getEntityById(long id) {
        String desc = "get parking spot by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_SPOT)) {
            prepStmt.setLong(1, id);
            List<ParkingSpot> spots = RowMapper.mapToParkingSpotEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return spots
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(ParkingSpot entity) {
        String desc = "update parking spot (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_SPOT)) {
            prepStmt.setString(1, entity.getName());
            prepStmt.setString(2, entity.getAddress());
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
    public Optional<ParkingSpot> createEntity(ParkingSpot entity) {
        String desc = "create parking spot (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_SPOT, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setString(1, entity.getName());
            prepStmt.setString(2, entity.getAddress());
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
        String desc = "remove parking spot by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_SPOT)) {
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
    public Optional<ParkingSpot> getSpotByTeacherId(long teacherId) {
        String desc = "get parking spot by teacher id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_SPOT_BY_TEACHER_ID)) {
            prepStmt.setLong(1, teacherId);
            List<ParkingSpot> spots = RowMapper.mapToParkingSpotEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, teacherId));
            return spots
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, teacherId), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int bindSpotToTeacherId(long spotId, long teacherId) {
        String desc = "bind parking spot (id: %d) to teacher (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_SPOT_TO_TEACHER)) {
            prepStmt.setLong(1, teacherId);
            prepStmt.setLong(2, spotId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, spotId, teacherId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, spotId, teacherId), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int setSpotFree(long spotId) {
        String desc = "set parking spot (id: %d) free";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(SET_SPOT_FREE)) {
            prepStmt.setLong(1, spotId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, spotId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, spotId), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<ParkingSpot> getFreeSpots() {
        String desc = "get all free parking spots";
        List<ParkingSpot> spots = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_FREE_SPOTS)) {
            spots.addAll(RowMapper.mapToParkingSpotEntityList(prepStmt.executeQuery()));
            logger.debug(EXECUTED_QUERY + desc);
        } catch (SQLException e) {
            logger.error(NOT_EXECUTE_QUERY + desc, e);
            e.printStackTrace();
        }
        return spots;
    }

}
