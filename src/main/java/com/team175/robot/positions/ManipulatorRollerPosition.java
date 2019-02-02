package com.team175.robot.positions;

public enum ManipulatorRollerPosition {

    GRAB_CARGO(1, 1),
    SCORE_CARGO(-1, 0),
    GRAB_HATCH(1, 1),
    SCORE_HATCH(-1, -1),
    IDLE(0, 0);

    private final int mFrontPower;
    private final int mRearPower;

    ManipulatorRollerPosition(int frontPower, int rearPower) {
        mFrontPower = frontPower;
        mRearPower = rearPower;
    }

    public int getFrontPower() {
        return mFrontPower;
    }

    public int getRearPower() {
        return mRearPower;
    }

}
