package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.dialog.CategoryDialog;
import cz.muni.fi.pv168.project.ui.model.CheckableListModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditCategoryAction extends AbstractAction {
    private final Container parent;
    private final CheckableListModel<Category> categoryList;
    private static final I18N I18N = new I18N(EditCategoryAction.class);

    public EditCategoryAction(Container parent, CheckableListModel<Category> categoryList) {
        super(I18N.getString("name"));
        this.parent = parent;
        this.categoryList = categoryList;
        putValue(SHORT_DESCRIPTION, I18N.getString("shortDescription"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var selectedCategories = categoryList.getSelectedItems();
        if (selectedCategories.size() != 1) {
            throw new IllegalStateException(I18N.getString("error.invalidSelection") + selectedCategories.size());
        }
        var selected = selectedCategories.get(0);
        var dialog = new CategoryDialog(parent, selected, categoryList.getUnderlyingModel());
        dialog.show(I18N.getString("name"))
                .ifPresent(category -> categoryList.getUnderlyingModel().updateRow(category));
    }
}
