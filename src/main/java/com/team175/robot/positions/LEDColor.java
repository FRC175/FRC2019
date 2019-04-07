package com.team175.robot.positions;

import java.awt.Color;

public enum LEDColor {

    DEFAULT(255, 255, 0), // Yellow
    DISABLED(0, 255, 0), // Green
    GRABBED(0, 0, 0),
    ERROR(255, 0, 0),
    OFF(0, 0, 0);

    private final Color mColor;

    LEDColor(int r, int g, int b) {
        mColor = new Color(r, g, b);
    }

    public Color getColor() {
        return mColor;
    }

}
