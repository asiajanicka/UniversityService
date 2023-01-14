package org.example.dao.interfaces;

import java.util.Optional;

public interface IBaseDAO<T> {

    String CONNECTED_DB = "Connected to DB to ";
    String NOT_CONNECT_DB = "Couldn't get connection to DB to ";
    String CLOSED_CON_DB = "Closed connection to DB after ";
    String EXECUTED_QUERY = "Executed query to DB to ";
    String NOT_EXECUTE_QUERY = "Couldn't get connection to DB to ";


    Optional<T> getEntityById(long id);

    int updateEntity(T entity);

    Optional<T> createEntity(T entity);

    int removeEntity(long id);

}
