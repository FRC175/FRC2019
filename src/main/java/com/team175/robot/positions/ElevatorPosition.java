package com.team175.robot.positions;

public enum ElevatorPosition implements Position {

    CARGO_LEVEL_THREE(0),
    HATCH_LEVEL_THREE(0),
    CARGO_LEVEL_TWO(0),
    HATCH_LEVEL_TWO(0),
    CARGO_LEVEL_ONE(0),
    HATCH_LEVEL_ONE(0),
    PICKUP(0);

    private final int mPosition;

    ElevatorPosition(int position) {
        mPosition = position;
    }

    @Override
    public int positionToMove() {
        return mPosition;
    }

}
