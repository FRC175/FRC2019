package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * @author Arvind
 */
public class NubButton extends Button {

    private GenericHID mJoystick;
    private NubPosition mPosition;

    public enum NubPosition {
        NORTH(0),
        NORTH_EAST(45),
        EAST(90),
        SOUTH_EAST(135),
        SOUTH(180),
        SOUTH_WEST(225),
        WEST(270),
        NORTH_WEST(315),
        ALL(-254); // Because -1 is when nub is not moved

        private final int mPOV;

        NubPosition(int pov) {
            mPOV = pov;
        }

        public int getPOV() {
            return mPOV;
        }
    }

    public NubButton(GenericHID joystick, NubPosition position) {
        mJoystick = joystick;
        mPosition = position;
    }

    @Override
    public boolean get() {
        return mPosition != NubPosition.ALL ? mJoystick.getPOV() == mPosition.getPOV() : mJoystick.getPOV() != -1;
    }

}
