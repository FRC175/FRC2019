package com.team175.robot.util.tuning;

/**
 * @author Arvind
 */
public class ClosedLoopGains {

    private final double mKp, mKi, mKd, mKf;
    private final int mAcceleration, mCruiseVelocity;

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
