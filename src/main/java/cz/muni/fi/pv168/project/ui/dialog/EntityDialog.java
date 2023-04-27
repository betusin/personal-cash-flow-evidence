package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.I18N;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static javax.swing.JOptionPane.*;

abstract class EntityDialog<E> {
    private static final I18N I18N = new I18N(EntityDialog.class);
    private final Component parent;
    private final JPanel panel = new JPanel();
    private int nextComponentRow = 0;

    EntityDialog(Component parent) {
        this.parent = parent;
        panel.setLayout(new GridBagLayout());
    }

    void add(String labelText, JComponent component) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = nextComponentRow++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 0.0;
        var label = new JLabel(labelText);
        label.setLabelFor(component);
        panel.add(label, c);
        c.gridx = 1;
        c.weightx = 1.0;
        panel.add(component, c);
    }

    void add(String labelText) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = nextComponentRow++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 0.0;
        var label = new JLabel(labelText);
        panel.add(label, c);
    }

    /**
     * @return error message if some data is invalid or empty optional if everything is fine.
     */
    protected List<String> validate(E entity) {
        return Collections.emptyList();
    }

    abstract E getEntity();

    public Optional<E> show(String title) {
        int result = JOptionPane.showOptionDialog(parent, panel, title,
                OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
        if (result == OK_OPTION) {
            var entity = getEntity();
            var validationErrors = validate(entity);
            if (!validationErrors.isEmpty()) {
                var errorMessage = String.join("\n", validationErrors);
                JOptionPane.showMessageDialog(parent, errorMessage,
                        I18N.getString("error.validation"), JOptionPane.WARNING_MESSAGE);
                return show(title);
            } else {
                return Optional.of(entity);
            }
        } else {
            return Optional.empty();
        }
    }

    public Component getParent() {
        return parent;
    }
}
