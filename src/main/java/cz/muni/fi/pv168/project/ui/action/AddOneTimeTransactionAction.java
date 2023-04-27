package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.dialog.OneTimeTransactionDialog;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;
import cz.muni.fi.pv168.project.ui.model.TransactionTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author Alžbeta Hajná
 */
public class AddOneTimeTransactionAction extends AddAction {
    private static final I18N I18N = new I18N(AddOneTimeTransactionAction.class);
    private final Component parent;

    public AddOneTimeTransactionAction(Component parent, JTable transactionsTable, MutableListModel<Category> categoryModel) {
        super(I18N.getString("name"), transactionsTable, categoryModel);
        this.parent = parent;
        putValue(SHORT_DESCRIPTION, I18N.getString("shortDescription"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_N);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        var transactionTableModel = (TransactionTableModel) transactionTable.getModel();
        var dialog = new OneTimeTransactionDialog(parent, null, categoryModel);
        dialog.show(I18N.getString("dialog"))
                .ifPresent(transactionTableModel::addRow);
    }
}
