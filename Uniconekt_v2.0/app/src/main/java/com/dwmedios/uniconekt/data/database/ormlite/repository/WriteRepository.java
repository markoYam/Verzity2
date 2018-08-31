package com.dwmedios.uniconekt.data.database.ormlite.repository;

import com.j256.ormlite.stmt.QueryBuilder;

import java.util.List;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public interface WriteRepository<T> {

    int addElement(T element);

    int addElements(List<T> elements);

    int updateElement(T element);

    int updateElements(List<T> elements);

    int deleteElement(T element);

    int deleteElements(List<T> elements);

    QueryBuilder<T, Integer> getQueryBuilder();

}