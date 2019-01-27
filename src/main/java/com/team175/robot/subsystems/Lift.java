package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team175.robot.Constants;

import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

/**
 * @author Arvind
 */
public class Lift extends AldrinSubsystem {

    /* Declarations */
    // Talon SRX
    private AldrinTalonSRX mDrive;

    // Talons
    private Talon mFront, mRear;

    // Singleton Instance
    private static Lift sInstance;

    public static Lift getInstance() {
        if (sInstance == null) {
            sInstance = new Lift();
        }

        return sInstance;
    }

    private Lift() {
        /* Instantiations */
        // CTREFactory.getTalon(portNum : int)
        mDrive = CTREFactory.getTalon(Constants.LIFT_DRIVE_PORT);

        // Talon(portNum : int)
        mFront = new Talon(Constants.LIFT_FRONT_PORT);
        mRear = new Talon(Constants.LIFT_REAR_PORT);
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
