package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.data.DataAccessObject;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Transaction;
import cz.muni.fi.pv168.project.model.TransactionItem;
import cz.muni.fi.pv168.project.model.TransactionType;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.TransactionCalculator;

import java.time.LocalDate;
import java.util.List;

public class TransactionTableModel extends FlatteningTableModel<TransactionItem, Transaction> {
    public static final int DATE_COLUMN_INDEX = 2;
    private final LocalDateModel fromDateModel;
    private final LocalDateModel toDateModel;
    private static final I18N I18N = new I18N(TransactionTableModel.class);

    public TransactionTableModel(DataAccessObject<Transaction> dao, LocalDateModel fromDateModel, LocalDateModel toDateModel) {
        super(COLUMNS, dao);
        this.fromDateModel = fromDateModel;
        this.toDateModel = toDateModel;
    }

    private static final List<Column<TransactionItem, ?>> COLUMNS = List.of(
            Column.readOnly(I18N.getString("type"), TransactionType.class, TransactionItem::getType),
            Column.editable(I18N.getString("description"), String.class, (t) -> t.getTransaction().getDescription(), (t, amount) -> t.getTransaction().setDescription(amount)),
            Column.possiblyEditable(I18N.getString("date"), LocalDate.class, TransactionTableModel::isTransactionDateEditable, TransactionItem::getDate, TransactionItem::setDate),
            Column.editable(I18N.getString("category"), Category.class, (t) -> t.getTransaction().getCategory(), (t, category) -> t.getTransaction().setCategory(category)),
            Column.editable(I18N.getString("amount") + " (CZK)", Long.class, (t) -> t.getTransaction().getAmount(), (t, amount) -> t.getTransaction().setAmount(amount))
    );

    @Override
    protected List<TransactionItem> getRowsFromEntity(Transaction entity) {
        var start = fromDateModel.getValue();
        var end = toDateModel.getValue();
        return TransactionCalculator.expandTransactionInRange(entity, start, end);
    }

    @Override
    protected Transaction getEntityFromRow(TransactionItem row) {
        return row.getTransaction();
    }

    private static boolean isTransactionDateEditable(TransactionItem item) {
        return item.getType() == TransactionType.ONE_TIME;
    }
}
