package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ManipulatorArmPosition {

    /*HATCH_PICKUP(294),
    BALL_PICKUP(186),
    SCORE(75),
    STOW(-190);*/
    HATCH_PICKUP(384),
    BALL_PICKUP(338),
    SCORE(254),
    STOW(187);

    private final int mPosition;

    ManipulatorArmPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

}
