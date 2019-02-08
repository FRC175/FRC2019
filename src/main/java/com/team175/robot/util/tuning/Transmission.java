package com.team175.robot.util.tuning;

import com.team175.robot.util.AldrinMath;

/**
 * A programmatic representation of a transmission used for CTRE Motion Magic Control.
 * Deprecated due to general uselessness.
 *
 * @author Arvind
 */
@Deprecated
public class Transmission {

    private final int mMaxMotorRPMVelocity, mMaxMotorSensorVelocity, mCountsPerRevolution;
    private final double mGearRatio, mKf;

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
