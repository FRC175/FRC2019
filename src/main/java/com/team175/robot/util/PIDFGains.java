package com.team175.robot.util;

public class PIDFGains {

    private final double mKp;
    private final double mKi;
    private final double mKd;
    private final double mKf;

    public PIDFGains(double kP, double kI, double kD, double kF) {
        mKp = kP;
        mKi = kI;
        mKd = kD;
        mKf = kF;
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

}
