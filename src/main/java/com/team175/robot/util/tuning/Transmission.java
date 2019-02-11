package com.team175.robot.util.tuning;

import com.team175.robot.util.AldrinMath;

/**
 * A programmatic representation of a transmission used for CTRE motion control.
 *
 * @author Arvind
 */
public class Transmission {

    private final int mMaxMotorRPMVelocity, mMaxMotorSensorVelocity, mCountsPerRevolution;
    private final double mGearRatio, mKf;

    public Transmission(int maxMotorRPMVelocity, int countsPerRevolution, double gearRatio) {
        mMaxMotorRPMVelocity = maxMotorRPMVelocity;
        mCountsPerRevolution = countsPerRevolution;
        mGearRatio = gearRatio;
        mMaxMotorSensorVelocity = calculateTalonVelocity();
        mKf = calculateKf();
    }

    public Transmission(int maxMotorSensorVelocity, int countsPerRevolution) {
        mMaxMotorSensorVelocity = maxMotorSensorVelocity;
        mCountsPerRevolution = countsPerRevolution;
        mMaxMotorRPMVelocity = 0;
        mGearRatio = 0;
        mKf = calculateKf();
    }

    private int calculateTalonVelocity() {
        return (int) ((((double) mMaxMotorRPMVelocity) / 600.0) * (((double) mCountsPerRevolution) / mGearRatio));
    }

    private double calculateKf() {
        if (mCountsPerRevolution == 4096) { // CTRE encoder
            return 1023.0 / ((double) mMaxMotorSensorVelocity);
        } else if (mCountsPerRevolution == 512) { // Alternate encoder
            return 127.0 / ((double) mMaxMotorSensorVelocity);
        } else {
            return 0;
        }
    }

    public int getVelocity() {
        return mMaxMotorSensorVelocity;
    }

    public double getKf() {
        return mKf;
    }

}
