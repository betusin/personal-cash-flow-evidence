package cz.muni.fi.pv168.project.dao;

import org.hibernate.Session;

import java.util.function.Consumer;
import java.util.function.Function;

public final class TransactionUtils {

    private TransactionUtils() {

    }


    public static <T> T withTransactionGet(Session s, Function<Session, T> action) {
        org.hibernate.Transaction transaction = null;
        T result;
        try {
            transaction = s.beginTransaction();
            result = action.apply(s);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
        return result;
    }

    public static void withTransaction(Session s, Consumer<Session> action) {
        withTransactionGet(s, session -> {
            action.accept(session);
            return null;
        });
    }
}
