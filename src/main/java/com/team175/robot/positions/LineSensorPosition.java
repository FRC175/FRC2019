package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum LineSensorPosition {

    // Political Spectrum
    EXTREME_LEFT(-100),
    LEFTIST(-75),
    LEFT(-50),
    CENTER_LEFT(-25),
    CENTER(0),
    CENTER_RIGHT(25),
    RIGHT(50),
    RIGHTIST(75),
    EXTREME_RIGHT(100),
    ERROR(0);

    private final int mPosition;

    LineSensorPosition(int position) {
        mPosition = position;
    }

    public int positionToMove() {
        return mPosition;
    }

}