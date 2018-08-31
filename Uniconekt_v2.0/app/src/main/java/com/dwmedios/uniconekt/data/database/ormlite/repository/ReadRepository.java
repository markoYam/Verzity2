package com.dwmedios.uniconekt.data.database.ormlite.repository;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.List;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public interface ReadRepository <T> {

    List<T> selectByPage(int pageNumber, int pageSize, String fieldOrder, boolean sortAscending, Where<T, Integer> where);

    List<T> selectByPage(int pageNumber, int pageSize, String fieldOrder, boolean sortAscending);

    List<T> selectAll();

    List<T> selectByQueryBuilder(QueryBuilder<T, Integer> queryBuilder);

    T selectById(int elementId);

    int refresh(T element);

    QueryBuilder<T, Integer> getQueryBuilder();

    T exists(int id);

}