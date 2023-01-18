package org.example.dao.interfaces;

import java.util.Optional;

public interface IBaseDAO<T> {

    String EXECUTED_QUERY = "Executed query to DB to ";
    String NOT_EXECUTE_QUERY = "Couldn't execute query to DB to ";

    Optional<T> getEntityById(long id);

    int updateEntity(T entity);

    Optional<T> createEntity(T entity);

    int removeEntity(long id);

}
