package cz.muni.fi.pv168.project.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class OneTimeTransaction extends Transaction {
    protected LocalDate date;

    public OneTimeTransaction() {
    }

    public OneTimeTransaction(LocalDate date, long amount, Category category, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public TransactionType getTransactionType() {
        return TransactionType.ONE_TIME;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "OneTimeTransaction{" +
                "date=" + date +
                "} " + super.toString();
    }
}
