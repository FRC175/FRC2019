package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ManipulatorArmPosition implements Position {

    GROUND(384),
    BALL_PICKUP(327),
    SCORE(257),
    HOME(187);

    private final int mPosition;

    ManipulatorArmPosition(int position) {
        mPosition = position;
    }

    @Override
    public int positionToMove() {
        return mPosition;
    }

}
