package org.example.dao;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IBuildingDAO;
import org.example.model.Building;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class BuildingDAO implements IBuildingDAO {

    private static final String GET_BUILDING = "SELECT * FROM buildings WHERE id = ?";
    private static final String UPDATE_BUILDING = "UPDATE buildings SET name = ?, address = ? WHERE id = ?";
    private static final String CREATE_BUILDING = "INSERT INTO buildings (name, address) VALUES (?, ?)";
    private static final String REMOVE_BUILDING = "DELETE FROM buildings WHERE id = ?";
    private static final Logger logger = LogManager.getLogger(BuildingDAO.class);

    @Override
    public Optional<Building> getEntityById(long id) {
        String desc = "get building by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_BUILDING)) {
            prepStmt.setLong(1, id);
            List<Building> teachers = RowMapper.mapToBuildingEntityList(prepStmt.executeQuery());
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
    public int updateEntity(Building entity) {
        String desc = "update building (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_BUILDING)) {
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
    public Optional<Building> createEntity(Building entity) {
        String desc = "create building (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_BUILDING, Statement.RETURN_GENERATED_KEYS)) {
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
        String desc = "remove building by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_BUILDING)) {
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
