package pl.jcommerce.apicat.dao;


/**
 * Created by prho on 17.01.17.
 */

public interface GenericDao<T> {

    T create(T t);
    T find(Object id);
    T update(T t);
    void delete(Object id);
}
