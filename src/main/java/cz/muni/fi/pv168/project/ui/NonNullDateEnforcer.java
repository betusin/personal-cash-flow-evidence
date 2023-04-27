package cz.muni.fi.pv168.project.ui;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class NonNullDateEnforcer implements PropertyChangeListener {
    private final DateModel<LocalDate> model;

    private NonNullDateEnforcer(DateModel<LocalDate> model) {
        this.model = model;
    }

    private NonNullDateEnforcer(JDatePicker picker) {
        //noinspection unchecked
        this((DateModel<LocalDate>) picker.getModel());
    }

    public static void enforce(JDatePicker picker) {
        picker.getModel().addPropertyChangeListener(new NonNullDateEnforcer(picker));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals("value"))
            return;
        var newValue = (LocalDate) evt.getNewValue();
        if (newValue != null)
            return;
        var oldValue = (LocalDate) evt.getOldValue();
        model.setValue(oldValue);
    }
}
