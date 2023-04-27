package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.ui.JCheckBoxList;
import cz.muni.fi.pv168.project.ui.ListDataChangeAdapter;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A slightly over-engineered model, which allows its items to be shown in a list of checkboxes.
 *
 * @param <T> Type of the model item
 */
public class CheckableListModel<T> extends AbstractListModel<JCheckBoxList.CheckableItem<T>> implements SizedModel {
    private final MutableListModel<T> model;
    private final List<JCheckBoxList.CheckableItem<T>> items;
    private final boolean checkedByDefault;

    public CheckableListModel(MutableListModel<T> model, boolean checkedByDefault) {
        this.model = model;
        this.items = new ArrayList<>(0);
        this.checkedByDefault = checkedByDefault;
        model.addListDataListener(new Listener());
        resetItems();
    }


    public MutableListModel<T> getUnderlyingModel() {
        return model;
    }

    public List<T> getSelectedItems() {
        return items
                .stream()
                .filter(item -> item.getCheckBox().isSelected())
                .map(JCheckBoxList.CheckableItem::getItem)
                .collect(Collectors.toList());
    }

    public void setSelectedItems(List<T> selectedItems) {
        for (int i = 0; i < items.size(); i++) {
            var wrapped = items.get(i);
            wrapped.getCheckBox().setSelected(selectedItems.contains(wrapped.getItem()));
            fireContentsChanged(this, i, i);
        }
    }

    public void selectAll() {
        setSelectedItems(getUnderlyingModel().getRows());
    }

    public void selectNone() {
        setSelectedItems(Collections.emptyList());
    }

    @Override
    public int getSize() {
        return model.getSize();
    }

    @Override
    public JCheckBoxList.CheckableItem<T> getElementAt(int index) {
        return items.get(index);
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public void addSizeChangedListener(OnSizeChangedListener listener) {
        addListDataListener(new ListDataChangeAdapter(e -> listener.onSizeChanged()));
    }

    private JCheckBoxList.CheckableItem<T> wrapItem(T item) {
        var wrapped = new JCheckBoxList.CheckableItem<>(item);
        wrapped.getCheckBox().addItemListener(e -> fireContentsChanged(this, 0, getSize() - 1));
        if (checkedByDefault)
            wrapped.getCheckBox().setSelected(true);
        return wrapped;
    }

    private void resetItems() {
        var previous = items.stream()
                .map(JCheckBoxList.CheckableItem::getItem)
                .collect(Collectors.toUnmodifiableSet());
        var previouslyChecked = items.stream()
                .filter(item -> item.getCheckBox().isSelected())
                .map(JCheckBoxList.CheckableItem::getItem)
                .collect(Collectors.toUnmodifiableSet());
        var newItems = model.getRows()
                .stream()
                .map(this::wrapItem)
                .collect(Collectors.toList());
        items.clear();
        items.addAll(newItems);
        items.stream()
                .filter(item -> previous.contains(item.getItem()))
                .forEach(item -> item.getCheckBox().setSelected(previouslyChecked.contains(item.getItem())));
    }

    private class Listener implements ListDataListener {
        @Override
        public void intervalAdded(ListDataEvent e) {
            resetItems();
            fireIntervalAdded(e.getSource(), e.getIndex0(), e.getIndex1());
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            resetItems();
            fireIntervalRemoved(e.getSource(), e.getIndex0(), e.getIndex1());
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            resetItems();
            fireContentsChanged(e.getSource(), e.getIndex0(), e.getIndex1());
        }
    }
}
