package cz.muni.fi.pv168.project.model;

import java.time.LocalDate;
import java.util.Objects;

public class TransactionItem {
    private final Transaction transaction;
    private final LocalDate date;

    public TransactionItem(OneTimeTransaction transaction) {
        this.transaction = transaction;
        this.date = null;
    }

    public TransactionItem(PeriodicTransaction periodicTransaction, LocalDate date) {
        this.transaction = periodicTransaction;
        this.date = date;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public TransactionType getType() {
        return transaction instanceof OneTimeTransaction ? TransactionType.ONE_TIME : TransactionType.PERIODIC;
    }

    public LocalDate getDate() {
        return getType() == TransactionType.ONE_TIME ? ((OneTimeTransaction) transaction).getDate() : date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            return;
        }
        if (getType() == TransactionType.PERIODIC)
            throw new IllegalStateException("Date can only be set for one-time transactions.");
        ((OneTimeTransaction) transaction).setDate(date);
    }

    @Override
    public String toString() {
        return "TransactionItem{" +
                "transaction=" + transaction +
                ", date=" + date +
                '}';
    }
}
