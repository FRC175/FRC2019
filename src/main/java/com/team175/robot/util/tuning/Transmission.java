package com.team175.robot.util.tuning;

import com.team175.robot.util.AldrinMath;

/**
 * @author Arvind
 */
public class Transmission {

    private final int mMaxMotorRPMVelocity;
    private final int mMaxMotorSensorVelocity;
    private final int mCountsPerRevolution;
    private final double mGearRatio;
    private final double mKf;

    public Transmission(int maxMotorRPMVelocity, int countsPerRevolution, double gearRatio) {
        mMaxMotorRPMVelocity = maxMotorRPMVelocity;
        mCountsPerRevolution = countsPerRevolution;
        mGearRatio = gearRatio;
        mMaxMotorSensorVelocity = AldrinMath.calculateEmpiricalVelocity(mMaxMotorRPMVelocity, mCountsPerRevolution,
                mGearRatio);
        mKf = AldrinMath.calculateKf(mMaxMotorSensorVelocity);
    }

    public Transmission(int maxMotorSensorVelocity) {
        mMaxMotorRPMVelocity = 0;
        mCountsPerRevolution = 0;
        mGearRatio = 0;
        mMaxMotorSensorVelocity = maxMotorSensorVelocity;
        mKf = AldrinMath.calculateKf(mMaxMotorSensorVelocity);
    }

    public int getVelocity() {
        return mMaxMotorSensorVelocity;
    }

    public double getKf() {
        return mKf;
    }

}
