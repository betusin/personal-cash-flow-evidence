package cz.muni.fi.pv168.project.ui.button;

import cz.muni.fi.pv168.project.ui.Colors;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alžbeta Hajná
 */
public final class CategoryButtonFactory {

    private CategoryButtonFactory() {

    }

    public static JButton createCategoryButton(Action action) {
        var button = new JButton(action);
        setup(button);
        return button;
    }

    public static JButton createCategoryButton(String text) {
        var button = new JButton(text);
        setup(button);
        return button;
    }

    private static void setup(JButton button) {
        button.setBackground(Colors.PRIMARY.getColor());
        button.setForeground(Color.BLACK);
    }
}
