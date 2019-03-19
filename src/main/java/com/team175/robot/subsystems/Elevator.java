package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.util.*;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.tuning.ClosedLoopGains;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public final class Elevator extends AldrinSubsystem implements ClosedLoopTunable {

    private final AldrinTalonSRX mMaster;

    private int mWantedPosition;
    private ClosedLoopGains mForwardGains, mReverseGains;

    private static final int ALLOWED_POSITION_DEVIATION = 50;

    private static Elevator sInstance;

    public static Elevator getInstance() {
        if (sInstance == null) {
            sInstance = new Elevator();
        }

        return sInstance;
    }

    private Elevator() {
        // CTREFactory.getMasterTalon(portNum : int)
        mMaster = CTREFactory.getMasterTalon(Constants.ELEVATOR_PORT);

        mWantedPosition = 0;

        RobotProfile profile = RobotManager.getProfile();
        CTREConfiguration.config(mMaster, profile.getElevatorConfig(), "Elevator");
        mForwardGains = CTREConfiguration.getGains(profile.getElevatorConfig(), true);
        mReverseGains = CTREConfiguration.getGains(profile.getElevatorConfig(), false);

        mMaster.setBrakeMode(true);
    }

    public void setPower(double power) {
        mMaster.set(ControlMode.PercentOutput, power);
    }

    public double getPower() {
        return mMaster.getMotorOutputPercent();
    }

    public double getVoltage() {
        return mMaster.getMotorOutputVoltage();
    }

    public void setPosition(int position) {
        mWantedPosition = position;

        if (mWantedPosition > getPosition()) { // Going Up
            mMaster.selectProfileSlot(Constants.PRIMARY_GAINS_SLOT, 0); // Forward Gains
        } else { // Going down
            mMaster.selectProfileSlot(Constants.AUX_GAINS_SLOT, 0); // Reverse Gains
        }

        mLogger.debug("Setting position to {}.", mWantedPosition);
        mLogger.debug("Current position: {}", getPosition());

        mMaster.set(ControlMode.MotionMagic, mWantedPosition);
    }

    public void setPosition(ElevatorPosition ep) {
        setPosition(ep.getPosition());
    }

    public int getPosition() {
        return mMaster.getSelectedSensorPosition();
    }

    public int getVelocity() {
        return mMaster.getSelectedSensorVelocity();
    }

    public void setWantedPosition(int position) {
        mWantedPosition = position;
    }

    public boolean isAtWantedPosition() {
        return Math.abs(getPosition() - mWantedPosition) <= ALLOWED_POSITION_DEVIATION;
    }

    public boolean isForwardLimitHit() {
        return mMaster.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public boolean isReverseLimitHit() {
        return mMaster.getSensorCollection().isRevLimitSwitchClosed();
    }

    public void setForwardGains(ClosedLoopGains gains) {
        mForwardGains = gains;
        CTREConfiguration.setGains(mMaster, mForwardGains, true, "Elevator");
    }

    public void setReverseGains(ClosedLoopGains gains) {
        mReverseGains = gains;
        CTREConfiguration.setGains(mMaster, mReverseGains, false, "Elevator");
    }

    @Override
    public void stop() {
        setPosition(mWantedPosition);
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();

        m.put("ElevatorFwdKp", () -> mForwardGains.getKp());
        m.put("ElevatorFwdKd", () -> mForwardGains.getKd());

        m.put("ElevatorRevKp", () -> mReverseGains.getKp());
        m.put("ElevatorRevKd", () -> mReverseGains.getKd());

        m.put("ElevatorKf", () -> mForwardGains.getKf());
        m.put("ElevatorAccel", () -> mForwardGains.getAcceleration());
        m.put("ElevatorCruiseVel", () -> mForwardGains.getCruiseVelocity());
        m.put("ElevatorWantedPos", () -> mWantedPosition);
        m.put("ElevatorPos", this::getPosition);
        m.put("ElevatorVel", this::getVelocity);
        m.put("ElevatorPower", this::getPower);
        m.put("ElevatorVolt", this::getVoltage);
        m.put("ElevatorCurrent", mMaster::getOutputCurrent);
        m.put("ElevatorFwdLimit", this::isForwardLimitHit);
        m.put("ElevatorRevLimit", this::isReverseLimitHit);

        return m;
    }

    @Override
    public void updateFromDashboard() {
        setForwardGains(new ClosedLoopGains(
                SmartDashboard.getNumber("ElevatorFwdKp", 0),
                0,
                SmartDashboard.getNumber("ElevatorFwdKd", 0),
                SmartDashboard.getNumber("ElevatorKf", 0),
                (int) SmartDashboard.getNumber("ElevatorAccel", 0),
                (int) SmartDashboard.getNumber("ElevatorCruiseVel", 0)
        ));
        setReverseGains(new ClosedLoopGains(
                SmartDashboard.getNumber("ElevatorRevKp", 0),
                0,
                SmartDashboard.getNumber("ElevatorRevKd", 0),
                SmartDashboard.getNumber("ElevatorKf", 0),
                (int) SmartDashboard.getNumber("ElevatorAccel", 0),
                (int) SmartDashboard.getNumber("ElevatorCruiseVel", 0)
        ));
        setPosition((int) SmartDashboard.getNumber("ElevatorWantedPos", 0));
    }

    @Override
    public boolean checkSubsystem() {
        CTREDiagnostics diag = new CTREDiagnostics(mMaster, "Elevator");

        mLogger.info("Beginning diagnostics test for Elevator subsystem.");
        boolean isGood = diag.checkMotorController();
        mLogger.info(diag.toString());

        if (!isGood) {
            mLogger.error("Elevator subsystem failed diagnostics test!");
        } else {
            mLogger.info("Elevator subsystem passed diagnostics test!");
        }

        return isGood;
    }

    @Override
    public void updateGains() {
        updateFromDashboard();
    }

    @Override
    public void resetSensors() {
        CTREDiagnostics.checkCommand(mMaster.setSelectedSensorPosition(0), "Failed to zero Elevator encoder!");
    }

    @Override
    public Map<String, Supplier> getCSVTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("elevator_position", this::getPosition);
        m.put("elevator_wanted_position", () -> mWantedPosition);
        m.put("elevator_velocity", this::getVelocity);
        m.put("elevator_voltage", this::getVoltage);
        m.put("elevator_current", mMaster::getOutputCurrent);
        return m;
    }

}
