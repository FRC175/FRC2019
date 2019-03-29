package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum LiftPosition {

    EXTEND(-1),
    RETRACT(1);

    private final double mPower;

    LiftPosition(double power) {
        mPower = power;
    }

    public double getPower() {
        return mPower;
    }

}