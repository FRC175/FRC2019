package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
import com.team175.robot.util.tuning.ClosedLoopGains;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public final class Lift extends AldrinSubsystem implements ClosedLoopTunable {

    private final AldrinTalonSRX mFront, mRear;
    private final AldrinTalon mDrive;
    private final SimpleDoubleSolenoid mFrontBrake, mRearBrake;
    private final DigitalInput mFrontHabSensor, mRearHabSensor;
    // private final DigitalInput mFrontForwardLimit, mRearForwardLimit, mFrontReverseLimit, mRearReverseLimit;

    private int mFrontWantedPosition, mRearWantedPosition;
    private ClosedLoopGains mFrontGains, mRearGains;

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

        mFrontWantedPosition = mRearWantedPosition = 0;

        RobotProfile profile = RobotManager.getProfile();
        CTREConfiguration.config(mFront, profile.getFrontLiftConfig(), "FrontLift");
        CTREConfiguration.config(mRear, profile.getRearLiftConfig(), "RearLift");
        mFrontGains = CTREConfiguration.getGains(profile.getFrontLiftConfig(), true);
        mRearGains = CTREConfiguration.getGains(profile.getRearLiftConfig(), true);

        // resetSensors();
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

    @Deprecated
    public void setFrontPosition(LiftPosition position) {
        setFrontPower(position.getPower());
    }

    @Deprecated
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

    public double getDrivePower() {
        return mDrive.get();
    }

    public void setFrontPosition(int position) {
        mFrontWantedPosition = position;
        mLogger.debug("Setting front position to {}.", mFrontWantedPosition);
        mLogger.debug("Current front position: {}", getFrontPosition());
        mFront.set(ControlMode.MotionMagic, mFrontWantedPosition);
    }

    public void setRearPosition(int position) {
        mRearWantedPosition = position;
        mLogger.debug("Setting rear position to {}.", mRearWantedPosition);
        mLogger.debug("Current rear position: {}", getRearPosition());
        mRear.set(ControlMode.MotionMagic, mRearWantedPosition);
    }

    public int getFrontPosition() {
        return mFront.getSelectedSensorPosition();
    }

    public int getRearPosition() {
        return mRear.getSelectedSensorPosition();
    }

    public void setFrontGains(ClosedLoopGains gains) {
        mFrontGains = gains;
        CTREConfiguration.setGains(mFront, mFrontGains, true, "FrontLift");
    }

    public void setRearGains(ClosedLoopGains gains) {
        mRearGains = gains;
        CTREConfiguration.setGains(mRear, mRearGains, true, "RearLift");
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
        return !mFrontHabSensor.get();
    }

    public boolean isRearOnHab() {
        return !mRearHabSensor.get();
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

    @Override
    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();

        m.put("FLiftKp", () -> mFrontGains.getKp());
        m.put("FLiftKd", () -> mFrontGains.getKd());
        // m.put("FLiftError", this::getFrontClosedLoopError);
        m.put("FLiftPos", this::getFrontPosition);
        // m.put("FLiftVel", this::getFrontVelocity);
        m.put("FLiftPower", this::getFrontPower);
        m.put("FLiftWantedPos", () -> mFrontWantedPosition);
        m.put("FHabSensor", this::isFrontOnHab);

        m.put("RLiftKp", () -> mRearGains.getKp());
        m.put("RLiftKd", () -> mRearGains.getKd());
        // m.put("RLiftError", this::getRearClosedLoopError);
        m.put("RLiftPos", this::getRearPosition);
        // m.put("RLiftVel", this::getRearVelocity);
        m.put("RLiftPower", this::getRearPower);
        m.put("RLiftWantedPos", () -> mRearWantedPosition);
        m.put("RHabSensor", this::isRearOnHab);

        m.put("LiftKf", () -> mFrontGains.getKf());
        m.put("LiftAccel", () -> mFrontGains.getAcceleration());
        m.put("LiftCruiseVel", () -> mFrontGains.getCruiseVelocity());
        m.put("LiftDrivePower", this::getDrivePower);

        return m;
    }

    @Override
    public void updateFromDashboard() {
        setFrontGains(new ClosedLoopGains(
                SmartDashboard.getNumber("FLiftKp", 0),
                0,
                SmartDashboard.getNumber("FLiftKd", 0),
                SmartDashboard.getNumber("LiftKf", 0),
                (int) SmartDashboard.getNumber("LiftAccel", 0),
                (int) SmartDashboard.getNumber("LiftCruiseVel", 0)
        ));
        setRearGains(new ClosedLoopGains(
                SmartDashboard.getNumber("RLiftKp", 0),
                0,
                SmartDashboard.getNumber("RLiftKd", 0),
                SmartDashboard.getNumber("LiftKf", 0),
                (int) SmartDashboard.getNumber("LiftAccel", 0),
                (int) SmartDashboard.getNumber("LiftCruiseVel", 0)
        ));
        setFrontPosition((int) SmartDashboard.getNumber("FLiftWantedPos", 0));
        setRearPosition((int) SmartDashboard.getNumber("RLiftWantedPos", 0));
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
        m.put("front_lift_position", mFront::getOutputCurrent);
        m.put("rear_lift_position", mRear::getOutputCurrent);
        m.put("front_lift_wanted_position", () -> mFrontWantedPosition);
        m.put("rear_lift_wanted_position", () -> mRearWantedPosition);
        return m;
    }

}
