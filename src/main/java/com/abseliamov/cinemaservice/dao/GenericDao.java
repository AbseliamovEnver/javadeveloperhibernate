package com.abseliamov.cinemaservice.dao;

import java.util.List;

public interface GenericDao<T> {

    void add(T item);

    List<T> getAll();

    T getById(long id);

    boolean update(long id, T item);

    boolean delete(long id);
}
