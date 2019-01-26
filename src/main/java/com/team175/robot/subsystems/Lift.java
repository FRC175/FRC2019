package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team175.robot.Constants;

import com.team175.robot.util.CTREFactory;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

/**
 * @author Arvind
 */
public class Lift extends AldrinSubsystem {

    // Talons
    private Talon mFront, mRear;
    private TalonSRX mDrive;

    // Limit Switches
    private DigitalInput mFrontLimit, mRearLimit;

    // Singleton Instance
    private static Lift sInstance;

    public static Lift getInstance() {
        if (sInstance == null) {
            sInstance = new Lift();
        }

        return sInstance;
    }

    private Lift() {
        // Talon(portNum : int)
        mFront = new Talon(Constants.LIFT_FRONT_PORT);
        mRear = new Talon(Constants.LIFT_REAR_PORT);
        mDrive = CTREFactory.getTalon(Constants.LIFT_DRIVE_PORT);

        // LimitSwitch(portNum : int)
        /*mFrontLimit = new DigitalInput(Constants.LIFT_FRONT_LIMIT_PORT);
        mRearLimit = new DigitalInput(Constants.LIFT_REAR_LIMIT_PORT);*/
    }

    public void setLiftPower(double power) {
        mFront.set(power);
        mRear.set(power);
    }

    public void setDrivePower(double power) {
        mDrive.set(ControlMode.PercentOutput, power);
    }

    public double getFrontPower() {
        return mFront.get();
    }

    public double getRearPower() {
        return mRear.get();
    }

    public double getDrivePower() {
        return mDrive.getMotorOutputPercent();
    }

    @Override
    protected void initDefaultCommand() {

    }

}
