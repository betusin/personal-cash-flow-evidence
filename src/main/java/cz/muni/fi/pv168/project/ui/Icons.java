package cz.muni.fi.pv168.project.ui;

import javax.swing.*;
import java.net.URL;

/**
 * @author Babetka Hajná
 */
public class Icons {
    public static final Icon ONE_TIME_ICON = createIcon("TransactionType-ONE_TIME-16.png");
    public static final Icon PERIODIC_ICON = createIcon("TransactionType-PERIODIC-16.png");

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    private static ImageIcon createIcon(String name) {
        URL url = Icons.class.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Icon resource not found on classpath: " + name);
        }
        return new ImageIcon(url);
    }
}
