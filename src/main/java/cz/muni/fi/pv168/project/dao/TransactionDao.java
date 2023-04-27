package cz.muni.fi.pv168.project.dao;

import cz.muni.fi.pv168.project.data.DataAccessException;
import cz.muni.fi.pv168.project.data.DataAccessObject;
import cz.muni.fi.pv168.project.model.Transaction;
import org.hibernate.Session;

import java.util.Collection;

import static cz.muni.fi.pv168.project.dao.TransactionUtils.withTransaction;
import static cz.muni.fi.pv168.project.dao.TransactionUtils.withTransactionGet;

public class TransactionDao implements DataAccessObject<Transaction> {
    private final Session session;

    public TransactionDao(Session session) {
        this.session = session;
    }

    @Override
    public void create(Transaction entity) throws DataAccessException {
        withTransaction(session, (session) -> session.saveOrUpdate(entity));
    }

    @Override
    public Collection<Transaction> findAll() throws DataAccessException {
        return withTransactionGet(session, session -> session
                .createQuery("from Transaction", Transaction.class)
                .getResultList()
        );
    }

    @Override
    public void update(Transaction entity) throws DataAccessException {
        withTransaction(session, (session) -> session.saveOrUpdate(entity));
    }

    @Override
    public void delete(Transaction entity) throws DataAccessException {
        withTransaction(session, (session) -> session.delete(entity));
    }
}
