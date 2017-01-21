package pl.jcommerce.apicat.dao;


public interface GenericDao<T> {

    T create(T t);

    T find(Object id);

    T update(T t);

    void delete(Object id);
}
