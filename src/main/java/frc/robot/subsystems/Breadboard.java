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

    private TalonSRX mLeftMotor;
    private TalonSRX mCIMMotor;
    private VictorSPX mRightMotor;

    private CANSparkMax mNEOMotor;
    private CANSparkMaxLowLevel.MotorType mType;

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
        mLeftMotor = CTREFactory.getSRX(Constants.LEFT_MOTOR_PORT);
        mRightMotor = CTREFactory.getSPX(Constants.RIGHT_MOTOR_PORT);
        mCIMMotor = CTREFactory.getSRX(Constants.CIM_MOTOR_PORT);

        //mType = MotorType.kBrushed;

        mNEOMotor = new CANSparkMax(Constants.NEO_MOTOR_PORT, MotorType.kBrushless);
    }

    public void setLeftPower(double power) {
        mLeftMotor.set(ControlMode.PercentOutput, power);
    }

    public void setRightPower(double power) {
        mRightMotor.set(ControlMode.PercentOutput, power);
    }

    public void setCIMPower(double power) {
        mCIMMotor.set(ControlMode.PercentOutput, power);
    }

    public void setNEOPower(double power) {
        mNEOMotor.set(power);
    }

    public double getLeftPower() {
        return mLeftMotor.getMotorOutputPercent();
    }

    public double getRightPower() {
        return mRightMotor.getMotorOutputPercent();
    }

    public double getCIMPower() {
        return mCIMMotor.getMotorOutputPercent();
    }

    public double getNEOPower() {
        return mNEOMotor.get();
    }

    @Override
    protected void initDefaultCommand() {

    }

}