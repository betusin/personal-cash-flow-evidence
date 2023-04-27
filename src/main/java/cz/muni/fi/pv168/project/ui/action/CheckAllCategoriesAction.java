package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.ListDataChangeAdapter;
import cz.muni.fi.pv168.project.ui.model.CheckableListModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collections;

public class CheckAllCategoriesAction extends AbstractAction {
    private final CheckableListModel<Category> checkableCategoryModel;
    private static final I18N I18N = new I18N(CheckAllCategoriesAction.class);

    public CheckAllCategoriesAction(CheckableListModel<Category> checkableCategoryModel) {
        this.checkableCategoryModel = checkableCategoryModel;
        checkableCategoryModel.addListDataListener(new ListDataChangeAdapter((e) -> updateCheckedLabel()));
        updateCheckedLabel();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        var currentState = getState();
        if (currentState == State.ALL) {
            checkableCategoryModel.selectNone();
        } else {
            checkableCategoryModel.selectAll();
        }
    }

    public State getState() {
        var selSize = checkableCategoryModel.getSelectedItems().size();
        if (selSize == checkableCategoryModel.getSize()) {
            return State.ALL;
        } else if (selSize == 0) {
            return State.NONE;
        } else {
            return State.SOME;
        }
    }


    private void updateCheckedLabel() {
        if (getState() == State.ALL) {
            putValue(Action.NAME, I18N.getString("uncheck"));
        } else {
            putValue(Action.NAME, I18N.getString("check"));
        }
    }


    private enum State {
        ALL,
        SOME,
        NONE
    }
}
