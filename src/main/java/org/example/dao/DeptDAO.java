package org.example.dao;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.interfaces.IDeptDAO;
import org.example.model.Department;
import org.example.utils.ConnectionPool;
import org.example.utils.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class DeptDAO implements IDeptDAO {

    private static final String GET_DEPT = "SELECT * FROM departments WHERE id = ?";
    private static final String UPDATE_DEPT = "UPDATE departments SET name = ? WHERE id = ?";
    private static final String CREATE_DEPT = "INSERT INTO departments (name) VALUES (?)";
    private static final String REMOVE_DEPT = "DELETE FROM departments WHERE id = ?";
    private static final String GET_DEPTS_BY_BUILDING_ID = "SELECT * FROM departments WHERE building_id = ?";
    private static final String BIND_DEPT_TO_BUILDING = "UPDATE departments SET building_id = ? WHERE id = ?";
    private static final String REMOVE_DEPT_FROM_BUILDING = "UPDATE departments SET building_id = null WHERE id = ?";
    private static final Logger logger = LogManager.getLogger(DeptDAO.class);

    @Override
    public Optional<Department> getEntityById(long id) {
        String desc = "get department by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_DEPT)) {
            prepStmt.setLong(1, id);
            List<Department> depts = RowMapper.mapToDepartmentEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, id));
            return depts
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, id), e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int updateEntity(Department entity) {
        String desc = "update department (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(UPDATE_DEPT)) {
            prepStmt.setString(1, entity.getName());
            prepStmt.setLong(2, entity.getId());
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
    public Optional<Department> createEntity(Department entity) {
        String desc = "create department (%s)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(CREATE_DEPT, Statement.RETURN_GENERATED_KEYS)) {
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
        String desc = "remove department by id (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_DEPT)) {
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
    public List<Department> getDepartmentsByBuildingId(long buildingId) {
        String desc = "get departments by student id (id: %d)";
        List<Department> depts = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(GET_DEPTS_BY_BUILDING_ID)) {
            prepStmt.setLong(1, buildingId);
            depts = RowMapper.mapToDepartmentEntityList(prepStmt.executeQuery());
            logger.debug(String.format(EXECUTED_QUERY + desc, buildingId));
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, buildingId), e);
            e.printStackTrace();
        }
        return depts;
    }

    @Override
    public int bindDepartmentToBuildingId(long deptId, long buildingId) {
        String desc = "bind department (id: %d) to building (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(BIND_DEPT_TO_BUILDING)) {
            prepStmt.setLong(1, buildingId);
            prepStmt.setLong(2, deptId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, deptId, buildingId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, deptId, buildingId), e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int removeDepartmentFromBuildingById(long deptId, long buildingId) {
        String desc = "remove department (id: %d) from building (id: %d)";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStmt = con.prepareStatement(REMOVE_DEPT_FROM_BUILDING)) {
            prepStmt.setLong(1, deptId);
            int result = prepStmt.executeUpdate();
            logger.debug(String.format(EXECUTED_QUERY + desc, deptId, buildingId));
            return result;
        } catch (SQLException e) {
            logger.error(String.format(NOT_EXECUTE_QUERY + desc, deptId, buildingId), e);
            e.printStackTrace();
        }
        return 0;
    }

}
