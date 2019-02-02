package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.util.AldrinTalonSRX;

import com.team175.robot.util.CTREFactory;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 * @author Arvind
 */
public class Manipulator extends AldrinSubsystem {

    /* Declarations */
    private AldrinTalonSRX mArm;
    private Talon mFrontRoller;
    private Talon mRearRoller;
    private Solenoid mBopper;
    private DoubleSolenoid mRetract;

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
        /* Instantiations */
        // CTREFactory.getMasterTalon(portNum : int)
        mArm = CTREFactory.getMasterTalon(Constants.MANIPULATOR_ARM_PORT);

        // Talon(portNum : int)
        mFrontRoller = new Talon(Constants.MANIPULATOR_FRONT_ROLLER);
        mRearRoller = new Talon(Constants.MANIPULATOR_REAR_ROLLER);

        // Solenoid(channel : int)
        // mBopper = new Solenoid(Constants.MANIPULATOR_BOPPER_CHANNEL);

        // DoubleSolenoid(forwardChannel : int, reverseChannel : int)
        // mRetract = new DoubleSolenoid(Constants.MANIPULATOR_RETRACT_FORWARD_CHANNEL, Constants.MANIPULATOR_RETRACT_REVERSE_CHANNEL);

        mArmWantedPosition = 0;
    }

    public void setRollerPower(double frontPower, double rearPower) {
        mFrontRoller.set(frontPower);
        mRearRoller.set(rearPower);
    }

    public void setRollerPosition(ManipulatorRollerPosition position) {

        switch (position) {
            case SHOOT_HATCH:
                setRollerPower(position.getFrontPower(), position.getRearPower());
                // TODO: Maybe add stop power before bopper is deployed
                setBopper(true);
                // TODO: Add sleep in code
                setBopper(false);
                break;
            case GRAB_HATCH:
            case SHOOT_CARGO:
            case GRAB_CARGO:
            case IDLE:
            default:
                setRollerPower(position.getFrontPower(), position.getRearPower());
                break;
        }
    }

    public double getFrontRollerPower() {
        return mFrontRoller.get();
    }

    public double getRearRollerPower() {
        return mRearRoller.get();
    }

    public void setBopper(boolean push) {
        // mBopper.set(push);
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

    public void setArmPosition(int position) {
        mArmWantedPosition = position;
        mArm.set(ControlMode.MotionMagic, mArmWantedPosition);
    }

    public void setArmPosition(ManipulatorArmPosition position) {
        setArmPosition(position.positionToMove());
    }

    public int getArmPosition() {
        return mArm.getSelectedSensorPosition();
    }

    public boolean isArmAtWantedPosition() {
        return Math.abs(getArmPosition() - mArmWantedPosition) <= Constants.ALLOWED_POSITION_DEVIATION;
    }

    public double getTimestamp() {
        return Timer.getFPGATimestamp();
    }

    @Override
    protected void initDefaultCommand() {

    }

}
