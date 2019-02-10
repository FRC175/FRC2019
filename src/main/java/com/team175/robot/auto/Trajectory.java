package com.team175.robot.auto;

public class Trajectory {

    // mPath = { pos, vel, dt (in ms), angle (in degrees) }
    private final double[][] mPath;
    private final boolean mIsReversed;

    public Trajectory(double[][] points, boolean isReversed) {
        mPath = points;
        mIsReversed = isReversed;
    }

    public double[][] getPath() {
        return mPath;
    }

    public boolean isReversed() {
        return mIsReversed;
    }

}
