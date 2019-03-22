package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum ElevatorPosition {

    CARGO_LEVEL_THREE(24937),
    HATCH_LEVEL_THREE(21698),
    CARGO_LEVEL_TWO(14520),
    HATCH_LEVEL_TWO(16344),
    CARGO_LOADING(10000),
    CARGO_LEVEL_ONE(4007),
    GROUND_PICKUP_ABOVE(1211),
    HATCH_LEVEL_ONE(6032),
    GROUND_PICKUP(415);
    /*CARGO_LEVEL_THREE(23738),
    HATCH_LEVEL_THREE(21495),
    CARGO_LEVEL_TWO(14520),
    HATCH_LEVEL_TWO(11647),
    CARGO_LEVEL_ONE(4007),
    CARGO_LOADING(10000),
    GROUND_PICKUP_ABOVE(1918),
    HATCH_LEVEL_ONE(281),
    GROUND_PICKUP(415);*/

    private final int mPosition;

    ElevatorPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

}
