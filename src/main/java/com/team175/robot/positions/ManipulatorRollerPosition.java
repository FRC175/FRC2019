package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ManipulatorRollerPosition {

    GRAB_CARGO(-1, 1),
    SCORE_CARGO(0.75, -0.75),
    SCORE_CARGO_FAST(1, -1),
    GRAB_HATCH(-1, -1),
    SCORE_HATCH(0, 0);

    private final double mFrontPower;
    private final double mRearPower;

    ManipulatorRollerPosition(double frontPower, double rearPower) {
        mFrontPower = frontPower;
        mRearPower = rearPower;
    }

    public double getFrontPower() {
        return mFrontPower;
    }

    public double getRearPower() {
        return mRearPower;
    }

}
