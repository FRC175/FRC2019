package com.team175.robot.util.drivers;

import com.team175.robot.util.AldrinMath;
import edu.wpi.first.wpilibj.Joystick;

/**
 * @author Arvind
 */
public class AldrinJoystick extends Joystick {

    private double mDeadZone;

    /**
     * Construct an instance of a joystick. The joystick index is the USB port on the drivers
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
     * Construct an instance of a joystick with a specified dead zone. The joystick index is the USB port on the drivers
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
        return AldrinMath.addDeadZone(super.getRawAxis(axis), mDeadZone);
    }

}
