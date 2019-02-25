package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ManipulatorArmPosition {

    GROUND(384),
    BALL_PICKUP(327),
    SCORE(257),
    STOW(187);

    private final int mPosition;

    ManipulatorArmPosition(int position) {
        mPosition = position;
    }

    public int positionToMove() {
        return mPosition;
    }

}
