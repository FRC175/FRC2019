package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A modified version of Joystick that allows for the addition of a software dead zone.
 *
 * @author Arvind
 * @see Joystick
 */
public class AldrinJoystick extends Joystick {

    /**
     * The possible dead zone to add.
     */
    private double mDeadZone;

    /**
     * Constructs an instance of a joystick. The joystick index is the USB port on the drivers
     * station.
     *
     * @param port
     *         The port on the Driver Station that the joystick is plugged into.
     */
    public AldrinJoystick(int port) {
        super(port);
        mDeadZone = 0;
    }

    /**
     * Constructs an instance of a joystick with a specified dead zone. The joystick index is the USB port on the drivers
     * station.
     *
     * @param port
     *         The port on the Driver Station that the joystick is plugged into.
     * @param deadZone
     *         The percent of dead zone to add to the joystick axises.
     */
    public AldrinJoystick(int port, double deadZone) {
        super(port);
        mDeadZone = deadZone;
    }

    /**
     * Adjusts dead zone of joystick's axises.
     *
     * @param deadZone
     *         The percent of dead zone to add to the joystick axises.
     */
    public void setDeadZone(int deadZone) {
        mDeadZone = deadZone;
    }

    /**
     * Gets the value of the axis and accounts for dead zone.
     *
     * @param axis
     *         The axis to read
     * @return The value of the axis
     */
    @Override
    public double getRawAxis(int axis) {
        double value = super.getRawAxis(axis);

        if (Math.abs(value) > mDeadZone) {
            value = Math.signum(value) * (Math.abs(value) - mDeadZone) / (1.0 - mDeadZone);
        } else {
            value = 0.0;
        }

        return (Math.abs(value) > mDeadZone) ? value : 0.0;
    }

}
