package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;

import javax.swing.*;

public abstract class AddAction extends AbstractAction {
    protected final JTable transactionTable;
    protected final MutableListModel<Category> categoryModel;

    public AddAction(String name, JTable transactionTable, MutableListModel<Category> categoryModel) {
        super(name);
        this.transactionTable = transactionTable;
        this.categoryModel = categoryModel;
    }
}
