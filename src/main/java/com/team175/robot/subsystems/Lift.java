package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team175.robot.Constants;

import com.team175.robot.positions.LiftPosition;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.util.CTREConfiguration;
import com.team175.robot.util.CTREDiagnostics;
import com.team175.robot.util.CTREFactory;
import com.team175.robot.util.RobotManager;
import com.team175.robot.util.drivers.AldrinTalon;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import com.team175.robot.util.tuning.CSVWritable;
import com.team175.robot.util.tuning.ClosedLoopGains;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * TODO: Determine PDP Channel
 *
 * @author Arvind
 */
public final class Lift extends AldrinSubsystem implements ClosedLoopTunable {

    private final AldrinTalonSRX mFront, mRear;
    private final AldrinTalon mDrive;
    private final SimpleDoubleSolenoid mFrontBrake, mRearBrake;
    private final DigitalInput mFrontHabSensor, mRearHabSensor;
    // private final DigitalInput mFrontForwardLimit, mRearForwardLimit, mFrontReverseLimit, mRearReverseLimit;

    private static Lift sInstance;

    public static Lift getInstance() {
        if (sInstance == null) {
            sInstance = new Lift();
        }

        return sInstance;
    }

    private Lift() {
        mFront = CTREFactory.getMasterTalon(Constants.FRONT_LIFT_PORT);
        mRear = CTREFactory.getMasterTalon(Constants.REAR_LIFT_PORT);

        // Talon(portNum : int)
        mDrive = new AldrinTalon(Constants.LIFT_DRIVE_PORT);

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int, isOnPCMTwo : boolean)
        mFrontBrake = new SimpleDoubleSolenoid(Constants.LIFT_FRONT_BRAKE_FORWARD_CHANNEL, Constants.LIFT_FRONT_BRAKE_REVERSE_CHANNEL,
                true);
        mRearBrake = new SimpleDoubleSolenoid(Constants.LIFT_REAR_BRAKE_FORWARD_CHANNEL, Constants.LIFT_REAR_BRAKE_REVERSE_CHANNEL,
                true);

        // DigitalInput(portNum : int)
        /*mFrontForwardLimit = new DigitalInput(Constants.LIFT_FRONT_FORWARD_LIMIT_PORT);
        mRearForwardLimit = new DigitalInput(Constants.LIFT_REAR_FORWARD_LIMIT_PORT);
        mFrontReverseLimit = new DigitalInput(Constants.LIFT_FRONT_REVERSE_LIMIT_PORT);
        mRearReverseLimit = new DigitalInput(Constants.LIFT_REAR_REVERSE_LIMIT_PORT);*/
        mFrontHabSensor = new DigitalInput(Constants.LIFT_FRONT_HAB_SENSOR_PORT);
        mRearHabSensor = new DigitalInput(Constants.LIFT_REAR_HAB_SENSOR_PORT);

        RobotProfile profile = RobotManager.getProfile();
        CTREConfiguration.config(mFront, profile.getFrontLiftConfig(), "FrontLift");
        CTREConfiguration.config(mRear, profile.getRearLiftConfig(), "RearLift");

        stop();

        super.logInstantiation();
    }

    public void setFrontPower(double power) {
        /*if (!isFrontForwardLimitHit() || !isFrontReverseLimitHit()) {
            mFrontBrake.set(false);
        }*/
        mFront.set(ControlMode.PercentOutput, power);
    }

    public void setRearPower(double power) {
        /*if (!isRearForwardLimitHit() || !isRearReverseLimitHit()) {
            mRearBrake.set(false);
        }*/
        mRear.set(ControlMode.PercentOutput, power);
    }

    public void setPower(double power) {
        setFrontPower(power);
        setRearPower(power);
    }

    public void setFrontPosition(LiftPosition position) {
        setFrontPower(position.getPower());
    }

    public void setRearPosition(LiftPosition position) {
        setRearPower(position.getPower());
    }

    public void setDrivePower(double power) {
        mDrive.set(power);
    }

    public double getFrontPower() {
        return mFront.getMotorOutputPercent();
    }

    public double getRearPower() {
        return mRear.getMotorOutputPercent();
    }

    public int getFrontPosition() {
        return mFront.getSelectedSensorPosition();
    }

    public int getRearPosition() {
        return mRear.getSelectedSensorPosition();
    }

    public double getDrivePower() {
        return mDrive.get();
    }

    public void setFrontBrake(boolean enable) {
        mFrontBrake.set(enable);
    }

    public void setRearBrake(boolean enable) {
        mRearBrake.set(enable);
    }

    public boolean isFrontForwardLimitHit() {
        // return mFrontForwardLimit.get();
        return false;
    }

    public boolean isRearForwardLimitHit() {
        // return mRearForwardLimit.get();
        return false;
    }

    public boolean isFrontReverseLimitHit() {
        // return mFrontReverseLimit.get();
        return false;
    }

    public boolean isRearReverseLimitHit() {
        // return mRearReverseLimit.get();
        return false;
    }

    public boolean isFrontOnHab() {
        return mFrontHabSensor.get();
    }

    public boolean isRearOnHab() {
        return mRearHabSensor.get();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        setPower(0);
        setDrivePower(0);
        setFrontBrake(true);
        setRearBrake(true);
    }

    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("FLiftPower", this::getFrontPower);
        m.put("RLiftPower", this::getRearPower);
        m.put("LiftDrivePower", this::getDrivePower);
        m.put("FHabSensor", this::isFrontOnHab);
        m.put("RHabSensor", this::isRearOnHab);
        m.put("FLiftPos", this::getFrontPosition);
        m.put("RLiftPos", this::getRearPosition);
        return m;
    }

    @Override
    public void updateGains() {
        updateFromDashboard();
    }

    @Override
    public void resetSensors() {
        CTREDiagnostics.checkCommand(mFront.setPrimarySensorPosition(0), "Failed to zero FrontLift encoder!");
        CTREDiagnostics.checkCommand(mRear.setPrimarySensorPosition(0), "Failed to zero RearLift encoder!");
    }

    @Override
    public Map<String, Supplier> getCSVTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("front_lift_current", mFront::getOutputCurrent);
        m.put("rear_lift_current", mRear::getOutputCurrent);
        return m;
    }

}
