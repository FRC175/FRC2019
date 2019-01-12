package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.util.CTREFactory;
import frc.robot.util.Constants;

/**
 * @author Arvind
 */
public class Breadboard extends Subsystem {

    private TalonSRX mLeft;
    private TalonSRX mCIM;
    private VictorSPX mRight;

    // private CANSparkMax mNEO;

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
        mLeft = CTREFactory.getSRX(Constants.LEFT_MOTOR_PORT);
        mRight = CTREFactory.getSPX(Constants.RIGHT_MOTOR_PORT);
        mCIM = CTREFactory.getSRX(Constants.CIM_MOTOR_PORT);

        // mNEO = new CANSparkMax(Constants.NEO_MOTOR_PORT, MotorType.kBrushless);
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

    // public void setNEOPower(double power) {
    //     mNEO.set(power);
    // }

    public double getLeftPower() {
        return mLeft.getMotorOutputPercent();
    }

    public double getRightPower() {
        return mRight.getMotorOutputPercent();
    }

    public double getCIMPower() {
        return mCIM.getMotorOutputPercent();
    }

    // public double getNEOPower() {
    //     return mNEO.get();
    // }

    @Override
    protected void initDefaultCommand() {

    }

}