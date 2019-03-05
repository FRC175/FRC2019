package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;

import com.team175.robot.positions.LiftPosition;
import com.team175.robot.util.drivers.AldrinTalon;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;
import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public final class Lift extends AldrinSubsystem {

    /* Declarations */
    private final AldrinTalonSRX mDrive;
    private final AldrinTalon mFront, mRear;
    private final SimpleDoubleSolenoid mFrontBrake, mRearBrake;
    private final DigitalInput mFrontForwardLimit, mRearForwardLimit, mFrontReverseLimit, mRearReverseLimit,
            mFrontHabSensor, mRearHabSensor;

    // Singleton Instance
    private static Lift sInstance;

    public static Lift getInstance() {
        if (sInstance == null) {
            sInstance = new Lift();
        }

        return sInstance;
    }

    private Lift() {
        /* Instantiations */
        // CTREFactory.getTalon(portNum : int)
        mDrive = CTREFactory.getTalon(Constants.LIFT_DRIVE_PORT);

        // Talon(portNum : int)
        mFront = new AldrinTalon(Constants.LIFT_FRONT_PORT);
        mRear = new AldrinTalon(Constants.LIFT_REAR_PORT);

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int, isOnPCMTwo : boolean)
        mFrontBrake = new SimpleDoubleSolenoid(Constants.LIFT_FRONT_BRAKE_FORWARD_CHANNEL, Constants.LIFT_FRONT_BRAKE_REVERSE_CHANNEL,
                true);
        mRearBrake = new SimpleDoubleSolenoid(Constants.LIFT_REAR_BRAKE_FORWARD_CHANNEL, Constants.LIFT_REAR_BRAKE_REVERSE_CHANNEL,
                true);

        // DigitalInput(portNum : int)
        mFrontForwardLimit = new DigitalInput(Constants.LIFT_FRONT_FORWARD_LIMIT_PORT);
        mRearForwardLimit = new DigitalInput(Constants.LIFT_REAR_FORWARD_LIMIT_PORT);
        mFrontReverseLimit = new DigitalInput(Constants.LIFT_FRONT_REVERSE_LIMIT_PORT);
        mRearReverseLimit = new DigitalInput(Constants.LIFT_REAR_REVERSE_LIMIT_PORT);
        mFrontHabSensor = new DigitalInput(Constants.LIFT_FRONT_HAB_SENSOR_PORT);
        mRearHabSensor = new DigitalInput(Constants.LIFT_REAR_HAB_SENSOR_PORT);

        /* Configuration */
        stop();
    }

    /*public void setPower(double power) {
        mFront.set(power);
        mRear.set(power);
    }*/

    public void setFrontPower(double power) {
        // if (!isFrontForwardLimitHit() || !isFrontReverseLimitHit()) {
            // mFrontBrake.set(false);
            mFront.set(power);
        // }
    }

    public void setRearPower(double power) {
        // if (!isRearForwardLimitHit() || !isRearReverseLimitHit()) {
            // mRearBrake.set(false);
            mRear.set(power);
        // }
    }

    public void setFrontPosition(LiftPosition lp) {
        setFrontPower(lp.getPower());
    }

    public void setRearPosition(LiftPosition lp) {
        setRearPower(lp.getPower());
    }

    public void setDrivePower(double power) {
        mDrive.set(ControlMode.PercentOutput, power);
    }

    public double getFrontPower() {
        return mFront.get();
    }

    public double getRearPower() {
        return mRear.get();
    }

    public double getDrivePower() {
        return mDrive.getMotorOutputPercent();
    }

    public void setFrontBrake(boolean enable) {
        mFrontBrake.set(enable);
    }

    public void setRearBrake(boolean enable) {
        mRearBrake.set(enable);
    }

    public boolean isFrontForwardLimitHit() {
        return mFrontForwardLimit.get();
    }

    public boolean isRearForwardLimitHit() {
        return mRearForwardLimit.get();
    }

    public boolean isFrontReverseLimitHit() {
        return mFrontReverseLimit.get();
    }

    public boolean isRearReverseLimitHit() {
        return mRearReverseLimit.get();
    }

    public boolean isFrontOnHab() {
        return mFrontHabSensor.get();
    }

    public boolean isRearOnHab() {
        return mRearHabSensor.get();
    }

    @Override
    public void stop() {
        setFrontPower(0);
        setRearPower(0);
        setDrivePower(0);
        setFrontBrake(true);
        setRearBrake(true);
    }

    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("FLiftPower", this::getFrontPower);
        m.put("RLiftPower", this::getRearPower);
        m.put("LiftDrivePower", this::getDrivePower);
        return m;
    }

    @Override
    public void updateFromDashboard() {
    }

}
