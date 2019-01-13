package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.commands.ManualCIMControl;
import frc.robot.util.AldrinTalonSRX;
import frc.robot.util.AldrinVictorSPX;
import frc.robot.util.CTREFactory;
import frc.robot.util.Constants;

/**
 * @author Arvind
 */
public class Breadboard extends AldrinSubsystem {

    // CTRE Motor Controllers
    private AldrinTalonSRX mLeft;
    private AldrinVictorSPX mRight;
    private AldrinTalonSRX mCIM;

    // Spark MAXs
    // private CANSparkMax mNEO;

    // Optical Sensors
    private DigitalInput mSensor;

    private static Breadboard sInstance;

    public static Breadboard getInstance() {
        if (sInstance == null) {
            try {
                sInstance = new Breadboard();
            } catch (Exception e) {
                // Insert log here
            }
        }

        return sInstance;
    }

    private Breadboard() {
        mLeft = CTREFactory.getSRX(Constants.kLeftTalonPort);
        mRight = CTREFactory.getSPX(Constants.kRightVictorPort);
        mCIM = CTREFactory.getSRX(Constants.kCIMTalonPort);

        // mNEO = new CANSparkMax(Constants.NEO_MOTOR_PORT, MotorType.kBrushless);

        mSensor = new DigitalInput(Constants.kOpticalSensorPort);
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

    // public void setNEOPower(double power) {
    //     mNEO.set(power);
    // }

    public double getLeftPower() {
        return mLeft.getPower();
    }

    public double getRightPower() {
        return mRight.getPower();
    }

    public double getCIMPower() {
        return mCIM.getPower();
    }

    // public double getNEOPower() {
    //     return mNEO.get();
    // }

    public boolean doesSensorSee() {
        return mSensor.get();
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
        System.out.println("Does sensor see white? " + doesSensorSee());
        System.out.println();
    }

    @Override
    public void onTeleop() {
        outputToConsole();
    }

}