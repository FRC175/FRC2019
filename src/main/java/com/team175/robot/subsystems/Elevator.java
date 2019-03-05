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
 * TODO: Implement gain swapping when going up and down.
 *
 * @author Arvind
 */
public final class Elevator extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private final AldrinTalonSRX mMaster;

    private int mWantedPosition;
    private boolean mIsGoingForward;
    private ClosedLoopGains mGains;

    // Singleton Instance
    private static Elevator sInstance;

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
        mGains = CTREConfiguration.getGainsFromConfig(profile.getElevatorConfig(), true);

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
        return Math.abs(getPosition() - mWantedPosition) <= Constants.ALLOWED_ELEVATOR_POSITION_DEVIATION;
    }

    public void setGains(ClosedLoopGains gains) {
        mGains = gains;
        CTREDiagnostics.checkCommand(mMaster.configPIDF(mGains.getKp(), mGains.getKi(), mGains.getKd(), mGains.getKf()),
                "Failed to config Elevator PID gains!");
        CTREDiagnostics.checkCommand(mMaster.configMotionAcceleration(mGains.getAcceleration()),
                "Failed to config Elevator acceleration!");
        CTREDiagnostics.checkCommand(mMaster.configMotionCruiseVelocity(mGains.getCruiseVelocity()),
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
        m.put("ElevatorKp", () -> mGains.getKp());
        m.put("ElevatorKd", () -> mGains.getKd());
        m.put("ElevatorKf", () -> mGains.getKf());
        m.put("ElevatorAccel", () -> mGains.getAcceleration());
        m.put("ElevatorCruiseVel", () -> mGains.getCruiseVelocity());
        m.put("ElevatorWantedPos", () -> mWantedPosition);
        m.put("ElevatorPos", this::getPosition);
        m.put("ElevatorPower", this::getPower);
        m.put("ElevatorVolt", this::getVoltage);
        return m;
    }

    @Override
    public void updateFromDashboard() {
        setGains(new ClosedLoopGains(SmartDashboard.getNumber("ElevatorKp", 0), 0,
                SmartDashboard.getNumber("ElevatorKd", 0),
                SmartDashboard.getNumber("ElevatorKf", 0),
                (int) SmartDashboard.getNumber("ElevatorAccel", 0),
                (int) SmartDashboard.getNumber("ElevatorCruiseVel", 0)));
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
