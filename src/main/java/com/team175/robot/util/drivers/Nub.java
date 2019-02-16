package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * A wrapper class around the POV/Hat Switch of the Joystick to act as a button.
 *
 * @author Arvind
 */
public class Nub extends Button {

    private GenericHID mJoystick;

    public Nub(GenericHID joystick) {
        mJoystick = joystick;
    }

    @Override
    public boolean get() {
        return mJoystick.getPOV() > -1;
    }

}
