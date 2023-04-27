package cz.muni.fi.pv168.project.ui.renderer;

import javax.swing.*;
import java.awt.*;

public abstract class LabeledListCellRenderer<T> extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        var label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        //noinspection unchecked
        label.setText(getLabelForItem((T) value));
        return label;
    }

    protected abstract String getLabelForItem(T item);
}
