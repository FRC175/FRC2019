package com.team175.robot.subsystems.deprecated;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;

import com.team175.robot.commands.deprecated.ManualCIMControl;
import com.team175.robot.subsystems.AldrinSubsystem;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.AldrinVictorSPX;
import com.team175.robot.util.CTREFactory;
import com.team175.robot.Constants;

import java.util.Map;

/**
 * @author Arvind
 */
public class Breadboard extends AldrinSubsystem {

    /* Declarations */
    // CTRE Motor Controllers
    private AldrinTalonSRX mLeft;
    private AldrinVictorSPX mRight;
    private AldrinTalonSRX mCIM;

    // Spark MAX
    // private CANSparkMax mNEO;

    // Optical Sensors
    private DigitalInput mSensor;
    private Map<String, DigitalInput> mLineSensors;

    // Pneumatics
    private Solenoid mShift;
    private DoubleSolenoid mLateralDeploy;

    // Singleton Instance
    private static Breadboard sInstance;

    // Enum
    /*public enum LineSensorPosition {
        *//* Political Spectrum *//*
        EXTREME_LEFT(-100),
        LEFTIST(-75),
        LEFT(-50),
        CENTER_LEFT(-25),
        CENTER(0),
        CENTER_RIGHT(25),
        RIGHT(50),
        RIGHTIST(75),
        EXTREME_RIGHT(100),
        ERROR(0);

        private final int POSITION;

        private LineSensorPosition(int position) {
            POSITION = position;
        }

        public int positionToMove() {
            return POSITION;
        }
    }*/

    public static Breadboard getInstance() {
        if (sInstance == null) {
            sInstance = new Breadboard();
        }

        return sInstance;
    }

    private Breadboard() {
        try {
            /* Instantiations */
            // CTREFactory.getTalon(portNum : int)
            // CTREFactory.getVictor(portNum : int)
            mLeft = CTREFactory.getTalon(Constants.LEFT_TALON_PORT);
            mRight = CTREFactory.getVictor(Constants.RIGHT_VICTOR_PORT);
            mCIM = CTREFactory.getTalon(Constants.CIM_TALON_PORT);

            // CANSparkMax(deviceID : int, type : MotorType)
            // mNEO = new CANSparkMax(Constants.NEO_MOTOR_PORT, MotorType.kBrushless);

            // Solenoid
            // DoubleSolenoid
            mShift = new Solenoid(Constants.SHIFT_CHANNEL);
            mLateralDeploy = new DoubleSolenoid(Constants.LATERAL_DEPLOY_FORWARD_CHANNEL, Constants.LATERAL_DEPLOY_REVERSE_CHANNEL);

            // DigitalInput(io : int)
            mSensor = new DigitalInput(Constants.OPTICAL_SENSOR_PORT);
            /*mLineSensors = Map.of(
                    "LeftTwo", new DigitalInput(Constants.LEFT_TWO_SENSOR_PORT),
                    "LeftOne", new DigitalInput(Constants.LEFT_ONE_SENSOR_PORT),
                    "Center", new DigitalInput(Constants.CENTER_SENSOR_PORT),
                    "RightOne", new DigitalInput(Constants.RIGHT_ONE_PORT),
                    "RightTwo", new DigitalInput(Constants.RIGHT_TWO_PORT)
            );*/
        } catch (Exception e) {
            mLogger.error("Breadboard failed to instantiate.", e);
        }

        mLogger.info("Breadboard instantiated successfully!");
    }

    public void setLeftPower(double power) {
        mLeft.set(ControlMode.PercentOutput, power);
    }

    public void setRightPower(double power) {
        mRight.set(ControlMode.PercentOutput, power);
    }

    public void setCIMPower(double power) {
        mCIM.set(ControlMode.PercentOutput, power);
    }

    public void setHighGear(boolean enable) {
        mShift.set(enable);
    }

    public void deployLateral(boolean enable) {
        mLateralDeploy.set(enable ? Value.kForward : Value.kReverse);
    }

    public double getLeftPower() {
        return mLeft.getMotorOutputPercent();
    }

    public double getRightPower() {
        return mRight.getMotorOutputPercent();
    }

    public double getCIMPower() {
        return mCIM.getMotorOutputPercent();
    }

    public boolean isHighGear() {
        return mShift.get();
    }

    public boolean isLateralDeployed() {
        return mLateralDeploy.get() == Value.kForward;
    }

    public boolean isSensorOnLine() {
        return mSensor.get();
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
    }

    public void setNEOPower(double power) {
        mNEO.set(power);
    }

    public double getNEOPower() {
        return mNEO.get();
    }

    @Override
    public void onTeleop() {
        outputToConsole();
        // mLogger.debug("Breadboard periodic message");
    }*/

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ManualCIMControl());
    }

    private void outputToConsole() {
        // System.out.println("Left Power: " + getLeftPower());
        // System.out.println("Right Power: " + getRightPower());
        // System.out.println("CIM Power: " + getCIMPower());
        // System.out.println("NEO Power: " + getNEOPower());
        // System.out.println("Does sensor see white? " + doesSensorSee());
        // System.out.println("Line Sensor Array: " + getLineSensorArray());
        // mLineSensors.forEach((str, sensor) -> { System.out.printf("%s State: %s", str, sensor.get()); } );

        // Lateral Drive
        /*mLogger.debug("Line Sensor Array: {}", getLineSensorArray());
        mLineSensors.forEach((str, sensor) -> {
            mLogger.debug("{} State: {}", str, sensor.get());
        });*/
    }

}