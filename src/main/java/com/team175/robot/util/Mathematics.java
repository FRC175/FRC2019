package com.team175.robot.util;

public class Mathematics {

    /**
     * Add 5% deadzone to joystick movement.
     */
    public static double addDeadzone(double n) {
        return (n <= 0.05 && n >= -0.05) ? 0 : n;
    }

}
