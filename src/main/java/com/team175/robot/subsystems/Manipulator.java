package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.util.AldrinTalonSRX;

import com.team175.robot.util.CTREFactory;
import edu.wpi.first.wpilibj.Talon;

/**
 * @author Arvind
 */
public class Manipulator extends AldrinSubsystem {

    // Talon SRX
    private AldrinTalonSRX mArm;
    
    // Talons
    private Talon mFrontRoller;
    private Talon mRearRoller;

    // Double
    private double mArmWantedPosition;

    // Singleton Instance
    private static Manipulator sInstance;

    public static Manipulator getInstance() {
        if (sInstance == null) {
            sInstance = new Manipulator();
        }

        return sInstance;
    }

    private Manipulator() {
        mArm = CTREFactory.getTalon(Constants.MANIPULATOR_ARM_PORT);

        mFrontRoller = new Talon(Constants.MANIPULATOR_FRONT_ROLLER);
        mRearRoller = new Talon(Constants.MANIPULATOR_REAR_ROLLER);

        mArmWantedPosition = 0;
    }

    public void setRollerPower(double power) {
        mFrontRoller.set(power);
        mRearRoller.set(power);
    }

    public double getFrontRollerPower() {
        return mFrontRoller.get();
    }

    public double getRearRollerPower() {
        return mRearRoller.get();
    }

    public void setArmMotionMagicPosition(int position) {
        mArmWantedPosition = position;
        mArm.set(ControlMode.MotionMagic, mArmWantedPosition);
    }

    public int getArmPosition() {
        return mArm.getSelectedSensorPosition();
    }

    public boolean isArmAtWantedPosition() {
        return Math.abs(getArmPosition() - mArmWantedPosition) <= Constants.ALLOWED_POSITION_DEVIATION;
    }

    public void setArmPower(double power) {
        mArm.set(ControlMode.PercentOutput, power);
    }

    public double getArmPower() {
        return mArm.getMotorOutputPercent();
    }

    public double getArmVoltage() {
        return mArm.getMotorOutputVoltage();
    }

    @Override
    protected void initDefaultCommand() {

    }

}
