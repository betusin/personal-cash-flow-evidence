package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ListUtils;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDialog extends EntityDialog<Category> {
    private static final I18N I18N = new I18N(CategoryDialog.class);
    private final MutableListModel<Category> categoryModel;
    private final JTextField categoryNameField = new JTextField(20);
    private final Category category;

    public CategoryDialog(Component parent, @Nullable Category category, MutableListModel<Category> categoryModel) {
        super(parent);
        this.categoryModel = categoryModel;
        this.category = category != null ? category : new Category();
        add(I18N.getString("field.name"), categoryNameField);
        categoryNameField.setText(this.category.getName());
    }

    @Override
    Category getEntity() {
        category.setName(categoryNameField.getText());
        return category;
    }

    @Override
    protected List<String> validate(Category entity) {
        var errors = new ArrayList<String>();
        if (entity.getName().isBlank()) {
            errors.add(I18N.getString("error.name.empty"));
        }
        if (nameAlreadyExists(entity)) {
            errors.add(I18N.getFormattedMessage("error.exists", entity.getName()));
        }
        return ListUtils.concat(super.validate(entity), errors);
    }

    private boolean nameAlreadyExists(Category editing) {
        return categoryModel.getRows().stream()
                .filter(item -> item != editing)
                .anyMatch(item -> item.getName().equalsIgnoreCase(editing.getName()));
    }
}
