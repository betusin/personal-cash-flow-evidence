package cz.muni.fi.pv168.project.ui;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class DateSuccessionEnforcer implements PropertyChangeListener {
    private final DateModel<LocalDate> fromModel;
    private final DateModel<LocalDate> toModel;

    private DateSuccessionEnforcer(DateModel<LocalDate> fromModel, DateModel<LocalDate> toModel) {
        this.fromModel = fromModel;
        this.toModel = toModel;
    }

    private DateSuccessionEnforcer(JDatePicker from, JDatePicker to) {
        //noinspection unchecked
        this((DateModel<LocalDate>) from.getModel(), (DateModel<LocalDate>) to.getModel());
    }

    public static void enforce(JDatePicker from, JDatePicker to) {
        var instance = new DateSuccessionEnforcer(from, to);
        from.getModel().addPropertyChangeListener(instance);
        to.getModel().addPropertyChangeListener(instance);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        var from = fromModel.getValue();
        var to = toModel.getValue();
        // Ignore date ranges where one of the dates is null
        if (from == null || to == null) {
            return;
        }
        if (isValidSuccession(from, to)) {
            return;
        }
        if (evt.getSource() == fromModel) {
            toModel.setValue(from);
        } else {
            fromModel.setValue(to);
        }
    }

    private boolean isValidSuccession(LocalDate from, LocalDate to) {
        return !from.isAfter(to);
    }
}
