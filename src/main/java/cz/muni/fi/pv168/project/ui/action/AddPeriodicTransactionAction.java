package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.dialog.PeriodicTransactionDialog;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;
import cz.muni.fi.pv168.project.ui.model.TransactionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author Alžbeta Hajná
 */
public class AddPeriodicTransactionAction extends AddAction {
    private static final I18N I18N = new I18N(AddPeriodicTransactionAction.class);

    public AddPeriodicTransactionAction(JTable transactionTable, MutableListModel<Category> categoryModel) {
        super(I18N.getString("name"), transactionTable, categoryModel);
        putValue(SHORT_DESCRIPTION, I18N.getString("shortDescription"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_M);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl M"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var transactionTableModel = (TransactionTableModel) transactionTable.getModel();
        var dialog = new PeriodicTransactionDialog(transactionTable, null, categoryModel);
        dialog.show(I18N.getString("dialog"))
                .ifPresent(transactionTableModel::addRow);
    }
}
