package cz.muni.fi.pv168.project.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JCheckBoxList<T> extends JList<JCheckBoxList.CheckableItem<T>> {
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public JCheckBoxList() {
        setCellRenderer(new CellRenderer());
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                if (index != -1) {
                    CheckableItem<T> item = getModel().getElementAt(index);
                    item.checkBox.setSelected(!item.checkBox.isSelected());
                    repaint();
                }
            }
        });
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public JCheckBoxList(ListModel<CheckableItem<T>> model) {
        this();
        setModel(model);
    }

    @Override
    public int locationToIndex(Point location) {
        int index = super.locationToIndex(location);
        if (index == -1)
            return index;
        if (getCellBounds(index, index).contains(location))
            return index;
        return -1;
    }

    private class CellRenderer implements ListCellRenderer<CheckableItem<T>> {
        @Override
        public Component getListCellRendererComponent(JList<? extends CheckableItem<T>> list, CheckableItem<T> value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkbox = value.checkBox;
            checkbox.setText(value.item.toString());

            //Drawing checkbox, change the appearance here
            checkbox.setBackground(isSelected ? getSelectionBackground()
                    : getBackground());
            checkbox.setForeground(isSelected ? getSelectionForeground()
                    : getForeground());
            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);
            checkbox.setBorder(isSelected ? UIManager
                    .getBorder("List.focusCellHighlightBorder") : noFocusBorder);
            return checkbox;
        }
    }

    public static class CheckableItem<T> {
        private final JCheckBox checkBox = new JCheckBox();
        private final T item;


        public CheckableItem(T item) {
            this.item = item;
        }

        public T getItem() {
            return item;
        }

        public JCheckBox getCheckBox() {
            return checkBox;
        }
    }
}