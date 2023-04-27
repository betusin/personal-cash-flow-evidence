package cz.muni.fi.pv168.project.model;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
public class PeriodicTransaction extends Transaction {
    private LocalDate startDate;
    @Nullable
    private LocalDate endDate;
    private PeriodType periodType;
    private long periodicity;

    public PeriodicTransaction() {
    }

    public PeriodicTransaction(long amount, Category category, String description, LocalDate startDate, @Nullable LocalDate endDate, PeriodType periodType, long periodicity) {
        super(amount, category, description);
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodType = periodType;
        this.periodicity = periodicity;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.PERIODIC;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Nullable
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@Nullable LocalDate endDate) {
        this.endDate = endDate;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public long getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(long periodicity) {
        this.periodicity = periodicity;
    }

    @Override
    public String toString() {
        return "PeriodicTransaction{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", periodType=" + periodType +
                ", periodicity=" + periodicity +
                "} " + super.toString();
    }
}
