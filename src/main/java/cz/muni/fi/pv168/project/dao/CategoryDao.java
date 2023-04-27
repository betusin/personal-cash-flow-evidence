package cz.muni.fi.pv168.project.dao;

import cz.muni.fi.pv168.project.data.DataAccessException;
import cz.muni.fi.pv168.project.data.DataAccessObject;
import cz.muni.fi.pv168.project.model.Category;
import org.hibernate.Session;

import java.util.Collection;

import static cz.muni.fi.pv168.project.dao.TransactionUtils.withTransaction;
import static cz.muni.fi.pv168.project.dao.TransactionUtils.withTransactionGet;

public class CategoryDao implements DataAccessObject<Category> {
    private final Session session;

    public CategoryDao(Session session) {
        this.session = session;
    }

    @Override
    public void create(Category entity) throws DataAccessException {
        withTransaction(session, (session) -> session.saveOrUpdate(entity));
    }

    @Override
    public Collection<Category> findAll() throws DataAccessException {
        return withTransactionGet(session, session -> session.createQuery("from Category", Category.class).getResultList());
    }

    @Override
    public void update(Category entity) throws DataAccessException {
        withTransaction(session, (session) -> session.saveOrUpdate(entity));
    }

    @Override
    public void delete(Category entity) throws DataAccessException {
        withTransaction(session, (session) -> session.delete(entity));
    }
}
