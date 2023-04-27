package cz.muni.fi.pv168.project.ui.filter;

import cz.muni.fi.pv168.project.model.TransactionItem;
import cz.muni.fi.pv168.project.ui.model.LocalDateModel;

import java.time.LocalDate;
import java.util.function.Predicate;

public class DatePredicate implements Predicate<TransactionItem> {
    private final LocalDateModel fromDateModel;
    private final LocalDateModel toDateModel;

    public DatePredicate(LocalDateModel fromDateModel, LocalDateModel toDateModel) {
        this.fromDateModel = fromDateModel;
        this.toDateModel = toDateModel;
    }

    @Override
    public boolean test(TransactionItem item) {
        var fromDate = modelToDate(fromDateModel);
        var toDate = modelToDate(toDateModel);
        var rowDate = item.getDate();
        return !rowDate.isBefore(fromDate) && !rowDate.isAfter(toDate);
    }

    private LocalDate modelToDate(LocalDateModel fromDateModel) {
        return LocalDate.of(fromDateModel.getYear(), fromDateModel.getMonth() + 1, fromDateModel.getDay());
    }
}
