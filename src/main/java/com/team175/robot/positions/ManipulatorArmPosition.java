package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ManipulatorArmPosition {

    GROUND(-2340),
    BALL_PICKUP(-2231),
    SCORE(-2128),
    STOW(-1850);

    private final int mPosition;

    ManipulatorArmPosition(int position) {
        mPosition = position;
    }

    public int positionToMove() {
        return mPosition;
    }

}
