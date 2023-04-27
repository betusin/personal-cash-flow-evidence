package cz.muni.fi.pv168.project.ui.renderer;

import cz.muni.fi.pv168.project.ui.Colors;
import cz.muni.fi.pv168.project.ui.model.TransactionTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Babetka Hajná
 */
public class ColorTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        var model = (TransactionTableModel) table.getModel();
        var item = model.getEntity(table.convertRowIndexToModel(row));
        var cellValue = item.getTransaction().getAmount();

        if (isSelected) {
            component.setBackground(cellValue < 0 ? Colors.RED_SEMI_TRANSPARENT.getColor() : Colors.GREEN_SEMI_TRANSPARENT.getColor());
        } else {
            component.setBackground(cellValue < 0 ? Colors.RED_TRANSPARENT.getColor() : Colors.GREEN_TRANSPARENT.getColor());
        }
        component.setForeground(Color.WHITE);
        return component;
    }
}
