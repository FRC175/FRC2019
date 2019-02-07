package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;
import com.team175.robot.util.ClosedLoopTunable;
import com.team175.robot.util.ClosedLoopGains;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import static java.util.Map.entry;

/**
 * @author Arvind
 */
public class Elevator extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private AldrinTalonSRX mMaster;

    private int mWantedPosition;
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
        mGains = Constants.ELEVATOR_GAINS;
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

    public boolean isAtWantedPosition() {
        return Math.abs(getPosition() - mWantedPosition) <= Constants.ALLOWED_POSITION_DEVIATION;
    }

    public void resetEncoder() {
        mMaster.setSelectedSensorPosition(0);
    }

    public void setGains(ClosedLoopGains gains) {
        mGains = gains;
        mMaster.config_kP(mGains.getKp());
        mMaster.config_kI(mGains.getKi());
        mMaster.config_kD(mGains.getKd());
        mMaster.config_kF(mGains.getKf());
        mMaster.configMotionAcceleration(mGains.getAcceleration());
        mMaster.configMotionCruiseVelocity(mGains.getCruiseVelocity());
    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public void stop() {
        setPower(0);
    }

    public Map<String, Object> getTelemetry() {
        return Map.ofEntries(
                entry("ElevatorKp", mGains.getKp()),
                entry("ElevatorKd", mGains.getKd()),
                entry("ElevatorKf", mGains.getKf()),
                entry("ElevatorAccel", mGains.getAcceleration()),
                entry("ElevatorCruiseVel", mGains.getCruiseVelocity()),
                entry("ElevatorWantedPos", mWantedPosition),
                entry("ElevatorPos", getPosition()),
                entry("ElevatorPower", getPower()),
                entry("ElevatorVolt", getVoltage())
        );
    }

    public void outputToDashboard() {
        getTelemetry().forEach((k, v) -> {
            if (v instanceof Double || v instanceof Integer) {
                try {
                    SmartDashboard.putNumber(k, Double.parseDouble(v.toString()));
                } catch (NumberFormatException e) {
                    mLogger.error("Failed to parse number to SmartDashboard!", e);
                }
            } else if (v instanceof Boolean) {
                SmartDashboard.putBoolean(k, Boolean.parseBoolean(v.toString()));
            } else {
                SmartDashboard.putString(k, v.toString());
            }
        });
    }

    public void updateFromDashboard() {
        setGains(new ClosedLoopGains(SmartDashboard.getNumber("ElevatorKp", 0), 0,
                SmartDashboard.getNumber("ElevatorKd", 0),
                SmartDashboard.getNumber("ElevatorKf", 0),
                (int) SmartDashboard.getNumber("ElevatorAccel", 0),
                (int) SmartDashboard.getNumber("ElevatorCruiseVel", 0)));
        setPosition((int) SmartDashboard.getNumber("ElevatorWantedPos", 0));
    }

    @Override
    public void updateGains() {
        // outputToDashboard();
        updateFromDashboard();
        mLogger.debug("Wanted Position: {}", mWantedPosition);
        mLogger.debug("Current Pos: {}", getPosition());
    }

    @Override
    public void reset() {
        resetEncoder();
    }

    @Override
    public Map<String, DoubleSupplier> getCSVTelemetry() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("position", this::getPosition);
        m.put("wanted_position", () -> mWantedPosition);
        return m;
    }










    /*public void sendToDashboardTeleop() {
        SmartDashboard.putNumber("Elevator kP", 0);
        SmartDashboard.putNumber("Elevator kD", 0);
        SmartDashboard.putNumber("Elevator kF", 0);
        SmartDashboard.putNumber("Elevator Position", getPosition());
    }

    public void sendToDashboard() {
        updateGains();
        mWantedPosition = (int) SmartDashboard.getNumber("Elevator Position", 0);
    }

    public void setPIDF(ClosedLoopGains gains) {
        mMaster.config_kP(gains.getKp());
        mMaster.config_kI(gains.getKi());
        mMaster.config_kD(gains.getKd());
        mMaster.config_kF(gains.getKf());
    }

    @Override
    public void updateGains() {
        setPIDF(new ClosedLoopGains(SmartDashboard.getNumber("Elevator kP", 0), 0,
                SmartDashboard.getNumber("Elevator kD", 0),
                SmartDashboard.getNumber("Elevator kF", 0)));
    }

    @Override
    public void updateWantedPosition() {
        resetEncoder();
        setPosition(1000);
    }

    @Override
    public LinkedHashMap<String, DoubleSupplier> getCSVTelemetry() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("position", this::getPosition);
        m.put("wanted_position", () -> mWantedPosition);
        return m;
    }*/

}
