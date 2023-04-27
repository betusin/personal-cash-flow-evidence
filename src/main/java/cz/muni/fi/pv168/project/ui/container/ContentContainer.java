package cz.muni.fi.pv168.project.ui.container;

import cz.muni.fi.pv168.project.ui.model.SizedModel;

import javax.swing.*;
import java.awt.*;

/**
 * Switches between {@link #contents} and {@link #emptyContents} depending
 * on the return value of {@link T#isEmpty()} of the provided {@link #model}.
 * @param <T> Model to check for emptiness
 */
public class ContentContainer<T extends SizedModel> extends JPanel {
    private final T model;
    private final Component contents;
    private final JPanel emptyContents;

    public ContentContainer(T model, Component contents, Component emptyContents) {
        super(new BorderLayout());
        setOpaque(false);
        this.model = model;
        this.contents = contents;
        this.emptyContents = wrapEmptyContents(emptyContents);
        model.addSizeChangedListener(this::onSizeChanged);
        onSizeChanged();
    }

    public ContentContainer(T model, Component component, String message) {
        this(model, component, new JLabel(message));
    }

    /**
     * Makes sure the contents are centered in this JPanel.
     *
     * @param emptyContents Component to display if model is empty
     * @return Centered empty contents
     */
    private JPanel wrapEmptyContents(Component emptyContents) {
        var wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        var gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        wrapper.add(emptyContents, gbc);
        return wrapper;
    }

    private void onSizeChanged() {
        Component toAdd = model.isEmpty() ? emptyContents : contents;
        ensureSingleChild(toAdd);
    }

    private void ensureSingleChild(Component toAdd) {
        var empty = getComponentCount() == 0;
        var add = false;
        if (empty) {
            add = true;
        } else if (getComponent(0) != toAdd) {
            remove(0);
            add = true;
        }
        if (add) {
            add(toAdd, BorderLayout.CENTER);
        }
    }
}
