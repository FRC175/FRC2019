package com.team175.robot.util;

/**
 * @author Arvind
 */
public final class ClosedLoopGains {

    private final double mKp;
    private final double mKi;
    private final double mKd;
    private final double mKf;
    private final int mAcceleration;
    private final int mCruiseVelocity;

    public ClosedLoopGains(double kP, double kI, double kD, double kF, int acceleration, int cruiseVelocity) {
        mKp = kP;
        mKi = kI;
        mKd = kD;
        mKf = kF;
        mAcceleration = acceleration;
        mCruiseVelocity = cruiseVelocity;
    }

    public double getKp() {
        return mKp;
    }

    public double getKi() {
        return mKi;
    }

    public double getKd() {
        return mKd;
    }

    public double getKf() {
        return mKf;
    }

    public int getAcceleration() {
        return mAcceleration;
    }

    public int getCruiseVelocity() {
        return mCruiseVelocity;
    }

}
