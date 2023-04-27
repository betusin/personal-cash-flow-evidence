package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.*;

import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TransactionCalculator {

    private TransactionCalculator() {

    }

    /**
     * Calculates the total sum of all transactions until the provided date.
     * @param transactions Transactions to sum
     * @param today Date to stop at
     * @return Sum of transactions until the provided date
     */
    public static long calculateOverallTotal(Collection<Transaction> transactions, LocalDate today) {
        return transactions.stream()
                .flatMap(entity -> expandTransactionUntilToday(entity, today))
                .map(item -> item.getTransaction().getAmount())
                .reduce(0L, Long::sum);
    }

    /**
     * Expands the transaction into its occurrences in the provided start-end date range.
     * @param entity Transaction
     * @param start First date to include
     * @param end Last day to include
     * @return All occurrences of the provided transaction
     */
    public static List<TransactionItem> expandTransactionInRange(Transaction entity, LocalDate start, LocalDate end) {
        if (entity instanceof OneTimeTransaction) {
            return Collections.singletonList(new TransactionItem((OneTimeTransaction) entity));
        } else {
            return expandPeriodicTransaction((PeriodicTransaction) entity, start, end);
        }
    }

    private static Stream<TransactionItem> expandTransactionUntilToday(Transaction entity, LocalDate today) {
        if (entity instanceof OneTimeTransaction) {
            var ot = (OneTimeTransaction) entity;
            if (!ot.getDate().isAfter(today)) {
                return Stream.ofNullable(new TransactionItem(ot));
            } else {
                return Stream.empty();
            }
        } else {
            var pt = (PeriodicTransaction) entity;
            return expandPeriodicTransaction(pt, pt.getStartDate(), today).stream();
        }
    }

    private static List<TransactionItem> expandPeriodicTransaction(PeriodicTransaction pt, LocalDate start, LocalDate end) {
        var curr = pt.getStartDate();
        var result = new ArrayList<TransactionItem>();
        var endDate = pt.getEndDate();
        while (!curr.isAfter(end) && (endDate == null || !curr.isAfter(endDate))) {
            if (!curr.isBefore(start)) {
                result.add(new TransactionItem(pt, curr));
            }
            curr = curr.plus(pt.getPeriodicity(), periodTypeToTemporalUnit(pt.getPeriodType()));
        }
        return result;
    }

    private static TemporalUnit periodTypeToTemporalUnit(PeriodType type) {
        return type.getChronoUnit();
    }
}
