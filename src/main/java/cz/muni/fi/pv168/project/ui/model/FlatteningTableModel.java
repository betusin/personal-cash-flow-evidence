package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.ListUtils;
import cz.muni.fi.pv168.project.data.DataAccessObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Allows expanding of a single entity into multiple rows.
 * {@link #reload()} must be called after construction to load initial data!
 *
 * @param <R> Row type
 * @param <E> Entity type
 */
public abstract class FlatteningTableModel<R, E> extends AbstractEntityTableModel<R> {
    private final DataAccessObject<E> dao;
    private final List<E> entities;
    private final List<R> rows;

    public FlatteningTableModel(List<Column<R, ?>> columns, DataAccessObject<E> dao) {
        super(columns);
        this.dao = dao;
        this.entities = new ArrayList<>();
        this.rows = new ArrayList<>();
    }


    /**
     * Expands an entity into possibly multiple rows.
     *
     * @param entity Entity to convert to row(s)
     * @return A list of zero or more rows
     */
    protected abstract List<R> getRowsFromEntity(E entity);

    /**
     * Returns the entity that was used to produce the row.
     *
     * @param row The row to get the entity for
     * @return Entity which belongs to row
     */
    protected abstract E getEntityFromRow(R row);


    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public void updateRow(R item) {
        var entity = getEntityFromRow(item);
        updateEntity(entity);
    }

    public void updateEntity(E entity) {
        dao.update(entity);
        // Solves the problem where an updated entity expands to a different
        // count of rows than it did before the update
        removeRowsForEntity(entity);
        addRowsForEntity(entity);
    }

    public void deleteRow(int rowIndex) {
        var entity = getEntityFromRow(rows.get(rowIndex));
        deleteRow(entity);
    }

    public void deleteRow(E entity) {
        dao.delete(entity);
        entities.remove(entity);
        removeRowsForEntity(entity);
    }

    private void removeRowsForEntity(E entity) {
        var indices = getIndicesOfEntity(Collections.unmodifiableList(rows), entity);
        ListUtils.removeItemsAtIndices(rows, indices);
        var firstIndex = indices.get(0);
        var lastIndex = indices.get(indices.size() - 1);
        fireTableRowsDeleted(firstIndex, lastIndex);
    }

    public void addRow(E entity) {
        dao.create(entity);
        entities.add(entity);
        addRowsForEntity(entity);
    }

    private void addRowsForEntity(E entity) {
        var originalSize = rows.size();
        var newItems = getRowsFromEntity(entity);
        if (newItems.isEmpty())
            return;
        rows.addAll(newItems);
        var lastIndexToAdd = originalSize + newItems.size() - 1;
        fireTableRowsInserted(originalSize, lastIndexToAdd);
    }

    public void addRows(List<E> entities) {
        entities.forEach(this::addRow);
    }

    /**
     * Re-creates all items including re-loading them from the database.
     * This is useful when an entity now expands to a different set of rows.
     */
    public void reload() {
        entities.clear();
        rows.clear();
        entities.addAll(dao.findAll());
        rows.addAll(mapEntitiesToRows(entities));
        fireTableDataChanged();
    }

    public R getEntity(int rowIndex) {
        return rows.get(rowIndex);
    }

    public List<R> getAllRows() {
        return Collections.unmodifiableList(rows);
    }

    public List<E> getAllEntities() {
        return Collections.unmodifiableList(entities);
    }


    private List<R> mapEntitiesToRows(Collection<E> entities) {
        return entities.stream()
                .flatMap((Function<E, Stream<R>>) e -> getRowsFromEntity(e).stream())
                .collect(Collectors.toList());
    }

    protected List<Integer> getIndicesOfEntity(List<R> rows, E entity) {
        return ListUtils.indicesOf(rows, (row) -> getEntityFromRow(row).equals(entity));
    }
}
