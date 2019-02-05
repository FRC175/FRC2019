package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.team175.robot.Constants;
import com.team175.robot.positions.LineSensorPosition;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;

import com.team175.robot.util.ClosedLoopTunable;
import com.team175.robot.util.PIDFGains;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public class LateralDrive extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private AldrinTalonSRX mMaster;
    private Solenoid mDeploy;
    private Map<String, DigitalInput> mLineSensors;
    private int mWantedPosition;

    // Singleton Instance
    private static LateralDrive sInstance;

    public static LateralDrive getInstance() {
        if (sInstance == null) {
            sInstance = new LateralDrive();
        }

        return sInstance;
    }

    private LateralDrive() {
        /* Instantiations */
        // CTREFactory.getMasterTalon(portNum : int)
        mMaster = CTREFactory.getMasterTalon(Constants.LATERAL_DRIVE_PORT);

        // Solenoid(channel : int)
        mDeploy = new Solenoid(Constants.LATERAL_DRIVE_DEPLOY_CHANNEL);

        // DigitalInput(portNum : int)
        /*mLineSensors = Map.of(
                "LeftTwo", new DigitalInput(Constants.LEFT_TWO_SENSOR_PORT),
                "LeftOne", new DigitalInput(Constants.LEFT_ONE_SENSOR_PORT),
                "Center", new DigitalInput(Constants.CENTER_SENSOR_PORT),
                "RightOne", new DigitalInput(Constants.RIGHT_ONE_SENSOR_PORT),
                "RightTwo", new DigitalInput(Constants.RIGHT_TWO_SENSOR_PORT)
        );*/

        mWantedPosition = 0;
    }

    public void deploy(boolean enable) {
        mDeploy.set(enable);
    }

    public boolean isDeployed() {
        return mDeploy.get();
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

    public void setPosition(LineSensorPosition position) {
        setPosition(position.positionToMove());
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

    private String getLineSensorArray() {
        String binarySensorArray = "";
        binarySensorArray += mLineSensors.get("LeftTwo").get() ? "1" : "0";
        binarySensorArray += mLineSensors.get("LeftOne").get() ? "1" : "0";
        binarySensorArray += mLineSensors.get("Center").get() ? "1" : "0";
        binarySensorArray += mLineSensors.get("RightOne").get() ? "1" : "0";
        binarySensorArray += mLineSensors.get("RightTwo").get() ? "1" : "0";

        return binarySensorArray;
    }

    public LineSensorPosition getLineSensorPosition() {
        switch (getLineSensorArray()) {
            case "10000":
                return LineSensorPosition.EXTREME_LEFT;
            case "11000":
                return LineSensorPosition.LEFTIST;
            case "01000":
                return LineSensorPosition.LEFT;
            case "01100":
                return LineSensorPosition.CENTER_LEFT;
            case "00100":
                return LineSensorPosition.CENTER;
            case "00110":
                return LineSensorPosition.CENTER_RIGHT;
            case "00010":
                return LineSensorPosition.RIGHT;
            case "00011":
                return LineSensorPosition.RIGHTIST;
            case "00001":
                return LineSensorPosition.EXTREME_RIGHT;
            default:
                // Robot orientation wrong
                return LineSensorPosition.ERROR;
        }
    }

    public void sendToDashboard() {
        SmartDashboard.putNumber("LateralDrive kP", 0);
        SmartDashboard.putNumber("LateralDrive kD", 0);
        SmartDashboard.putNumber("LateralDrive kF", 0);
    }

    public void setPIDF(PIDFGains gains) {
        mMaster.config_kP(gains.getKp());
        mMaster.config_kI(gains.getKi());
        mMaster.config_kD(gains.getKd());
        mMaster.config_kF(gains.getKf());
    }

    @Override
    public void updatePIDF() {
        setPIDF(new PIDFGains(SmartDashboard.getNumber("LateralDrive kP", 0), 0,
                SmartDashboard.getNumber("LateralDrive kD", 0),
                SmartDashboard.getNumber("LateralDrive kF", 0)));
    }

    @Override
    public void updateWantedPosition() {
        setPosition((int) SmartDashboard.getNumber("LateralDrive Position", 0));
    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public LinkedHashMap<String, DoubleSupplier> getCSVProperties() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("position", this::getPosition);
        m.put("wanted_position", () -> mWantedPosition);
        return m;
    }

}
