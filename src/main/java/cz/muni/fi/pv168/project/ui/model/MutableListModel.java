package cz.muni.fi.pv168.project.ui.model;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

public interface MutableListModel<T> extends ListModel<T> {
    void deleteRow(T row);

    void addRow(T row);

    default void addRows(Collection<T> rows) {
        rows.forEach(this::addRow);
    }

    void updateRow(T row);

    T getRow(int rowIndex);

    List<T> getRows();
}
