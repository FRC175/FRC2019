package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

public class AldrinButton extends Button {

    private GenericHID mJoystick;
    private int mButtonNumber;

    public AldrinButton(GenericHID joystick, int buttonNumber) {
        mJoystick = joystick;
        mButtonNumber = buttonNumber;
    }

    @Override
    public boolean get() {
        return mJoystick.getRawButton(mButtonNumber) && mJoystick.getPOV() == -1;
    }

}
