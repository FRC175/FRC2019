package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ManipulatorArmPosition {

    GROUND(1319),
    BALL_PICKUP(1196),
    SCORE(1107),
    STOW(883);

    private final int mPosition;

    ManipulatorArmPosition(int position) {
        mPosition = position;
    }

    public int positionToMove() {
        return mPosition;
    }

}
