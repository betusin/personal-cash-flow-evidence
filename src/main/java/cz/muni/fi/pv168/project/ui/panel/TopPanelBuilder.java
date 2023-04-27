package cz.muni.fi.pv168.project.ui.panel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TopPanelBuilder {
    private final Color background;
    private final Insets componentInsets;
    private final List<Component> components = new ArrayList<>();

    public TopPanelBuilder(Color background, Insets componentInsets) {
        this.background = background;
        this.componentInsets = componentInsets;
    }

    public TopPanelBuilder addComponent(Component component) {
        components.add(component);
        return this;
    }

    public TopPanelBuilder addSpace() {
        components.add(null);
        return this;
    }

    public JPanel build() {
        var panel = new JPanel(new GridBagLayout());
        panel.setBackground(background);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = componentInsets;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        for (var component : components) {
            if (component != null) {
                addWidget(c, panel, component);
            } else {
                addSpace(c, panel);
            }
        }
        return panel;
    }

    private static void addWidget(GridBagConstraints c, JPanel panel, Component thing) {
        c.gridx++;
        panel.add(thing, c);
    }

    private static void addSpace(GridBagConstraints c, JPanel panel) {
        c.weightx = 1;
        addWidget(c, panel, Box.createHorizontalBox());
        c.weightx = 0;
    }
}
