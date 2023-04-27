package cz.muni.fi.pv168.project.ui.filter;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.TransactionItem;
import cz.muni.fi.pv168.project.ui.model.CheckableListModel;
import cz.muni.fi.pv168.project.ui.model.LocalDateModel;
import cz.muni.fi.pv168.project.ui.model.TransactionTableModel;

import javax.swing.*;
import java.util.Set;
import java.util.function.Predicate;

public class TransactionRowFilter extends RowFilter<TransactionTableModel, Integer> {
    private final TransactionTableModel transactionTableModel;
    private final Set<Predicate<TransactionItem>> predicates;

    public TransactionRowFilter(
            TransactionTableModel transactionModel,
            CheckableListModel<Category> categoryModel,
            ComboBoxModel<TransactionTypeOption> typeModel,
            LocalDateModel fromDateModel,
            LocalDateModel toDateModel
    ) {
        this.transactionTableModel = transactionModel;
        this.predicates = Set.of(
                new CategoryPredicate(categoryModel),
                new DatePredicate(fromDateModel, toDateModel),
                new TypePredicate(typeModel)
        );
    }

    @Override
    public boolean include(Entry<? extends TransactionTableModel, ? extends Integer> entry) {
        var row = transactionTableModel.getEntity(entry.getIdentifier());
        return predicates.stream().allMatch(predicate -> predicate.test(row));
    }
}
