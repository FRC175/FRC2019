package com.team175.robot.positions;

public enum ManipulatorArmPosition implements Position {

    PICKUP(0),
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
