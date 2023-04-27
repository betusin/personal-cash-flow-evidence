package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.model.CheckableListModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DeleteCategoryAction extends AbstractAction {
    private static final I18N I18N = new I18N(DeleteCategoryAction.class);
    private final Component parent;
    private final CheckableListModel<Category> categoryList;

    public DeleteCategoryAction(Component parent, CheckableListModel<Category> categoryList) {
        super(I18N.getString("name"));
        this.parent = parent;
        this.categoryList = categoryList;
        putValue(SHORT_DESCRIPTION, I18N.getString("shortDescription"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int response = JOptionPane.showConfirmDialog(
                parent,
                I18N.getString("message"),
                I18N.getString("name"),
                JOptionPane.OK_CANCEL_OPTION
        );
        if (response == JOptionPane.YES_OPTION) {
            categoryList.getSelectedItems()
                    .forEach(category -> categoryList.getUnderlyingModel().deleteRow(category));
        }
    }
}
