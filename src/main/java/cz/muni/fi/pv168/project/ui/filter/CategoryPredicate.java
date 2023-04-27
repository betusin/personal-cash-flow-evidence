package cz.muni.fi.pv168.project.ui.filter;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.TransactionItem;
import cz.muni.fi.pv168.project.ui.model.CheckableListModel;

import java.util.function.Predicate;

public class CategoryPredicate implements Predicate<TransactionItem> {
    private final CheckableListModel<Category> categoryModel;

    public CategoryPredicate(CheckableListModel<Category> categoryModel) {
        this.categoryModel = categoryModel;
    }

    @Override
    public boolean test(TransactionItem item) {
        var selectedCategories = categoryModel.getSelectedItems();
        return selectedCategories.stream()
                .filter(category -> item.getTransaction().getCategory() != null)
                .anyMatch(category -> item.getTransaction().getCategory().equals(category));
    }
}
