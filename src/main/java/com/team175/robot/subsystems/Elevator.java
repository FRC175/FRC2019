package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.team175.robot.Constants;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.positions.LEDColor;
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
    private final LED mLED;

    private int mWantedPosition;
    private ClosedLoopGains mForwardGains, mReverseGains;
    private ElevatorState mWantedState;

    private static final int ALLOWED_POSITION_DEVIATION = 100;

    private static Elevator sInstance;

    private enum ElevatorState {
        POSITION,
        MANUAL;
    }

    public static Elevator getInstance() {
        if (sInstance == null) {
            sInstance = new Elevator();
        }

        return sInstance;
    }

    private Elevator() {
        // CTREFactory.getMasterTalon(portNum : int)
        mMaster = CTREFactory.getMasterTalon(Constants.ELEVATOR_PORT);

        mLED = LED.getInstance();

        mWantedPosition = 0;
        mWantedState = ElevatorState.MANUAL;

        RobotProfile profile = RobotManager.getProfile();
        CTREConfiguration.config(mMaster, profile.getElevatorConfig(), "Elevator");
        mForwardGains = CTREConfiguration.getGains(profile.getElevatorConfig(), true);
        mReverseGains = CTREConfiguration.getGains(profile.getElevatorConfig(), false);
        CTREDiagnostics.checkCommand(mMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyClosed, Constants.TIMEOUT_MS),
                "Failed to config elevator forward limit switch!");
        CTREDiagnostics.checkCommand(mMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyClosed, Constants.TIMEOUT_MS),
                "Failed to config elevator reverse limit switch!");
        /*CTREDiagnostics.checkCommand(mMaster.configClearPositionOnLimitF(true, Constants.TIMEOUT_MS),
                "Failed to config elevator zero on reverse limit switch!");*/

        mMaster.setBrakeMode(true);
        // resetSensors();

        super.logInstantiation();
    }

    public void setPower(double power) {
        mWantedState = ElevatorState.MANUAL;
        mMaster.set(ControlMode.PercentOutput, power);
    }

    public double getPower() {
        return mMaster.getMotorOutputPercent();
    }

    public double getVoltage() {
        return mMaster.getMotorOutputVoltage();
    }

    public void setPosition(int position) {
        mWantedState = ElevatorState.POSITION;
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

    public void setToWantedPosition() {
        setPosition(mWantedPosition);
    }

    @Deprecated
    public void setWantedPosition(int position) {
        mWantedPosition = position;
    }

    public boolean isAtWantedPosition() {
        return Math.abs(getPosition() - mWantedPosition) <= ALLOWED_POSITION_DEVIATION;
    }

    public boolean isTopLimitHit() {
        return !mMaster.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public boolean isBottomLimitHit() {
        return !mMaster.getSensorCollection().isRevLimitSwitchClosed();
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
    public void start() {
    }

    @Override
    public void loop() {
        switch (mWantedState) {
            case MANUAL:
                mWantedPosition = getPosition();
                break;
            case POSITION:
            default:
                break;
        }

        if (isTopLimitHit()) {
            // mLED.setStaticColor(LEDColor.LIMIT_HIT);
        }

        if (isBottomLimitHit()) {
            resetSensors();
            // mLED.blinkColor(LEDColor.LIMIT_HIT);
        }

        outputToDashboard();
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
        m.put("ElevatorFwdLimit", this::isTopLimitHit);
        m.put("ElevatorRevLimit", this::isBottomLimitHit);

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
        CTREDiagnostics.checkCommand(mMaster.setPrimarySensorPosition(0), "Failed to zero Elevator encoder!");
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
