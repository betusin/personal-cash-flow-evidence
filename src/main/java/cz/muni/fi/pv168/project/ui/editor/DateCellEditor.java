package cz.muni.fi.pv168.project.ui.editor;

import org.jdatepicker.JDatePicker;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.time.LocalDate;

public class DateCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JDatePicker picker;

    public DateCellEditor(JDatePicker picker) {
        this.picker = picker;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        LocalDate date = (LocalDate) value;
        picker.getModel().setDate(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth());
        return picker;
    }

    @Override
    public Object getCellEditorValue() {
        return picker.getModel().getValue();
    }
}
