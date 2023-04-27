package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;
import cz.muni.fi.pv168.project.ui.dialog.CategoryDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddCategoryAction extends AbstractAction {
    private static final I18N I18N = new I18N(AddCategoryAction.class);
    private final Component parent;
    private final MutableListModel<Category> categoryList;

    public AddCategoryAction(Component parent, MutableListModel<Category> categoryList) {
        super(I18N.getString("name"));
        this.parent = parent;
        this.categoryList = categoryList;
        putValue(SHORT_DESCRIPTION, I18N.getString("shortDescription"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = new CategoryDialog(parent, null, categoryList);
        dialog.show(I18N.getString("dialog"))
                .ifPresent(categoryList::addRow);
    }
}
