package cn.jdz.glib.data.dbaccess;

import java.util.List;

/**
 * Created by admin on 2017/11/23.
 */
public interface IDBManager<T> {

    boolean create();

    boolean insert(T t);

    boolean update(T t);

    boolean delete(T t);

    List<T> getAll();

    T getByUnique(Object uniqueValue);

}
