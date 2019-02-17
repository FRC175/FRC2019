package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;

import com.team175.robot.positions.LiftPosition;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.CTREFactory;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Arvind
 */
public final class Lift extends AldrinSubsystem {

    /* Declarations */
    private final AldrinTalonSRX mDrive;
    private final Talon mFront, mRear;
    private final Solenoid mFrontBrake, mRearBrake;
    private final DigitalInput mFrontLimit, mRearLimit, mFrontHabSensor, mRearHabSensor;

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
        mFront = new Talon(Constants.LIFT_FRONT_PORT);
        mRear = new Talon(Constants.LIFT_REAR_PORT);

        // Solenoid(channel : int)
        mFrontBrake = new Solenoid(Constants.LIFT_FRONT_BRAKE_CHANNEL);
        mRearBrake = new Solenoid(Constants.LIFT_REAR_BRAKE_CHANNEL);

        // DigitalInput(portNum : int)
        mFrontLimit = new DigitalInput(Constants.LIFT_FRONT_LIMIT_PORT);
        mRearLimit = new DigitalInput(Constants.LIFT_REAR_LIMIT_PORT);
        mFrontHabSensor = new DigitalInput(Constants.LIFT_FRONT_HAB_SENSOR_PORT);
        mRearHabSensor = new DigitalInput(Constants.LIFT_REAR_HAB_SENSOR_PORT);
    }

    /*public void setPower(double power) {
        mFront.set(power);
        mRear.set(power);
    }*/

    public void setFrontPower(double power) {
        if (!isFrontLimitHit()) {
            mFront.set(power);
        }
    }

    public void setRearPower(double power) {
        if (!isRearLimitHit()) {
            mRear.set(power);
        }
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

    public boolean isFrontLimitHit() {
        return mFrontLimit.get();
    }

    public boolean isRearLimitHit() {
        return mRearLimit.get();
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

    public Map<String, Object> getTelemetry() {
        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("FLiftPower", getFrontPower());
        m.put("RLiftPower", getRearPower());
        m.put("LiftDrivePower", getDrivePower());
        return m;
    }

    @Override
    public void updateFromDashboard() {
    }

}
