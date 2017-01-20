package pl.jcommerce.apicat.dao.impl;

import pl.jcommerce.apicat.dao.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by prho on 17.01.17.
 */

public abstract class GenericDaoImpl<T> implements GenericDao<T> {

    @PersistenceContext
    protected EntityManager em;

    private Class<T> type;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public T create(final T t) {
        this.em.persist(t);
        return t;
    }

    @Override
    public void delete(final Object id) {
        this.em.remove(this.em.getReference(type, id));
    }

    @Override
    public T find(final Object id) {
        return (T) this.em.find(type, id);
    }

    @Override
    public T update(final T t) {
        return this.em.merge(t);
    }
}
