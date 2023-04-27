package cz.muni.fi.pv168.project.ui.panel;

import cz.muni.fi.pv168.project.ui.Colors;
import cz.muni.fi.pv168.project.ui.DateSuccessionEnforcer;
import cz.muni.fi.pv168.project.ui.I18N;
import cz.muni.fi.pv168.project.ui.NonNullDateEnforcer;
import cz.muni.fi.pv168.project.ui.filter.TransactionTypeOption;
import cz.muni.fi.pv168.project.ui.renderer.LabeledListCellRenderer;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * @author Alžbeta Hajná
 */
public class TopPanel extends Panel {
    private static final I18N I18N = new I18N(TopPanel.class);
    private static final Insets COMPONENT_INSETS = new Insets(4, 12, 4, 12);
    private JLabel currentBalance;
    private final ComboBoxModel<TransactionTypeOption> typeOptionModel;
    private final DateModel<LocalDate> balanceCalcDate;
    private final DateModel<LocalDate> fromDateModel;
    private final DateModel<LocalDate> toDateModel;
    private final Action addOneTimeTransactionAction;
    private final Action addPeriodicTransactionAction;
    private final Action editTransactionAction;
    private final Action deleteAction;

    public TopPanel(
            ComboBoxModel<TransactionTypeOption> typeOptionModel,
            DateModel<LocalDate> balanceCalcDate,
            DateModel<LocalDate> fromDateModel,
            DateModel<LocalDate> toDateModel,
            Action addOneTimeTransactionAction,
            Action addPeriodicTransactionAction,
            Action editTransactionAction,
            Action deleteAction
    ) {
        super(new BorderLayout());
        this.balanceCalcDate = balanceCalcDate;
        this.typeOptionModel = typeOptionModel;
        this.fromDateModel = fromDateModel;
        this.toDateModel = toDateModel;
        this.addOneTimeTransactionAction = addOneTimeTransactionAction;
        this.addPeriodicTransactionAction = addPeriodicTransactionAction;
        this.editTransactionAction = editTransactionAction;
        this.deleteAction = deleteAction;

        var topPanel = createTopPanelContents();
        var toolbar = createToolbarPanel();
        add(topPanel, BorderLayout.NORTH);
        add(toolbar, BorderLayout.SOUTH);
    }

    private JPanel createTopPanelContents() {
        return new TopPanelBuilder(Colors.PRIMARY.getColor(), COMPONENT_INSETS)
                .addComponent(createBalanceLabel())
                .addSpace()
                .addComponent(createDatePickers())
                .addSpace()
                .addComponent(createTypePicker())
                .addSpace()
                .addComponent(createAddButtons())
                .build();
    }

    private JPanel createAddButtons() {
        // Add one-time transaction button
        var addOneTimeTransactionButton = new JButton(I18N.getString("add.one.time"));
        addOneTimeTransactionButton.setAction(addOneTimeTransactionAction);
        addOneTimeTransactionButton.setBackground(Color.BLACK);

        // Add periodic transaction button
        var addPeriodicTransactionButton = new JButton(I18N.getString("add.periodic"));
        addPeriodicTransactionButton.setAction(addPeriodicTransactionAction);
        addPeriodicTransactionButton.setBackground(Color.BLACK);
        return wrapSubgroup(addOneTimeTransactionButton, addPeriodicTransactionButton);
    }

    private JPanel createTypePicker() {
        // Transaction type picker
        JComboBox<TransactionTypeOption> typePicker = new JComboBox<>(typeOptionModel);
        typePicker.setRenderer(new TypeFilterRenderer());
        var typePickerLabeled = addLabelToComponent(typePicker, I18N.getString("field.transaction.type"));
        return wrapSubgroup(typePickerLabeled);
    }

    private JPanel createDatePickers() {
        // From date
        var fromPicker = new JDatePicker(fromDateModel);
        NonNullDateEnforcer.enforce(fromPicker);
        var fromPickerLabeled = addLabelToComponent(fromPicker, I18N.getString("field.date.from"));

        // To date
        var toPicker = new JDatePicker(toDateModel);
        NonNullDateEnforcer.enforce(toPicker);
        var toPickerLabeled = addLabelToComponent(toPicker, I18N.getString("field.date.to"));

        DateSuccessionEnforcer.enforce(fromPicker, toPicker);
        return wrapSubgroup(fromPickerLabeled, toPickerLabeled);
    }

    private JPanel wrapSubgroup(Component... components) {
        var panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        for (var component : components) {
            panel.add(component, gbc);
            gbc.gridy++;
        }
        return panel;
    }

    private JPanel createBalanceLabel() {
        // Current balance value
        currentBalance = new JLabel();
        currentBalance.setBackground(Color.GRAY);
        currentBalance.setForeground(Color.WHITE);
        currentBalance.setOpaque(true);
        currentBalance.setFont(new Font("", Font.BOLD, 25));
        currentBalance.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        var balanceWithLabel = addLabelToComponent(currentBalance, I18N.getString("current.balance"));
        var picker = new JDatePicker(balanceCalcDate);
        var pickerWithLabel = addLabelToComponent(picker, I18N.getString("balance.date.to"));
        NonNullDateEnforcer.enforce(picker);
        return wrapSubgroup(balanceWithLabel, pickerWithLabel);
    }

    public void changeCurrentBalance(long amount) {
        currentBalance.setText(amount + " CZK");
    }

    private JPanel createToolbarPanel() {
        var toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(editTransactionAction);
        toolbar.addSeparator();
        toolbar.add(deleteAction);

        var toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.add(toolbar, BorderLayout.EAST);
        return toolbarPanel;
    }

    private JPanel addLabelToComponent(JComponent component, String label) {
        var panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(createLabelForComponent(label), BorderLayout.NORTH);
        panel.add(component, BorderLayout.SOUTH);
        return panel;
    }

    private JLabel createLabelForComponent(String text) {
        var currentBalanceLabel = new JLabel();
        currentBalanceLabel.setText(text);
        currentBalanceLabel.setFont(new Font("", Font.PLAIN, 10));
        currentBalanceLabel.setForeground(Color.DARK_GRAY);
        return currentBalanceLabel;
    }

    private static class TypeFilterRenderer extends LabeledListCellRenderer<TransactionTypeOption> {
        @Override
        protected String getLabelForItem(TransactionTypeOption item) {
            return I18N.getString(item.getName());
        }
    }
}
