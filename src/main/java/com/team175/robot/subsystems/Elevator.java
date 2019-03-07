package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team175.robot.Constants;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.util.*;
import com.team175.robot.util.choosers.RobotChooser;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public final class Elevator extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private final AldrinTalonSRX mMaster;

    private int mWantedPosition;
    // private ClosedLoopGains mGains;
    private ClosedLoopGains mForwardGains, mReverseGains;

    // Singleton Instance
    private static Elevator sInstance;

    private static final int ALLOWED_POSITION_DEVIATION = 100;

    public static Elevator getInstance() {
        if (sInstance == null) {
            sInstance = new Elevator();
        }

        return sInstance;
    }

    private Elevator() {
        /* Instantiations */
        // CTREFactory.getMasterTalon(portNum : int)
        mMaster = CTREFactory.getMasterTalon(Constants.ELEVATOR_PORT);

        mWantedPosition = 0;

        /* Configuration */
        RobotProfile profile = RobotChooser.getInstance().getProfile();
        CTREConfiguration.config(mMaster, profile.getElevatorConfig(), "Elevator");
        mForwardGains = CTREConfiguration.getGainsFromConfig(profile.getElevatorConfig(), true);
        mReverseGains = CTREConfiguration.getGainsFromConfig(profile.getElevatorConfig(), false);

        /*mGains = Constants.ELEVATOR_GAINS;
        CTREDiagnostics.checkCommand(mMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative),
                "Failed to config Elevator encoder!");
        setGains(mGains);
        mMaster.setInverted(true);
        mMaster.setSensorPhase(true);*/

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
            setGains(mForwardGains);
            // mMaster.selectProfileSlot(Constants.PRIMARY_GAINS_SLOT, 0);
        } else { // Going down
            setGains(mReverseGains);
            // mMaster.selectProfileSlot(Constants.AUX_GAINS_SLOT, 0);
        }

        mMaster.set(ControlMode.MotionMagic, mWantedPosition);
    }

    public void setPosition(ElevatorPosition ep) {
        setPosition(ep.positionToMove());
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
        CTREConfiguration.setPrimaryGains(mMaster, mForwardGains, "Elevator");
    }

    public void setReverseGains(ClosedLoopGains gains) {
        mReverseGains = gains;
        CTREConfiguration.setAuxGains(mMaster, mReverseGains, "Elevator");
    }

    public void setGains(ClosedLoopGains gains) {
        CTREDiagnostics.checkCommand(mMaster.configPIDF(gains.getKp(), gains.getKi(), gains.getKd(), gains.getKf()),
                "Failed to config Elevator PID gains!");
        CTREDiagnostics.checkCommand(mMaster.configMotionAcceleration(gains.getAcceleration()),
                "Failed to config Elevator acceleration!");
        CTREDiagnostics.checkCommand(mMaster.configMotionCruiseVelocity(gains.getCruiseVelocity()),
                "Failed to config Elevator cruise velocity!");
    }

    @Override
    public void stop() {
        setPosition(mWantedPosition);
        // setPower(0);
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();

        m.put("ElevatorForKp", () -> mForwardGains.getKp());
        m.put("ElevatorForKd", () -> mForwardGains.getKd());
        m.put("ElevatorForKf", () -> mForwardGains.getKf());
        m.put("ElevatorForAccel", () -> mForwardGains.getAcceleration());
        m.put("ElevatorForCruiseVel", () -> mForwardGains.getCruiseVelocity());

        m.put("ElevatorRevKp", () -> mReverseGains.getKp());
        m.put("ElevatorRevKd", () -> mReverseGains.getKd());
        m.put("ElevatorRevKf", () -> mReverseGains.getKf());
        m.put("ElevatorRevAccel", () -> mReverseGains.getAcceleration());
        m.put("ElevatorRevCruiseVel", () -> mReverseGains.getCruiseVelocity());

        m.put("ElevatorWantedPos", () -> mWantedPosition);
        m.put("ElevatorPos", this::getPosition);
        m.put("ElevatorPower", this::getPower);
        m.put("ElevatorVolt", this::getVoltage);
        m.put("ElevatorForLimit", this::isForwardLimitHit);
        m.put("ElevatorRevLimit", this::isReverseLimitHit);

        return m;
    }

    @Override
    public void updateFromDashboard() {
        /*setGains(new ClosedLoopGains(SmartDashboard.getNumber("ElevatorKp", 0), 0,
                SmartDashboard.getNumber("ElevatorKd", 0),
                SmartDashboard.getNumber("ElevatorKf", 0),
                (int) SmartDashboard.getNumber("ElevatorAccel", 0),
                (int) SmartDashboard.getNumber("ElevatorCruiseVel", 0)));*/
        setForwardGains(new ClosedLoopGains(
                SmartDashboard.getNumber("ElevatorForKp", 0),
                0,
                SmartDashboard.getNumber("ElevatorForKd", 0),
                SmartDashboard.getNumber("ElevatorForKf", 0),
                (int) SmartDashboard.getNumber("ElevatorForAccel", 0),
                (int) SmartDashboard.getNumber("ElevatorForCruiseVel", 0)
        ));
        setReverseGains(new ClosedLoopGains(
                SmartDashboard.getNumber("ElevatorRevKp", 0),
                0,
                SmartDashboard.getNumber("ElevatorRevKd", 0),
                SmartDashboard.getNumber("ElevatorRevKf", 0),
                (int) SmartDashboard.getNumber("ElevatorRevAccel", 0),
                (int) SmartDashboard.getNumber("ElevatorRevCruiseVel", 0)
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
        mLogger.debug("Wanted Position: {}", mWantedPosition);
        mLogger.debug("Current Position: {}", getPosition());
    }

    @Override
    public void resetSensors() {
        CTREDiagnostics.checkCommand(mMaster.setSelectedSensorPosition(0), "Failed to zero Elevator encoder!");
    }

    @Override
    public Map<String, Supplier> getCSVTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("position", this::getPosition);
        m.put("wanted_position", () -> mWantedPosition);
        return m;
    }

}
