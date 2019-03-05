package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ElevatorPosition {

    CARGO_LEVEL_THREE(24937),
    HATCH_LEVEL_THREE(21698),
    CARGO_LEVEL_TWO(14520),
    HATCH_LEVEL_TWO(11180),
    CARGO_LEVEL_ONE(4007),
    HATCH_LEVEL_ONE(348);

    private final int mPosition;

    ElevatorPosition(int position) {
        mPosition = position;
    }

    public int positionToMove() {
        return mPosition;
    }

}
