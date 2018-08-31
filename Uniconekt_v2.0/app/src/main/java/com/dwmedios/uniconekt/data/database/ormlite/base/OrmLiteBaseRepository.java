package com.dwmedios.uniconekt.data.database.ormlite.base;

import android.text.TextUtils;

import com.dwmedios.uniconekt.data.database.ormlite.repository.Repository;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public class OrmLiteBaseRepository <T> implements Repository<T> {

    public OrmLiteBaseRepository(OrmLiteDatabaseHelper helper, Class<T> clazz) {
        mHelper = helper;
        mClazz = clazz;
        createDao();
    }

    @Override
    public int addElement(T element) {
        try {
            return mDao.create(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int addElements(List<T> elementList) {
        return executeBatchRequest(elementList, BATCH_CREATE);
    }

    @Override
    public int updateElement(T element) {
        try {
            return mDao.update(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateElements(List<T> elementList) {
        return executeBatchRequest(elementList, BATCH_UPDATE);
    }

    @Override
    public int deleteElement(T element) {
        try {
            return mDao.delete(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteElements(List<T> elementList) {
        return executeBatchRequest(elementList, BATCH_DELETE);
    }

    @Override
    public List<T> selectByPage(int pageNumber, int pageSize, String fieldOrder, boolean sortAscending, Where<T, Integer> where) {
        List<T> list = new ArrayList<>();
        try {
            QueryBuilder<T, Integer> builder = getQueryBuilder();
            if (pageNumber > 0 && pageSize > 0) {
                if (pageNumber > 0) {
                    pageNumber = pageNumber - 1;
                }
                builder.offset((long) pageNumber * pageSize).limit(
                        (long) pageSize);
            }
            if (!TextUtils.isEmpty(fieldOrder)) {
                builder.orderBy(fieldOrder, sortAscending);
            }
            if (where != null) {
                builder.setWhere(where);
            }
            list = builder.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<T> selectByPage(int pageNumber, int pageSize, String fieldOrder, boolean sortAscending) {
        List<T> list = new ArrayList<>();
        try {
            QueryBuilder<T, Integer> builder = getQueryBuilder();
            if (pageNumber > 0 && pageSize > 0) {
                if (pageNumber > 0) {
                    pageNumber = pageNumber - 1;
                }
                builder.offset((long) pageNumber * pageSize).limit(
                        (long) pageSize);
            }
            if (!TextUtils.isEmpty(fieldOrder)) {
                builder.orderBy(fieldOrder, sortAscending);
            }
            list = builder.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public T selectById(int id) {
        try {
            return mDao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int refresh(T element) {
        try {
            return getDao().refresh(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<T> selectAll() {
        List<T> itemList = null;
        try {
            itemList = mDao.queryForAll();
            if (itemList == null) {
                itemList = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }

    @Override
    public List<T> selectByQueryBuilder(QueryBuilder<T, Integer> queryBuilder) {
        try {
            return queryBuilder.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public QueryBuilder<T, Integer> getQueryBuilder() {
        return mDao.queryBuilder();
    }

    @Override
    public T exists(int id) {
        try {
            return mDao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Dao<T, Integer> getDao() {
        return mDao;
    }

    protected OrmLiteSqliteOpenHelper getHelper() {
        return mHelper;
    }

    private int executeBatchRequest(final List<T> elementsToProcess, final int batchType) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int affectedRows = 0;
                for (T currentElement : elementsToProcess) {
                    switch (batchType) {
                        case BATCH_CREATE:
                            affectedRows += addElement(currentElement);
                            break;
                        case BATCH_DELETE:
                            affectedRows += deleteElement(currentElement);
                            break;
                        case BATCH_UPDATE:
                            affectedRows += updateElement(currentElement);
                            break;
                    }
                }
                return affectedRows;
            }
        };
        try {
            return mDao.callBatchTasks(callable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void createDao() {
        try {
            mDao = mHelper.getDao(mClazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int BATCH_UPDATE = 1;
    private static final int BATCH_DELETE = 2;
    private static final int BATCH_CREATE = 3;

    private Dao<T, Integer> mDao;
    private OrmLiteSqliteOpenHelper mHelper;
    private Class<T> mClazz;
}
