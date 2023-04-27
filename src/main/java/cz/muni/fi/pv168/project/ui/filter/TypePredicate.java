package cz.muni.fi.pv168.project.ui.filter;

import cz.muni.fi.pv168.project.model.TransactionItem;

import javax.swing.*;
import java.util.function.Predicate;

public class TypePredicate implements Predicate<TransactionItem> {
    private final ComboBoxModel<TransactionTypeOption> typeOptionModel;

    public TypePredicate(ComboBoxModel<TransactionTypeOption> typeOptionModel) {
        this.typeOptionModel = typeOptionModel;
    }

    @Override
    public boolean test(TransactionItem item) {
        var typeOption = (TransactionTypeOption) typeOptionModel.getSelectedItem();
        return typeOption.allows(item.getType());
    }
}
