package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum LiftPosition {

    EXTEND(0.75),
    RETRACT(0.75),
    IDLE(0);

    private final double mPower;

    LiftPosition(double power) {
        mPower = power;
    }

    public double getPower() {
        return mPower;
    }

}
