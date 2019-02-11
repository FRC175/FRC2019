package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.CTREFactory;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import com.team175.robot.util.tuning.ClosedLoopGains;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public final class Elevator extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private final AldrinTalonSRX mMaster;

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

    public int getVelocity() {
        return mMaster.getSelectedSensorVelocity();
    }

    public boolean isAtWantedPosition() {
        return Math.abs(getPosition() - mWantedPosition) <= Constants.ALLOWED_POSITION_DEVIATION;
    }

    public void setGains(ClosedLoopGains gains) {
        mGains = gains;
        mMaster.configPIDF(mGains.getKp(), mGains.getKi(), mGains.getKd(), mGains.getKf());
        mMaster.configMotionAcceleration(mGains.getAcceleration());
        mMaster.configMotionCruiseVelocity(mGains.getCruiseVelocity());
    }

    @Override
    public void resetSensors() {
        mMaster.setSelectedSensorPosition(0);
    }

    @Override
    public void stop() {
        setPower(0);
    }

    @Override
    public Map<String, Object> getTelemetry() {
        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("ElevatorKp", mGains.getKp());
        m.put("ElevatorKd", mGains.getKd());
        m.put("ElevatorKf", mGains.getKf());
        m.put("ElevatorAccel", mGains.getAcceleration());
        m.put("ElevatorCruiseVel", mGains.getCruiseVelocity());
        m.put("ElevatorWantedPos", mWantedPosition);
        m.put("ElevatorPos", getPosition());
        m.put("ElevatorPower", getPower());
        m.put("ElevatorVolt", getVoltage());
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
    public void updateGains() {
        updateFromDashboard();
        mLogger.debug("Wanted Position: {}", mWantedPosition);
        mLogger.debug("Current Position: {}", getPosition());
    }

    @Override
    public void reset() {
        resetSensors();
    }

    @Override
    public Map<String, DoubleSupplier> getCSVTelemetry() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("position", this::getPosition);
        m.put("wanted_position", () -> mWantedPosition);
        return m;
    }

}
