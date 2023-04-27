package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.OneTimeTransaction;
import cz.muni.fi.pv168.project.model.PeriodicTransaction;
import cz.muni.fi.pv168.project.model.TransactionType;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.dialog.OneTimeTransactionDialog;
import cz.muni.fi.pv168.project.ui.dialog.PeriodicTransactionDialog;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;
import cz.muni.fi.pv168.project.ui.model.TransactionTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class EditTransactionAction extends AbstractAction {
    private static final I18N I18N = new I18N(EditTransactionAction.class);
    private final Component parent;
    private final JTable transactionTable;
    private final MutableListModel<Category> categoryModel;

    public EditTransactionAction(Component parent, JTable transactionTable, MutableListModel<Category> categoryModel) {
        super(I18N.getString("edit")/*, Icons.EDIT_ICON*/);
        this.parent = parent;
        this.transactionTable = transactionTable;
        this.categoryModel = categoryModel;
        putValue(SHORT_DESCRIPTION, I18N.getString("shortDescription"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = transactionTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException(I18N.getString("error.invalidSelection") + selectedRows.length);
        }
        if (transactionTable.isEditing()) {
            transactionTable.getCellEditor().cancelCellEditing();
        }
        var transactionTableModel = (TransactionTableModel) transactionTable.getModel();
        int modelRow = transactionTable.convertRowIndexToModel(selectedRows[0]);
        var item = transactionTableModel.getEntity(modelRow);
        var transaction = item.getTransaction();
        if (transaction.getTransactionType() == TransactionType.ONE_TIME) {
            var dialog = new OneTimeTransactionDialog(parent, (OneTimeTransaction) transaction, categoryModel);
            dialog.show(I18N.getString("oneTimeDialog"))
                    .ifPresent(t -> transactionTableModel.updateRow(item));
        } else {
            var periodicTransaction = (PeriodicTransaction) transaction;
            var dialog = new PeriodicTransactionDialog(parent, periodicTransaction, categoryModel);
            dialog.show(I18N.getString("periodicDialog"))
                    .ifPresent(t -> transactionTableModel.updateRow(item));
        }
    }
}
