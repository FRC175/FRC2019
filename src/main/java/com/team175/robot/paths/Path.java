package com.team175.robot.paths;

/**
 * An object used to represent a path. Based heavily off of Team319's Arc class.
 *
 * @author Arvind
 */
public class Path {

    // mPath = { pos, vel, dt (in ms), angle (in degrees) }
    private final double[][] mPoints;
    private final boolean mIsReversed;

    public Path(double[][] points, boolean isReversed) {
        mPoints = points;
        mIsReversed = isReversed;
    }

    public double[][] getPoints() {
        return mPoints;
    }

    public boolean isReversed() {
        return mIsReversed;
    }

}
