package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ManipulatorArmPosition {

    GROUND(294),
    BALL_PICKUP(186),
    SCORE(75),
    STOW(-190);

    private final int mPosition;

    ManipulatorArmPosition(int position) {
        mPosition = position;
    }

    public int positionToMove() {
        return mPosition;
    }

}
