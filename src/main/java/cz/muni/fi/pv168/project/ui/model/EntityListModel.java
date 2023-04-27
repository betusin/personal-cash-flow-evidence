package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.ListUtils;
import cz.muni.fi.pv168.project.data.DataAccessObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityListModel<T> extends AbstractListModel<T> implements ListModel<T>, MutableListModel<T> {
    private final DataAccessObject<T> dao;
    private final List<T> items;

    public EntityListModel(DataAccessObject<T> dao) {
        this.dao = dao;
        this.items = new ArrayList<>(dao.findAll());
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public T getElementAt(int index) {
        return items.get(index);
    }

    @Override
    public void deleteRow(T item) {
        var index = ListUtils.indexOfFirst(items, t -> t.equals(item));
        items.remove(item);
        dao.delete(item);
        fireIntervalRemoved(this, index, index);
    }

    @Override
    public void addRow(T row) {
        dao.create(row);
        items.add(row);
        int newRowIndex = items.size();
        fireIntervalAdded(this, newRowIndex, newRowIndex);
    }

    @Override
    public void updateRow(T row) {
        var index = ListUtils.indexOfFirst(items, item -> item.equals(row));
        dao.update(row);
        fireContentsChanged(this, index, index);
    }

    @Override
    public T getRow(int rowIndex) {
        return items.get(rowIndex);
    }

    @Override
    public List<T> getRows() {
        return Collections.unmodifiableList(items);
    }
}