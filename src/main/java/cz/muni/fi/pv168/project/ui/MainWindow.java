package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.dao.CategoryDao;
import cz.muni.fi.pv168.project.dao.TransactionDao;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Demo;
import cz.muni.fi.pv168.project.model.TransactionType;
import cz.muni.fi.pv168.project.ui.action.AddCategoryAction;
import cz.muni.fi.pv168.project.ui.action.AddOneTimeTransactionAction;
import cz.muni.fi.pv168.project.ui.action.AddPeriodicTransactionAction;
import cz.muni.fi.pv168.project.ui.action.DeleteAction;
import cz.muni.fi.pv168.project.ui.action.DeleteCategoryAction;
import cz.muni.fi.pv168.project.ui.action.EditCategoryAction;
import cz.muni.fi.pv168.project.ui.action.EditTransactionAction;
import cz.muni.fi.pv168.project.ui.container.ContentContainer;
import cz.muni.fi.pv168.project.ui.editor.DateCellEditor;
import cz.muni.fi.pv168.project.ui.filter.TransactionRowFilter;
import cz.muni.fi.pv168.project.ui.filter.TransactionTypeOption;
import cz.muni.fi.pv168.project.ui.model.CheckableListModel;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;
import cz.muni.fi.pv168.project.ui.model.EntityListModel;
import cz.muni.fi.pv168.project.ui.model.LocalDateModel;
import cz.muni.fi.pv168.project.ui.model.MutableListModel;
import cz.muni.fi.pv168.project.ui.model.TransactionTableModel;
import cz.muni.fi.pv168.project.ui.panel.CategoryPanel;
import cz.muni.fi.pv168.project.ui.panel.TopPanel;
import cz.muni.fi.pv168.project.ui.renderer.ColorTableCellRenderer;
import cz.muni.fi.pv168.project.ui.renderer.TransactionTypeRenderer;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class MainWindow {
    private final JFrame frame;
    private final TopPanel topPanel;
    private final CategoryPanel categoryPanel;
    private final JTable transactionsTable;
    private JLabel visibleItemsTotal;

    private final TransactionDao transactionDao;

    private final TransactionTableModel transactionTableModel;
    private final CheckableListModel<Category> categoriesCheckBoxListModel;
    private final LocalDateModel balanceCalculationLimit;
    private final LocalDateModel fromDateModel;
    private final LocalDateModel toDateModel;

    private final Action addOneTimeTransactionAction;
    private final Action addPeriodicTransactionAction;
    private final Action editTransactionAction;
    private final Action deleteAction;

    private final EditCategoryAction editCategoryAction;
    private final DeleteCategoryAction deleteCategoryAction;

    private static final I18N I18N = new I18N(MainWindow.class);

    public MainWindow(TransactionDao transactionDao, CategoryDao categoryDao) {
        this.transactionDao = transactionDao;
        frame = createFrame();

        // Model
        balanceCalculationLimit = new LocalDateModel();
        fromDateModel = new LocalDateModel();
        toDateModel = new LocalDateModel();
        var today = LocalDate.now();
        fromDateModel.setValue(today.minus(1, ChronoUnit.MONTHS));
        fromDateModel.addChangeListener(e -> filterCriteriaChanged());
        toDateModel.setValue(today);
        toDateModel.addChangeListener(e -> filterCriteriaChanged());
        balanceCalculationLimit.setValue(today);
        balanceCalculationLimit.addChangeListener(e -> updateOverallTotal());
        transactionTableModel = new TransactionTableModel(transactionDao, fromDateModel, toDateModel);
        transactionTableModel.addTableModelListener(e -> updateTotals());
        categoriesCheckBoxListModel = new CheckableListModel<>(new EntityListModel<>(categoryDao), true);
        categoriesCheckBoxListModel.addListDataListener(new ListDataChangeAdapter((e) -> categoryStateChanged()));
        var categoriesListModel = categoriesCheckBoxListModel.getUnderlyingModel();
        var typeOptionModel = new DefaultComboBoxModel<>(TransactionTypeOption.values());
        typeOptionModel.addListDataListener(new ListDataChangeAdapter(e -> filterCriteriaChanged()));

        // Transactions table
        transactionsTable = createTransactionTable(transactionTableModel, typeOptionModel, categoriesListModel, fromDateModel, toDateModel);

        // Actions
        addOneTimeTransactionAction = new AddOneTimeTransactionAction(frame, transactionsTable, categoriesListModel);
        addPeriodicTransactionAction = new AddPeriodicTransactionAction(transactionsTable, categoriesListModel);
        editTransactionAction = new EditTransactionAction(frame, transactionsTable, categoriesListModel);
        deleteAction = new DeleteAction(frame, transactionsTable);
        editCategoryAction = new EditCategoryAction(frame, categoriesCheckBoxListModel);
        deleteCategoryAction = new DeleteCategoryAction(frame, categoriesCheckBoxListModel);
        AddCategoryAction addCategoryAction = new AddCategoryAction(frame, categoriesCheckBoxListModel.getUnderlyingModel());

        // Needs to be AFTER action initialization
        transactionsTable.setComponentPopupMenu(createEmployeeTablePopupMenu());

        // UI panels
        var transactionsPane = new JScrollPane(transactionsTable);
        topPanel = new TopPanel(typeOptionModel, balanceCalculationLimit, fromDateModel, toDateModel, addOneTimeTransactionAction,
                addPeriodicTransactionAction, editTransactionAction, deleteAction);
        categoryPanel = new CategoryPanel(editCategoryAction, deleteCategoryAction,
                addCategoryAction, categoriesCheckBoxListModel);
        var visibleItemsPanel = createVisibleItemsPanel();

        var transactionsContainer = new ContentContainer<>(transactionTableModel, transactionsPane, "No transactions");
        frame.add(transactionsContainer, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);
        frame.add(categoryPanel, BorderLayout.WEST);
        frame.add(visibleItemsPanel, BorderLayout.SOUTH);
        frame.pack();

        // Set Confirmation Dialog's buttons labels
        UIManager.put("OptionPane.cancelButtonText", I18N.getString("cancelButton"));
        UIManager.put("OptionPane.okButtonText", I18N.getString("okButton"));

//        initializeDemoData();
        initializeEmptyData();
        categoryStateChanged();
    }

    private void initializeDemoData() {
        categoriesCheckBoxListModel.getUnderlyingModel().addRows(Demo.DEMO_CATEGORIES);
        transactionTableModel.addRows(Demo.DEMO_ITEMS_BOTH);
    }

    private void initializeEmptyData() {
        categoriesCheckBoxListModel.getUnderlyingModel().addRows(new ArrayList<>());
        transactionTableModel.addRows(new ArrayList<>());
    }

    private JPanel createVisibleItemsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        visibleItemsTotal = new JLabel();
        visibleItemsTotal.setFont(new Font("", Font.BOLD, 14));
        panel.add(visibleItemsTotal, BorderLayout.LINE_END);
        return panel;
    }

    public void show() {
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private JFrame createFrame() {
        var frame = new JFrame(I18N.getString("frameName"));
        frame.setMinimumSize(new Dimension(800, 400));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private JTable createTransactionTable(TransactionTableModel transactionTableModel,
                                          ComboBoxModel<TransactionTypeOption> typeOptionModel,
                                          MutableListModel<Category> categoryModel,
                                          LocalDateModel fromDateModel,
                                          LocalDateModel toDateModel) {
        transactionTableModel.addTableModelListener(e -> updateCategoryDeletionPossibility());
        var table = new JTable(transactionTableModel);
        table.setDefaultRenderer(TransactionType.class, new TransactionTypeRenderer());
        table.setDefaultRenderer(Object.class, new ColorTableCellRenderer());
        table.setDefaultRenderer(Long.class, new ColorTableCellRenderer());
        TransactionRowFilter filter = new TransactionRowFilter(transactionTableModel, categoriesCheckBoxListModel,
                typeOptionModel, fromDateModel, toDateModel);
        var sorter = new TableRowSorter<>(transactionTableModel);
        sorter.toggleSortOrder(TransactionTableModel.DATE_COLUMN_INDEX);
        sorter.setRowFilter(filter);
        table.setRowSorter(sorter);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        var categoriesComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(categoryModel));
        table.setDefaultEditor(Category.class, new DefaultCellEditor(categoriesComboBox));
        var datePicker = new JDatePicker(new LocalDateModel());
        table.setDefaultEditor(LocalDate.class, new DateCellEditor(datePicker));
        return table;
    }

    private JPopupMenu createEmployeeTablePopupMenu() {
        var menu = new JPopupMenu();
        menu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> selectTransactionRowBelowPopupMenuIfNotSelected(menu));
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        menu.add(addOneTimeTransactionAction);
        menu.add(addPeriodicTransactionAction);
        menu.add(editTransactionAction);
        menu.add(deleteAction);
        return menu;
    }

    private void selectTransactionRowBelowPopupMenuIfNotSelected(JPopupMenu menu) {
        var point = SwingUtilities.convertPoint(menu, new Point(0, 0), transactionsTable);
        var rowAtPoint = transactionsTable.rowAtPoint(point);
        if (rowAtPoint != -1 && !transactionsTable.getSelectionModel().isSelectedIndex(rowAtPoint)) {
            transactionsTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
        }
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();

        var oneRowSelected = selectionModel.getSelectedItemsCount() == 1;
        var anySelected = selectionModel.getSelectedItemsCount() > 0;

        editTransactionAction.setEnabled(oneRowSelected);
        deleteAction.setEnabled(anySelected);
    }

    private void categoryStateChanged() {
        updateCategoryDeletionPossibility();
        updateCategoryEditionPossibility();
        filterCriteriaChanged();
    }

    private void filterCriteriaChanged() {
        // This is a hack - the date will be immediately reverted to a valid value,
        // ignore the brief null that would throw exceptions in transactionTableModel.
        // The automatic null-revert is done using NonNullDateEnforcer in TopPanel.
        if (fromDateModel.getValue() == null || toDateModel.getValue() == null)
            return;
        transactionTableModel.reload();
    }

    private void updateTotals() {
        updateVisibleItemsTotal();
        updateOverallTotal();
    }

    private void updateCategoryDeletionPossibility() {
        var selected = categoriesCheckBoxListModel.getSelectedItems();
        var oneCategorySelected = !selected.isEmpty() && selected.stream().noneMatch(this::isCategoryUsed);
        deleteCategoryAction.setEnabled(oneCategorySelected);
    }

    private void updateCategoryEditionPossibility() {
        var selected = categoriesCheckBoxListModel.getSelectedItems();
        editCategoryAction.setEnabled(selected.size() == 1);
    }

    private void updateVisibleItemsTotal() {
        long sum = calculateVisibleItemsTotal();
        visibleItemsTotal.setText(I18N.getString("visibleSum") + sum + " CZK");
    }

    private long calculateVisibleItemsTotal() {
        return IntStream.range(0, transactionsTable.getRowCount())
                .map(transactionsTable::convertRowIndexToModel)
                .mapToLong(value -> transactionTableModel.getEntity(value).getTransaction().getAmount())
                .reduce(0L, Long::sum);
    }

    private void updateOverallTotal() {
        var date = balanceCalculationLimit.getValue();
        // This is a hack - the date will be immediately reverted to a valid value,
        // ignore the brief null that would throw exceptions in transactionTableModel.
        // The automatic null-revert is done using NonNullDateEnforcer in TopPanel.
        if (date == null)
            return;
        topPanel.changeCurrentBalance(TransactionCalculator.calculateOverallTotal(transactionDao.findAll(), date));
    }

    private boolean isCategoryUsed(Category category) {
        return transactionTableModel.getAllEntities().stream()
                .filter(item -> item.getCategory() != null)
                .anyMatch(item -> item.getCategory().equals(category));
    }

    public JFrame getFrame() {
        return frame;
    }
}
