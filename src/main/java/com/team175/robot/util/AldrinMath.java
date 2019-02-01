package com.team175.robot.util;

public class AldrinMath {

    /**
     * Adds n% deadzone to an input device.
     * Was created to prevent usage of WPILib DifferentialDrive and thus WPI_TalonSRXs.
     *
     * @param value    The inputted value as a decimal
     * @param deadzone The wanted deadzone as a decimal
     */
    public static double addDeadzone(double value, double deadzone) {
        if (Math.abs(value) > deadzone) {
            value = Math.signum(value) * (Math.abs(value) - deadzone) / (1.0 - deadzone);
        } else {
            value = 0.0;
        }

        return (Math.abs(value) > deadzone) ? value : 0.0;
    }

}
