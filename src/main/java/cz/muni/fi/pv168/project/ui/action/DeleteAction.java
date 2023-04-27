package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.TransactionItem;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.model.TransactionTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class DeleteAction extends AbstractAction {
    private static final I18N I18N = new I18N(DeleteAction.class);
    private final Component parent;
    private final JTable transactionTable;

    public DeleteAction(Component parent, JTable transactionTable) {
        super(I18N.getString("name")/*, Icons.DELETE_ICON*/);
        this.parent = parent;
        this.transactionTable = transactionTable;
        putValue(SHORT_DESCRIPTION, I18N.getString("shortDescription"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var transactionModel = (TransactionTableModel) transactionTable.getModel();
        int response = JOptionPane.showConfirmDialog(parent,
                I18N.getString("message"), I18N.getString("dialog"), JOptionPane.OK_CANCEL_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Arrays.stream(transactionTable.getSelectedRows())
                    .map(transactionTable::convertRowIndexToModel)
                    .mapToObj(transactionModel::getEntity)
                    .map(TransactionItem::getTransaction)
                    .distinct()
                    // Stream must be collected before deletion to not be affected by index changes
                    .collect(Collectors.toList())
                    .forEach(transactionModel::deleteRow);
        }
    }
}
