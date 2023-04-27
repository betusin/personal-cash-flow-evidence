package cz.muni.fi.pv168.project.ui.renderer;

import cz.muni.fi.pv168.project.model.TransactionType;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.Icons;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @author Alžbeta Hajná
 */
public class TransactionTypeRenderer extends ColorTableCellRenderer {
    private static final I18N I18N = new I18N(TransactionTypeRenderer.class);
    private final Map<TransactionType, Icon> ICONS = Map.of(
            TransactionType.PERIODIC, Icons.PERIODIC_ICON,
            TransactionType.ONE_TIME, Icons.ONE_TIME_ICON
    );
    private final Map<TransactionType, String> LABELS = Map.of(
            TransactionType.PERIODIC, "periodic",
            TransactionType.ONE_TIME, "oneTime"
    );

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        var label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        TransactionType type = (TransactionType) value;
        label.setIcon(ICONS.get(type));
        label.setText(I18N.getString(LABELS.get(type)));
        return label;
    }
}
