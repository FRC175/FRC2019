package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * Allows for the use of a command when the hat switch (nub) and a button are pressed. Used in order to reuse buttons on
 * the Joystick. Thanks mechanical for making the robot too complicated!
 *
 * @author Arvind
 * @see Button
 */
public class NubDoubleButton extends Button {

    private GenericHID mJoystick;
    private int mButtonNumber;

    public NubDoubleButton(GenericHID joystick, int buttonNumber) {
        mJoystick = joystick;
        mButtonNumber = buttonNumber;
    }

    @Override
    public boolean get() {
        return mJoystick.getRawButton(mButtonNumber) && mJoystick.getPOV() > -1;
    }

}
