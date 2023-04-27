package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ListUtils;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Transaction;
import cz.muni.fi.pv168.project.model.TransactionDirection;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.button.CategoryButtonFactory;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;
import cz.muni.fi.pv168.project.ui.renderer.LabeledListCellRenderer;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransactionDialog<T extends Transaction> extends EntityDialog<T> {
    private static final I18N I18N = new I18N(BaseTransactionDialog.class);
    private final MutableListModel<Category> baseCategoryModel;
    protected final ComboBoxModel<Category> dialogCategoryModel;
    protected final JComboBox<Category> comboCategories;
    protected final ComboBoxModel<TransactionDirection> dialogDirectionModel;
    protected final T transaction;
    protected final JTextField descriptionField = new JTextField(20);
    protected final JFormattedTextField amountField = new JFormattedTextField(getIntegerFormatter());

    public BaseTransactionDialog(Component parent, T transaction, MutableListModel<Category> categoryModel) {
        super(parent);
        this.baseCategoryModel = categoryModel;
        this.dialogCategoryModel = new ComboBoxModelAdapter<>(categoryModel);
        this.comboCategories = new JComboBox<>(dialogCategoryModel);
        this.dialogDirectionModel = new DefaultComboBoxModel<>(TransactionDirection.values());
        this.transaction = transaction != null ? transaction : newEmptyInstancePrefilled();
        init();
        prefillFields();
        addFields();
    }

    protected abstract void init();

    protected abstract T newEmptyInstance();

    private T newEmptyInstancePrefilled() {
        var transaction = newEmptyInstance();
        if (dialogCategoryModel.getSize() > 0) {
            transaction.setCategory(dialogCategoryModel.getElementAt(0));
        }
        return transaction;
    }

    protected void prefillFields() {
        var amount = transaction.getAmount();
        dialogCategoryModel.setSelectedItem(transaction.getCategory());
        descriptionField.setText(transaction.getDescription());
        amountField.setValue(Math.abs(amount));
        dialogDirectionModel.setSelectedItem(amount > 0 ? TransactionDirection.IN : TransactionDirection.OUT);
    }

    protected void addFields() {
        var button = CategoryButtonFactory.createCategoryButton(I18N.getString("category.add"));
        button.addActionListener(e -> openAddCategoryDialog());
        comboCategories.setEnabled(dialogCategoryModel.getSize() > 0);
        add(I18N.getString("field.category"), comboCategories);
        add("", button);
        add(I18N.getString("field.description"), descriptionField);
        add(I18N.getString("field.amount"), amountField);
        var comboBox = new JComboBox<>(dialogDirectionModel);
        comboBox.setRenderer(new DirectionRenderer());
        add(I18N.getString("field.direction"), comboBox);
    }

    private void openAddCategoryDialog() {
        var dialog = new CategoryDialog(getParent(), null, baseCategoryModel);
        dialog.show(I18N.getString("category.add"))
                .ifPresent(this::onCategoryCreated);
    }

    private void onCategoryCreated(Category category) {
        baseCategoryModel.addRow(category);
        dialogCategoryModel.setSelectedItem(category);
        comboCategories.setEnabled(true);
    }

    protected void fillEntityFromUi() {
        var direction = (TransactionDirection) dialogDirectionModel.getSelectedItem();
        var amount = getAmountFromRawString(direction, amountField.getText());
        transaction.setCategory((Category) dialogCategoryModel.getSelectedItem());
        transaction.setDescription(descriptionField.getText());
        transaction.setAmount(amount);
    }

    @Override
    protected List<String> validate(T entity) {
        var errors = new ArrayList<String>();
        if (entity.getCategory() == null) {
            errors.add(I18N.getString("error.category.empty"));
        }
        if (entity.getAmount() == 0) {
            errors.add(I18N.getString("error.amount.empty"));
        }
        return ListUtils.concat(super.validate(entity), errors);
    }

    @Override
    T getEntity() {
        fillEntityFromUi();
        return transaction;
    }

    static long getAmountFromRawString(TransactionDirection direction, String rawText) {
        long amount = getIntegerFromTextFieldValue(rawText);
        if (direction == TransactionDirection.OUT) {
            amount = -amount;
        }
        return amount;
    }

    static NumberFormatter getIntegerFormatter() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);
        return formatter;
    }

    static long getIntegerFromTextFieldValue(String rawText) {
        var amountStr = rawText.replaceAll("[^0-9]", "");
        if (amountStr.isEmpty()) amountStr = "0";
        return Long.parseLong(amountStr);
    }

    private static class DirectionRenderer extends LabeledListCellRenderer<TransactionDirection> {
        @Override
        protected String getLabelForItem(TransactionDirection item) {
            return I18N.getString(item.getDirectionName());
        }
    }
}
