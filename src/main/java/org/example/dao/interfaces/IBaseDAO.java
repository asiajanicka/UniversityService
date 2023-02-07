package org.example.dao.interfaces;

import java.util.Optional;

public interface IBaseDAO<T> {

    String EXECUTED_QUERY_LOG_TEMPLATE = "Executed query to DB to ";
    String NOT_EXECUTED_QUERY_LOG_TEMPLATE = "Couldn't execute query to DB to ";

    Optional<T> getEntityById(long id);

    int updateEntity(T entity);

    void createEntity(T entity);

    int removeEntity(long id);

}
