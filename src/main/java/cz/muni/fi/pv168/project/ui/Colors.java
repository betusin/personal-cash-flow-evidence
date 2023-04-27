package cz.muni.fi.pv168.project.ui;

import java.awt.*;

public enum Colors {
    PRIMARY(Color.decode("#CC9900")),
    RED_TRANSPARENT(new Color(255, 0, 0, 50)),
    RED_SEMI_TRANSPARENT(new Color(255, 0, 0, 85)),
    GREEN_TRANSPARENT(new Color(0, 255, 0, 50)),
    GREEN_SEMI_TRANSPARENT(new Color(0, 255, 0, 85));

    private final Color color;

    Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
