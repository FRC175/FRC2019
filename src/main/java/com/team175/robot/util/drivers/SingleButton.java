package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * @author Arvind
 * @see Button
 */
public class SingleButton extends Button {

    private GenericHID mJoystick;
    private int mButtonNumber;

    public SingleButton(GenericHID joystick, int buttonNumber) {
        mJoystick = joystick;
        mButtonNumber = buttonNumber;
    }

    /**
     * Used in order to determine if both the button is pressed down and the nub is not off center.
     *
     * @return Whether the button is pressed and the nub is not moved
     */
    @Override
    public boolean get() {
        return mJoystick.getRawButton(mButtonNumber) && mJoystick.getPOV() == -1;
    }

}
