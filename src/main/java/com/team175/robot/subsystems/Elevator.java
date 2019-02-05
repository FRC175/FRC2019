package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;
import com.team175.robot.util.ClosedLoopTunable;
import com.team175.robot.util.PIDFGains;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public class Elevator extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private AldrinTalonSRX mMaster;

    public int mWantedPosition;

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

    @Override
    protected void initDefaultCommand() {
    }

    public void sendToDashboardTeleop() {
        SmartDashboard.putNumber("Elevator kP", 0);
        SmartDashboard.putNumber("Elevator kD", 0);
        SmartDashboard.putNumber("Elevator kF", 0);
        SmartDashboard.putNumber("Elevator Position", getPosition());
    }

    public void sendToDashboard() {
        updatePIDF();
        mWantedPosition = (int) SmartDashboard.getNumber("Elevator Position", 0);
    }

    public void setPIDF(PIDFGains gains) {
        mMaster.config_kP(gains.getKp());
        mMaster.config_kI(gains.getKi());
        mMaster.config_kD(gains.getKd());
        mMaster.config_kF(gains.getKf());
    }

    @Override
    public void updatePIDF() {
        setPIDF(new PIDFGains(SmartDashboard.getNumber("Elevator kP", 0), 0,
                SmartDashboard.getNumber("Elevator kD", 0),
                SmartDashboard.getNumber("Elevator kF", 0)));
    }

    @Override
    public void updateWantedPosition() {
        resetEncoder();
        setPosition(1000);
    }

    @Override
    public LinkedHashMap<String, DoubleSupplier> getCSVProperties() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("position", this::getPosition);
        m.put("wanted_position", () -> mWantedPosition);
        return m;
    }
    
}
