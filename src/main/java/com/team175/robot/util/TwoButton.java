package com.team175.robot.util;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * An object that allows for the use of a command when two buttons are pressed
 * at once.
 *
 * @author Arvind
 */
public class TwoButton extends Button {

    private GenericHID mJoystick;
    private int mFirstButtonNumber;
    private int mSecondButtonNumber;

    /**
     * Constructs a two joystick buttons into one object.
     *
     * @param joystick           The Joystick the buttons are a part of
     * @param firstButtonNumber  The number of the first button on the joystick
     * @param secondButtonNumber The number of the second button on the joystick
     */
    public TwoButton(GenericHID joystick, int firstButtonNumber, int secondButtonNumber) {
        mJoystick = joystick;
        mFirstButtonNumber = firstButtonNumber;
        mSecondButtonNumber = secondButtonNumber;
    }

    /**
     * Returns whether or not the two specified buttons are held down at once.
     *
     * @return Whether or not two buttons are held down
     */
    @Override
    public boolean get() {
        return mJoystick.getRawButton(mFirstButtonNumber) && mJoystick.getRawButton(mSecondButtonNumber);
    }

}
