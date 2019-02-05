package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.team175.robot.Constants;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;

import com.team175.robot.util.ClosedLoopTunable;
import com.team175.robot.util.PIDFGains;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public class Manipulator extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private AldrinTalonSRX mArm;
    private Talon mFrontRoller;
    private Talon mRearRoller;
    private Solenoid mHatchPush;
    private DoubleSolenoid mDeploy;

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
        mHatchPush = new Solenoid(Constants.MANIPULATOR_HATCH_PUSH_CHANNEL);

        // DoubleSolenoid(forwardChannel : int, reverseChannel : int)
        mDeploy = new DoubleSolenoid(Constants.MANIPULATOR_DEPLOY_FORWARD_CHANNEL,
                Constants.MANIPULATOR_DEPLOY_REVERSE_CHANNEL);

        mArmWantedPosition = 0;
    }

    public void deploy(boolean enable) {
        mDeploy.set(enable ? Value.kForward : Value.kReverse);
    }

    public boolean isDeployed() {
        return mDeploy.get() == Value.kForward;
    }

    public void setRollerPower(double frontPower, double rearPower) {
        mFrontRoller.set(frontPower);
        mRearRoller.set(rearPower);
    }

    public void setRollerPosition(ManipulatorRollerPosition position) {
        setRollerPower(position.getFrontPower(), position.getRearPower());
    }

    public double getFrontRollerPower() {
        return mFrontRoller.get();
    }

    public double getRearRollerPower() {
        return mRearRoller.get();
    }

    public void pushHatch(boolean enable) {
        mHatchPush.set(enable);
    }

    public boolean isHatchPushed() {
        return mHatchPush.get();
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

    public void sendToDashboard() {
        SmartDashboard.putNumber("ManipulatorArm kP", 0);
        SmartDashboard.putNumber("ManipulatorArm kD", 0);
        SmartDashboard.putNumber("ManipulatorArm kF", 0);
    }

    public void setArmPIDF(PIDFGains gains) {
        mArm.config_kP(gains.getKp());
        mArm.config_kI(gains.getKi());
        mArm.config_kD(gains.getKd());
        mArm.config_kF(gains.getKf());
    }

    @Override
    public void updatePIDF() {
        setArmPIDF(new PIDFGains(SmartDashboard.getNumber("ManipulatorArm kP", 0), 0,
                SmartDashboard.getNumber("ManipulatorArm kD", 0),
                SmartDashboard.getNumber("ManipulatorArm kF", 0)));
    }

    @Override
    public void updateWantedPosition() {
        setArmPosition((int) SmartDashboard.getNumber("ManipulatorArm Position", 0));
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public LinkedHashMap<String, DoubleSupplier> getCSVProperties() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("position", this::getArmPosition);
        m.put("wanted_position", () -> mArmWantedPosition);
        return m;
    }

}
