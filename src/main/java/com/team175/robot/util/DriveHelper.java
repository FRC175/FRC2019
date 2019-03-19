package com.team175.robot.util;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * A helper class to implement multiple different drive types including Cheesy Drive and Arcade Drive to run on the
 * Talon SRX.
 *
 * @author Arvind
 */
public final class DriveHelper {

    /**
     * The Talon SRXs
     */
    private TalonSRX mLeft, mRight;

    /**
     * The Cheesy Drive variables
     */
    // These factors determine how fast the wheel traverses the "non linear" sine curve.
    private static final double kHighWheelNonLinearity = 0.65;
    private static final double kLowWheelNonLinearity = 0.5;

    private static final double kHighNegInertiaScalar = 4.0;

    private static final double kLowNegInertiaThreshold = 0.65;
    private static final double kLowNegInertiaTurnScalar = 3.5;
    private static final double kLowNegInertiaCloseScalar = 4.0;
    private static final double kLowNegInertiaFarScalar = 5.0;

    private static final double kHighSensitivity = 0.65;
    private static final double kLowSensitivity = 0.65;

    private static final double kQuickStopDeadband = 0.5;
    private static final double kQuickStopWeight = 0.1;
    private static final double kQuickStopScalar = 5.0;

    private double mOldWheel = 0.0;
    private double mQuickStopAccumulator = 0.0;
    private double mNegInertiaAccumulator = 0.0;

    /**
     * Constructs a new DriveHelper.
     *
     * @param left
     *         The master Talon SRX for the left drive motors
     * @param right
     *         The master Talon SRX for the right drive motors
     */
    public DriveHelper(TalonSRX left, TalonSRX right) {
        mLeft = left;
        mRight = right;
    }

    /**
     * "Cheesy Drive" simply means that the "turning" stick controls the curvature of the robot's path rather than its
     * rate of heading change. This helps make the robot more controllable at high speeds. It also handles the robot's
     * quick turn functionality - "quick turn" overrides constant-curvature turning for turn-in-place maneuvers.
     *
     * @author Team254
     */
    public void cheesyDrive(double throttle, double turn, boolean isQuickTurn, boolean isHighGear) {
        double negInertia = turn - mOldWheel;
        mOldWheel = turn;

        double wheelNonLinearity;
        if (isHighGear) {
            wheelNonLinearity = kHighWheelNonLinearity;
            final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            // Apply a sin function that's scaled to make it feel better.
            turn = Math.sin(Math.PI / 2.0 * wheelNonLinearity * turn) / denominator;
            turn = Math.sin(Math.PI / 2.0 * wheelNonLinearity * turn) / denominator;
        } else {
            wheelNonLinearity = kLowWheelNonLinearity;
            final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            // Apply a sin function that's scaled to make it feel better.
            turn = Math.sin(Math.PI / 2.0 * wheelNonLinearity * turn) / denominator;
            turn = Math.sin(Math.PI / 2.0 * wheelNonLinearity * turn) / denominator;
            turn = Math.sin(Math.PI / 2.0 * wheelNonLinearity * turn) / denominator;
        }

        double leftPwm, rightPwm, overPower;
        double sensitivity;

        double angularPower;
        double linearPower;

        // Negative inertia!
        double negInertiaScalar;
        if (isHighGear) {
            negInertiaScalar = kHighNegInertiaScalar;
            sensitivity = kHighSensitivity;
        } else {
            if (turn * negInertia > 0) {
                // If we are moving away from 0.0, aka, trying to get more turn.
                negInertiaScalar = kLowNegInertiaTurnScalar;
            } else {
                // Otherwise, we are attempting to go back to 0.0.
                if (Math.abs(turn) > kLowNegInertiaThreshold) {
                    negInertiaScalar = kLowNegInertiaFarScalar;
                } else {
                    negInertiaScalar = kLowNegInertiaCloseScalar;
                }
            }
            sensitivity = kLowSensitivity;
        }
        double negInertiaPower = negInertia * negInertiaScalar;
        mNegInertiaAccumulator += negInertiaPower;

        turn = turn + mNegInertiaAccumulator;
        if (mNegInertiaAccumulator > 1) {
            mNegInertiaAccumulator -= 1;
        } else if (mNegInertiaAccumulator < -1) {
            mNegInertiaAccumulator += 1;
        } else {
            mNegInertiaAccumulator = 0;
        }
        linearPower = throttle;

        // Quickturn!
        if (isQuickTurn) {
            if (Math.abs(linearPower) < kQuickStopDeadband) {
                double alpha = kQuickStopWeight;
                mQuickStopAccumulator = (1 - alpha) * mQuickStopAccumulator
                        + alpha * limit(turn, 1.0) * kQuickStopScalar;
            }
            overPower = 1.0;
            angularPower = turn;
        } else {
            overPower = 0.0;
            angularPower = Math.abs(throttle) * turn * sensitivity - mQuickStopAccumulator;
            if (mQuickStopAccumulator > 1) {
                mQuickStopAccumulator -= 1;
            } else if (mQuickStopAccumulator < -1) {
                mQuickStopAccumulator += 1;
            } else {
                mQuickStopAccumulator = 0.0;
            }
        }

        rightPwm = leftPwm = linearPower;
        leftPwm += angularPower;
        rightPwm -= angularPower;

        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }

