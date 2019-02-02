package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.positions.LineSensorPosition;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;

import java.util.Map;

/**
 * @author Arvind
 */
public class LateralDrive extends AldrinSubsystem {

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
        // mDeploy = new Solenoid(Constants.LATERAL_DRIVE_DEPLOY_CHANNEL);

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
        // mDeploy.set(enable ? Value.kForward : Value.kReverse);
        // mDeploy.set(enable);
    }

    public boolean isDeployed() {
        // return mDeploy.get() == Value.kForward;
        // return mDeploy.get();
        return false;
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

    @Override
    protected void initDefaultCommand() {

    }

}
