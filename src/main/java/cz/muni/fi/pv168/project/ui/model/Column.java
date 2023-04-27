package cz.muni.fi.pv168.project.ui.model;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class Column<E, T> {
    private final String columnName;
    private final Class<T> columnClass;
    private final Function<E, T> valueGetter;

    private Column(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        // see Item 49: Check parameters for validity
        this.columnName = Objects.requireNonNull(columnName, "columnName");
        this.columnClass = Objects.requireNonNull(columnClass, "columnClass");
        this.valueGetter = Objects.requireNonNull(valueGetter, "valueGetter");
    }

    // see Item 1: Consider static factory methods instead of constructors
    static <E, T> Column<E, T> editable(String columnName, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
        return new PossiblyEditable<>(columnName, columnClass, e -> true, valueGetter, valueSetter);
    }

    // see Item 1: Consider static factory methods instead of constructors
    static <E, T> Column<E, T> readOnly(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        return new PossiblyEditable<>(columnName, columnClass, e -> false, valueGetter, (e, t) -> {
        });
    }

    // see Item 1: Consider static factory methods instead of constructors
    static <E, T> Column<E, T> possiblyEditable(String columnName, Class<T> columnClass, Predicate<E> isEditable, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
        return new PossiblyEditable<>(columnName, columnClass, isEditable, valueGetter, valueSetter);
    }

    Object getValue(E entity) {
        return valueGetter.apply(entity);
    }

    abstract void setValue(Object value, E entity);

    String getColumnName() {
        return columnName;
    }

    Class<T> getColumnClass() {
        return columnClass;
    }

    abstract boolean isEditable(E entity);

    private static class PossiblyEditable<E, T> extends Column<E, T> {
        private final BiConsumer<E, T> valueSetter;
        private final Predicate<E> isEditablePredicate;

        private PossiblyEditable(String columnName, Class<T> columnClass, Predicate<E> isEditablePredicate, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
            super(columnName, columnClass, valueGetter);
            this.isEditablePredicate = isEditablePredicate;
            this.valueSetter = Objects.requireNonNull(valueSetter, "valueSetter");
        }

        @Override
        boolean isEditable(E entity) {
            return isEditablePredicate.test(entity);
        }

        @Override
        void setValue(Object value, E entity) {
            if (isEditable(entity)) {
                valueSetter.accept(entity, getColumnClass().cast(value));
            } else {
                throw new UnsupportedOperationException("Column '" + getColumnName() + "' is not editable");
            }
        }
    }
}
