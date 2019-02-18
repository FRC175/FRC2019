package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ManipulatorArmPosition implements Position {

    GROUND(0),
    BALL_PICKUP(0),
    SCORE(0),
    HOME(0);

    private final int mPosition;

    ManipulatorArmPosition(int position) {
        mPosition = position;
    }

    @Override
    public int positionToMove() {
        return mPosition;
    }

}