        mLeft.set(ControlMode.PercentOutput, leftPwm);
        mRight.set(ControlMode.PercentOutput, rightPwm);
    }

    /**
     * The arcade drive mode from the Talon SRX's example code.
     */
    public void arcadeDrive(double throttle, double turn) {
        mLeft.set(ControlMode.PercentOutput, throttle, DemandType.ArbitraryFeedForward, +turn);
        mRight.set(ControlMode.PercentOutput, throttle, DemandType.ArbitraryFeedForward, -turn);
    }

    /**
     * An alternative way of performing arcade drive.
     */
    public void altAcradeDrive(double throttle, double turn) {
        mLeft.set(ControlMode.PercentOutput, limit(throttle + turn, 1));
        mRight.set(ControlMode.PercentOutput, limit(throttle - turn, 1));
    }

    /**
     * The arcade drive mode from the Differential Drive helper.
     *
     * @author WPILib
     */
    public void wpiArcadeDrive(double throttle, double turn) {
        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        throttle = Math.copySign(throttle * throttle, throttle);
        turn = Math.copySign(turn * turn, turn);

        double leftMotorOutput;
        double rightMotorOutput;
        double maxInput = Math.copySign(Math.max(Math.abs(throttle), Math.abs(turn)), throttle);

        if (throttle >= 0.0) {
            // First quadrant, else second quadrant
            if (turn >= 0.0) {
                leftMotorOutput = maxInput;
                rightMotorOutput = throttle - turn;
            } else {
                leftMotorOutput = throttle + turn;
                rightMotorOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (turn >= 0.0) {
                leftMotorOutput = throttle + turn;
                rightMotorOutput = maxInput;
            } else {
                leftMotorOutput = maxInput;
                rightMotorOutput = throttle - turn;
            }
        }

        mLeft.set(ControlMode.PercentOutput, limit(leftMotorOutput, 1));
        mRight.set(ControlMode.PercentOutput, limit(rightMotorOutput, 1));
    }

    /**
     * Forces robot to drive straight by using gyro. Must be using Pigeon gyro with tuned auxiliary PID.
     */
    public void straightDrive(double throttle) {
        mRight.set(ControlMode.PercentOutput, throttle, DemandType.AuxPID, 0); // 0 degrees => straight
        mLeft.follow(mRight, FollowerType.AuxOutput1);
    }

    /**
     * From Team254's Util class.
     */
    public double limit(double v, double maxMagnitude) {
        return Math.min(maxMagnitude, Math.max(-maxMagnitude, v));
    }

}
