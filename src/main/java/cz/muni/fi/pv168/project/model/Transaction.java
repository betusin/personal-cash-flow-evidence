package cz.muni.fi.pv168.project.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "Transaction")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Transaction extends BaseEntity {
    protected long amount;
    @ManyToOne
    @JoinColumn(name = "category_id")
    protected Category category;
    protected String description;

    public Transaction() {
    }

    public Transaction(long amount, Category category, String description) {
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public abstract TransactionType getTransactionType();

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BaseTransaction{" +
                "amount=" + amount +
                ", category=" + category +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
