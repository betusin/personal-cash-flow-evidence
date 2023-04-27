package cz.muni.fi.pv168.project.ui.panel;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.JCheckBoxList;
import cz.muni.fi.pv168.project.ui.action.AddCategoryAction;
import cz.muni.fi.pv168.project.ui.action.DeleteCategoryAction;
import cz.muni.fi.pv168.project.ui.action.EditCategoryAction;
import cz.muni.fi.pv168.project.ui.action.CheckAllCategoriesAction;
import cz.muni.fi.pv168.project.ui.button.CategoryButtonFactory;
import cz.muni.fi.pv168.project.ui.container.ContentContainer;
import cz.muni.fi.pv168.project.ui.model.CheckableListModel;
import cz.muni.fi.pv168.project.ui.model.NoSelectionModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alžbeta Hajná
 */
public class CategoryPanel extends JPanel {
    private static final I18N I18N = new I18N(CategoryPanel.class);
    private final CheckableListModel<Category> categoryModel;
    private final AddCategoryAction addAction;
    private final EditCategoryAction editAction;
    private final DeleteCategoryAction deleteAction;
    private final CheckAllCategoriesAction checkAllAction;

    public CategoryPanel(EditCategoryAction editAction, DeleteCategoryAction deleteAction,
                         AddCategoryAction addAction, CheckableListModel<Category> categoryModel) {
        super(new GridBagLayout());
        this.categoryModel = categoryModel;
        this.editAction = editAction;
        this.deleteAction = deleteAction;
        this.addAction = addAction;
        this.checkAllAction = new CheckAllCategoriesAction(categoryModel);
        addCategoriesPanel();
    }

    private void addCategoriesPanel() {
        setBackground(Color.LIGHT_GRAY);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        // Check/uncheck all
        c.gridy = 0;
        c.weighty = 0;
        var checkAllButton = new JButton(checkAllAction);
        checkAllButton.setBackground(Color.LIGHT_GRAY);
        checkAllButton.setForeground(Color.BLACK);
        add(checkAllButton, c);

        // List of categories
        var categoriesCheckBoxList = new JCheckBoxList<>(categoryModel);
        categoriesCheckBoxList.setSelectionModel(new NoSelectionModel());
        categoriesCheckBoxList.setBackground(Color.LIGHT_GRAY);
        categoriesCheckBoxList.setForeground(Color.BLACK);
        categoriesCheckBoxList.setComponentPopupMenu(createCategoryPopupMenu());
        c.gridy = 1;
        c.weighty = 1;
        var scrollPane = new JScrollPane(categoriesCheckBoxList);
        var emptyLabel = new JLabel(I18N.getString("no.categories"));
        emptyLabel.setForeground(Color.BLACK);
        var container = new ContentContainer<>(categoryModel, scrollPane, emptyLabel);
        add(container, c);

        var addCategoryButton = CategoryButtonFactory.createCategoryButton(addAction);
        c.gridy = 2;
        c.weighty = 0;
        add(addCategoryButton, c);

        var editCategoryButton = CategoryButtonFactory.createCategoryButton(editAction);
        c.gridy = 3;
        c.weighty = 0;
        add(editCategoryButton, c);

        var deleteCategoriesButton = CategoryButtonFactory.createCategoryButton(deleteAction);
        c.gridy = 4;
        c.weighty = 0;
        add(deleteCategoriesButton, c);
    }

    private JPopupMenu createCategoryPopupMenu() {
        var menu = new JPopupMenu();
        menu.add(addAction);
        menu.add(deleteAction);
        menu.add(editAction);
        return menu;
    }
}
