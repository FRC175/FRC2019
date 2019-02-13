package com.team175.robot.util;

/**
 * A static utility class containing commonly used mathematical operations.
 * Deprecated due to functions being integrated into objects.
 *
 * @author Arvind
 */
@Deprecated
public final class AldrinMath {

    // Prevent AldrinMath from being instantiated
    private AldrinMath() {
    }

    /**
     * Adds n% deadzone to an input device.
     * Created to prevent usage of WPILib DifferentialDrive and thus WPI_TalonSRXs.
     * Deprecated because it's not longer used.
     *
     * @param value
     *         The inputted value as a decimal
     * @param deadzone
     *         The wanted deadzone as a decimal
     */
    public static double addDeadZone(double value, double deadzone) {
        if (Math.abs(value) > deadzone) {
            value = Math.signum(value) * (Math.abs(value) - deadzone) / (1.0 - deadzone);
        } else {
            value = 0.0;
        }

        return (Math.abs(value) > deadzone) ? value : 0.0;
    }

    /**
     * Calculates theoretical velocity in counts per 100 ms.
     *
     * @param velocity
     *         In RPM
     * @param countsPerRevolution
     *         In Talon SRX encoder counts
     * @param gearRatio
     *         As a ratio
     * @return Theoretical velocity
     */
    public static int calculateEmpiricalVelocity(int velocity, int countsPerRevolution, double gearRatio) {
        return (int) ((((double) velocity) / 600.0) * (((double) countsPerRevolution) / gearRatio));
    }

    /**
     * Calculates theoretical Kf gain.
     *
     * @param maxVelocity
     *         In counts per 100 ms
     * @return theoretical kF gain
     */
    public static double calculateKf(int maxVelocity) {
        return 1023.0 / ((double) maxVelocity);
    }

    /**
     * Calculates theoretical Kf gain with alternate encoder.
     *
     * @param maxVelocity
     *         In counts per 100 ms
     * @return theoretical kF gain
     */
    public static double calculateAltKf(int maxVelocity) {
        return 127.0 / ((double) maxVelocity);
    }

}
