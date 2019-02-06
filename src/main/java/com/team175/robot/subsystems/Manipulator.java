package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.team175.robot.Constants;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;

import com.team175.robot.util.ClosedLoopTunable;
import com.team175.robot.util.ClosedLoopGains;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import static java.util.Map.entry;

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

    private int mArmWantedPosition;
    private ClosedLoopGains mArmGains;

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
        mArmGains = Constants.MAINPULATOR_ARM_GAINS;
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

    public void stopArm() {
        setArmPower(0);
    }

    public void stopRollers() {
        setRollerPower(0, 0);
    }

    public void setArmGains(ClosedLoopGains gains) {
        mArmGains = gains;
        mArm.config_kP(mArmGains.getKp());
        mArm.config_kI(mArmGains.getKi());
        mArm.config_kD(mArmGains.getKd());
        mArm.config_kF(mArmGains.getKf());
        mArm.configMotionAcceleration(mArmGains.getAcceleration());
        mArm.configMotionCruiseVelocity(mArmGains.getCruiseVelocity());
    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public void stop() {
        stopArm();
        stopRollers();
    }

    public Map<String, Object> getTelemetry() {
        return Map.ofEntries(
                entry("ManipArmfKp", mArmGains.getKp()),
                entry("ManipArmKd", mArmGains.getKd()),
                entry("ManipArmKf", mArmGains.getKf()),
                entry("ManipArmAccel", mArmGains.getAcceleration()),
                entry("ManipArmCruiseVel", mArmGains.getCruiseVelocity()),
                entry("ManipArmWantedPos", mArmWantedPosition),
                entry("ManipArmPos", getArmPosition()),
                entry("ManipArmPower", getArmPower()),
                entry("ManipulatorArmVolt", getArmVoltage())
        );
    }

    public void outputToDashboard() {
        getTelemetry().forEach((k, v) -> {
            if (v instanceof Double || v instanceof Integer) {
                SmartDashboard.putNumber(k, (double) v);
            } else if (v instanceof Boolean) {
                SmartDashboard.putBoolean(k, (boolean) v);
            } else {
                SmartDashboard.putString(k, v.toString());
            }
        });
    }

    public void updateFromDashboard() {
        setArmGains(new ClosedLoopGains(SmartDashboard.getNumber("ManipArmKp", 0), 0,
                SmartDashboard.getNumber("ManipArmKd", 0),
                SmartDashboard.getNumber("ManipArmKf", 0),
                (int) SmartDashboard.getNumber("ManipArmAccel", 0),
                (int) SmartDashboard.getNumber("ManipArmCruiseVel", 0)));
        setArmPosition((int) SmartDashboard.getNumber("ManipArmWantedPos", 0));
    }

    public void updateGains() {
        outputToDashboard();
        updateFromDashboard();
    }

    @Override
    public Map<String, DoubleSupplier> getCSVTelemetry() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("position", this::getArmPosition);
        m.put("wanted_position", () -> mArmWantedPosition);
        return m;
    }

}
