package cz.muni.fi.pv168.project.ui.model;

import javax.swing.table.AbstractTableModel;
import java.util.List;

abstract class AbstractEntityTableModel<E> extends AbstractTableModel implements EntityTableModel<E>, SizedModel {

    private final List<Column<E, ?>> columns;

    protected AbstractEntityTableModel(List<Column<E, ?>> columns) {
        this.columns = columns;
    }

    protected abstract void updateRow(E entity);

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var entity = getEntity(rowIndex);
        return getColumn(columnIndex).getValue(entity);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return getColumn(columnIndex).getColumnName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getColumn(columnIndex).getColumnClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return getColumn(columnIndex).isEditable(getEntity(rowIndex));
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var entity = getEntity(rowIndex);
        getColumn(columnIndex).setValue(value, entity);
        updateRow(entity);
    }

    private Column<E, ?> getColumn(int columnIndex) {
        return columns.get(columnIndex);
    }

    @Override
    public boolean isEmpty() {
        return getRowCount() == 0;
    }

    @Override
    public void addSizeChangedListener(OnSizeChangedListener listener) {
        addTableModelListener(e -> listener.onSizeChanged());
    }
}
