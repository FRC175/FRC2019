package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.team175.robot.Constants;
import com.team175.robot.positions.LineSensorPosition;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.CTREFactory;

import com.team175.robot.util.tuning.ClosedLoopTunable;
import com.team175.robot.util.tuning.ClosedLoopGains;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public final class LateralDrive extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private final AldrinTalonSRX mMaster;
    private final Solenoid mDeploy;
    // private final Map<String, DigitalInput> mLineSensors;

    private int mWantedPosition;
    private ClosedLoopGains mGains;

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
        mGains = Constants.LATERAL_DRIVE_GAINS;
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

    /*public double getVoltage() {
        return mMaster.getMotorOutputVoltage();
    }*/

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

    public int getVelocity() {
        return mMaster.getSelectedSensorVelocity();
    }

    public boolean isAtWantedPosition() {
        return Math.abs(getPosition() - mWantedPosition) <= Constants.ALLOWED_POSITION_DEVIATION;
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

    public void resetEncoder() {
        mMaster.setSelectedSensorPosition(0);
    }

    /*private String getLineSensorArray() {
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
    }*/

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public void stop() {
        setPower(0);
    }

    @Override
    public Map<String, Object> getTelemetry() {
        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("LateralKp", mGains.getKp());
        m.put("LateralKd", mGains.getKd());
        m.put("LateralKf", mGains.getKf());
        m.put("LateralAccel", mGains.getAcceleration());
        m.put("LateralCruiseVel", mGains.getCruiseVelocity());
        m.put("LateralWantedPos", mWantedPosition);
        m.put("LateralPos", getPosition());
        m.put("LateralPower", getPower());
        return m;
    }

    @Override
    public void updateFromDashboard() {
        setGains(new ClosedLoopGains(SmartDashboard.getNumber("LateralKp", 0), 0,
                SmartDashboard.getNumber("LateralKd", 0),
                SmartDashboard.getNumber("LateralKf", 0),
                (int) SmartDashboard.getNumber("LateralAccel", 0),
                (int) SmartDashboard.getNumber("LateralCruiseVel", 0)));
        setPosition((int) SmartDashboard.getNumber("LateralWantedPos", 0));
    }

    @Override
    public void updateGains() {
        updateFromDashboard();
        mLogger.debug("Wanted Position: {}", mWantedPosition);
        mLogger.debug("Current Position: {}", getPosition());
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

}
