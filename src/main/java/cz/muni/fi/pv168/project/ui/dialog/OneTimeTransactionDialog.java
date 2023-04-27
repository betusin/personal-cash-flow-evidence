package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ListUtils;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.OneTimeTransaction;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.NonNullDateEnforcer;
import cz.muni.fi.pv168.project.ui.model.LocalDateModel;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class OneTimeTransactionDialog extends BaseTransactionDialog<OneTimeTransaction> {
    private static final I18N I18N = new I18N(OneTimeTransactionDialog.class);
    private DateModel<LocalDate> dateModel;

    public OneTimeTransactionDialog(Component parent, @Nullable OneTimeTransaction transaction, MutableListModel<Category> categoryModel) {
        super(parent, transaction, categoryModel);
    }

    @Override
    protected void init() {
        dateModel = new LocalDateModel();
    }

    @Override
    protected OneTimeTransaction newEmptyInstance() {
        var instance = new OneTimeTransaction();
        instance.setDate(LocalDate.now());
        return instance;
    }

    @Override
    protected void prefillFields() {
        super.prefillFields();
        dateModel.setValue(transaction.getDate());
    }

    @Override
    protected void addFields() {
        super.addFields();
        var picker = new JDatePicker(dateModel);
        NonNullDateEnforcer.enforce(picker);
        add(I18N.getString("field.date"), picker);
    }

    @Override
    protected void fillEntityFromUi() {
        super.fillEntityFromUi();
        transaction.setDate(dateModel.getValue());
    }

    @Override
    protected List<String> validate(OneTimeTransaction entity) {
        var errors = new ArrayList<String>();
        if (entity.getDate() == null) {
            errors.add(I18N.getString("error.date"));
        }
        return ListUtils.concat(super.validate(entity), errors);
    }
}
