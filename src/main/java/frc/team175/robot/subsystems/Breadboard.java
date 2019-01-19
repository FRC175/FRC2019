package frc.team175.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import frc.team175.robot.commands.ManualCIMControl;
import frc.team175.robot.util.AldrinTalonSRX;
import frc.team175.robot.util.AldrinVictorSPX;
import frc.team175.robot.util.CTREFactory;
import frc.team175.robot.util.Constants;

import java.util.Map;

/**
 * @author Arvind
 */
public class Breadboard extends AldrinSubsystem {

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
    public enum LineSensorPosition {
        /* Political Spectrum */
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
    }

    public static Breadboard getInstance() {
        if (sInstance == null) {
            sInstance = new Breadboard();
        }

        return sInstance;
    }

    private Breadboard() {
        try {
            /* Instantiation */
            // CTREFactory.getTalon(portNum : int)
            // CTREFactory.getVictor(portNum : int)
            mLeft = CTREFactory.getTalon(Constants.kLeftTalonPort);
            mRight = CTREFactory.getVictor(Constants.kRightVictorPort);
            mCIM = CTREFactory.getTalon(Constants.kCIMTalonPort);

            // CANSparkMax(deviceID : int, type : MotorType)
            // mNEO = new CANSparkMax(Constants.NEO_MOTOR_PORT, MotorType.kBrushless);

            // Solenoid
            // DoubleSolenoid
            mShift = new Solenoid(Constants.kShiftChannel);
            mLateralDeploy = new DoubleSolenoid(Constants.kLateralDeployForwardChannel, Constants.kLateralDeployReverseChannel);

            // DigitalInput(io : int)
            mSensor = new DigitalInput(Constants.kOpticalSensorPort);
            mLineSensors = Map.of(
                    "LeftTwo", new DigitalInput(Constants.kLeftTwoSensorPort),
                    "LeftOne", new DigitalInput(Constants.kLeftOneSensorPort),
                    "Center", new DigitalInput(Constants.kCenterSensorPort),
                    "RightOne", new DigitalInput(Constants.kRightOneSensorPort),
                    "RightTwo", new DigitalInput(Constants.kRightTwoSensorPort)
            );
        } catch (Exception e) {
            mLogger.error("Breadboard failed to instantiate.\n{}", e);
        }

        mLogger.info("Breadboard instantiated succesfully!");
    }

    public void setLeftPower(double power) {
        mLeft.setPower(power);
    }

    public void setRightPower(double power) {
        mRight.setPower(power);
    }

    public void setCIMPower(double power) {
        mCIM.setPower(power);
    }

    public void setCIMPosition(int position) {
        mCIM.setPosition(position);
    }

    // public void setNEOPower(double power) {
    //     mNEO.set(power);
    // }

    public void setHighGear(boolean enable) {
        mShift.set(enable);
    }

    public void deployLateral(boolean enable) {
        mLateralDeploy.set(enable ? Value.kForward : Value.kReverse);
    }

    public double getLeftPower() {
        return mLeft.getPower();
    }

    public double getRightPower() {
        return mRight.getPower();
    }

    public double getCIMPower() {
        return mCIM.getPower();
    }

    public int getCIMPosition() {
        return mCIM.getPosition();
    }

    // public double getNEOPower() {
    //     return mNEO.get();
    // }

    public boolean isGearHigh() {
        return mShift.get();
    }

    public boolean isLateralDeployed() {
        return mLateralDeploy.get() == Value.kForward;
    }

    public boolean doesSensorSee() {
        return mSensor.get();
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
        mLogger.debug("Line Sensor Array: {}", getLineSensorArray());
        mLineSensors.forEach((str, sensor) -> { mLogger.debug("{} State: {}", str, sensor.get()); } );
    }

    @Override
    public void onTeleop() {
        outputToConsole();
        // mLogger.debug("Breadboard periodic message");
    }

}