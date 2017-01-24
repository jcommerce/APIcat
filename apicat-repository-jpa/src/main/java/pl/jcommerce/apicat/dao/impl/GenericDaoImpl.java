package pl.jcommerce.apicat.dao.impl;

import pl.jcommerce.apicat.dao.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract class GenericDaoImpl<T> implements GenericDao<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> type;

    @SuppressWarnings("unchecked")
    GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public T create(final T t) {
        this.entityManager.persist(t);
        return t;
    }

    @Override
    public void delete(final Object id) {
        this.entityManager.remove(this.entityManager.getReference(type, id));
    }

    @Override
    public T find(final Object id) {
        return this.entityManager.find(type, id);
    }

    @Override
    public T update(final T t) {
        return this.entityManager.merge(t);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
