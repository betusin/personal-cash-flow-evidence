package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ListUtils;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.PeriodType;
import cz.muni.fi.pv168.project.model.PeriodicTransaction;
import cz.muni.fi.pv168.project.ui.DateSuccessionEnforcer;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.NonNullDateEnforcer;
import cz.muni.fi.pv168.project.ui.model.LocalDateModel;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;
import cz.muni.fi.pv168.project.ui.renderer.LabeledListCellRenderer;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class PeriodicTransactionDialog extends BaseTransactionDialog<PeriodicTransaction> {
    private static final I18N I18N = new I18N(PeriodicTransactionDialog.class);
    private DateModel<LocalDate> startDateModel;
    private DateModel<LocalDate> endDateModel;
    private ComboBoxModel<PeriodType> periodTypeModel;
    private JFormattedTextField periodField;

    public PeriodicTransactionDialog(Component parent, @Nullable PeriodicTransaction transaction, MutableListModel<Category> categoryModel) {
        super(parent, transaction, categoryModel);
    }

    @Override
    protected void init() {
        startDateModel = new LocalDateModel();
        endDateModel = new LocalDateModel();
        periodTypeModel = new DefaultComboBoxModel<>(PeriodType.values());
        periodField = new JFormattedTextField(getIntegerFormatter());
    }

    @Override
    protected PeriodicTransaction newEmptyInstance() {
        var instance = new PeriodicTransaction();
        instance.setStartDate(LocalDate.now());
        instance.setPeriodicity(1);
        instance.setPeriodType(PeriodType.MONTHLY);
        return instance;
    }

    @Override
    protected void prefillFields() {
        super.prefillFields();
        startDateModel.setValue(transaction.getStartDate());
        endDateModel.setValue(transaction.getEndDate());
        periodTypeModel.setSelectedItem(transaction.getPeriodType());
        periodField.setValue(transaction.getPeriodicity());
    }

    @Override
    protected void addFields() {
        super.addFields();

        var startPicker = new JDatePicker(startDateModel);
        var endPicker = new JDatePicker(endDateModel);
        DateSuccessionEnforcer.enforce(startPicker, endPicker);
        NonNullDateEnforcer.enforce(startPicker);

        add(I18N.getString("field.start.date"), startPicker);
        add(I18N.getString("field.end.date"), endPicker);
        var periodComboBox = new JComboBox<>(periodTypeModel);
        periodComboBox.setRenderer(new PeriodTypeRenderer());
        add(I18N.getString("field.period.type"), periodComboBox);
        add(I18N.getString("field.periodicity"), periodField);
    }

    @Override
    protected void fillEntityFromUi() {
        super.fillEntityFromUi();
        transaction.setPeriodType((PeriodType) periodTypeModel.getSelectedItem());
        transaction.setPeriodicity(getIntegerFromTextFieldValue(periodField.getText()));
        transaction.setStartDate(startDateModel.getValue());
        transaction.setEndDate(endDateModel.getValue());
    }

    @Override
    protected List<String> validate(PeriodicTransaction entity) {
        var errors = new ArrayList<String>();
        if (entity.getStartDate() == null) {
            errors.add(I18N.getString("error.start.date"));
        }
        if (entity.getPeriodType() == null) {
            errors.add(I18N.getString("error.period.type"));
        }
        if (entity.getPeriodicity() == 0) {
            errors.add(I18N.getString("error.periodicity"));
        }
        return ListUtils.concat(super.validate(entity), errors);
    }

    private static class PeriodTypeRenderer extends LabeledListCellRenderer<PeriodType> {
        @Override
        protected String getLabelForItem(PeriodType item) {
            return I18N.getString(item.getName());
        }
    }
}
