package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team175.robot.Constants;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.CTREDiagnostics;
import com.team175.robot.util.drivers.CTREFactory;

import com.team175.robot.util.tuning.ClosedLoopTunable;
import com.team175.robot.util.tuning.ClosedLoopGains;
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
public final class Manipulator extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private final AldrinTalonSRX mArm;
    private final Talon mFrontRoller, mRearRoller;
    private final Solenoid mHatchPunch, mBrake;
    private final DoubleSolenoid mDeploy;

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
        mHatchPunch = new Solenoid(Constants.MANIPULATOR_HATCH_PUNCH_CHANNEL);
        mBrake = new Solenoid(Constants.MANIPULATOR_BRAKE_CHANNEL);

        // DoubleSolenoid(forwardChannel : int, reverseChannel : int)
        mDeploy = new DoubleSolenoid(Constants.MANIPULATOR_DEPLOY_FORWARD_CHANNEL,
                Constants.MANIPULATOR_DEPLOY_REVERSE_CHANNEL);

        mArmWantedPosition = 0;

        /* Configuration */
        CTREDiagnostics.checkCommand(mArm.configSelectedFeedbackSensor(FeedbackDevice.Analog),
                "Failed to config ManipulatorArm encoder!");
        mArmGains = Constants.MANIPULATOR_ARM_GAINS;
        stopRollers(); // Maybe change to stop() if encoder works correctly
    }

    public void setBrake(boolean enable) {
        mBrake.set(enable);
    }

    public boolean isBraked() {
        return mBrake.get();
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

    public void setRollerPosition(ManipulatorRollerPosition rp) {
        setRollerPower(rp.getFrontPower(), rp.getRearPower());

        if (rp == ManipulatorRollerPosition.SCORE_HATCH) {
            punchHatch(true);
        }
    }

    public void stopRollers() {
        setRollerPower(0, 0);
        punchHatch(false);
    }

    public double getFrontRollerPower() {
        return mFrontRoller.get();
    }

    public double getRearRollerPower() {
        return mRearRoller.get();
    }

    public void punchHatch(boolean enable) {
        mHatchPunch.set(enable);
    }

    public boolean isHatchPunched() {
        return mHatchPunch.get();
    }

    public void setArmPower(double power) {
        mArm.set(ControlMode.PercentOutput, power);
    }

    public void stopArm() {
        setArmPosition(mArmWantedPosition);
        setBrake(true);
        // setArmPower(0);
    }

    public double getArmPower() {
        return mArm.getMotorOutputPercent();
    }

    public double getArmVoltage() {
        return mArm.getMotorOutputVoltage();
    }

    public void setArmPosition(int position) {
        setBrake(false);
        mArmWantedPosition = position;
        mArm.set(ControlMode.MotionMagic, mArmWantedPosition);
    }

    public void setArmPosition(ManipulatorArmPosition ap) {
        setArmPosition(ap.positionToMove());
    }

    public int getArmPosition() {
        return mArm.getSelectedSensorPosition();
    }

    public int getArmVelocity() {
        return mArm.getSelectedSensorVelocity();
    }

    public boolean isArmAtWantedPosition() {
        return Math.abs(getArmPosition() - mArmWantedPosition) <= Constants.ALLOWED_POSITION_DEVIATION;
    }

    public void setArmGains(ClosedLoopGains gains) {
        mArmGains = gains;
        CTREDiagnostics.checkCommand(mArm.configPIDF(mArmGains.getKp(), mArmGains.getKi(), mArmGains.getKd(), mArmGains.getKf()),
                "Failed to config Arm PID gains!");
        CTREDiagnostics.checkCommand(mArm.configMotionAcceleration(mArmGains.getAcceleration()),
                "Failed to config Arm acceleration!");
        CTREDiagnostics.checkCommand(mArm.configMotionCruiseVelocity(mArmGains.getCruiseVelocity()),
                "Failed to config Arm cruise velocity!");
    }

    @Override
    public void resetSensors() {
        CTREDiagnostics.checkCommand(mArm.setSelectedSensorPosition(0), "Failed to zero arm encoder!");
    }

    @Override
    public void stop() {
        stopArm();
        stopRollers();
    }

    @Override
    public Map<String, Object> getTelemetry() {
        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("ManipArmKp", mArmGains.getKp());
        m.put("ManipArmKd", mArmGains.getKd());
        m.put("ManipArmKf", mArmGains.getKf());
        m.put("ManipArmAccel", mArmGains.getAcceleration());
        m.put("ManipArmCruiseVel", mArmGains.getCruiseVelocity());
        m.put("ManipArmWantedPos", mArmWantedPosition);
        m.put("ManipArmPos", getArmPosition());
        m.put("ManipArmPower", getArmPower());
        m.put("ManipulatorArmVolt", getArmVoltage());
        return m;
    }

    @Override
    public void updateFromDashboard() {
        setArmGains(new ClosedLoopGains(SmartDashboard.getNumber("ManipArmKp", 0), 0,
                SmartDashboard.getNumber("ManipArmKd", 0),
                SmartDashboard.getNumber("ManipArmKf", 0),
                (int) SmartDashboard.getNumber("ManipArmAccel", 0),
                (int) SmartDashboard.getNumber("ManipArmCruiseVel", 0)));
        setArmPosition((int) SmartDashboard.getNumber("ManipArmWantedPos", 0));
    }

    @Override
    public boolean checkSubsystem() {
        CTREDiagnostics diag = new CTREDiagnostics(mArm, "ManipulatorArm");

        mLogger.info("Beginning diagnostics test for ManipulatorArm!");
        boolean isGood = diag.checkMotorController();
        mLogger.info(diag.toString());

        if (!isGood) {
            mLogger.error("ManipulatorArm failed diagnostics test!");
        } else {
            mLogger.info("ManipulatorArm passed diagnostics test!");
        }

        return isGood;
    }

    @Override
    public void updateGains() {
        updateFromDashboard();
        mLogger.debug("Wanted Position: {}", mArmWantedPosition);
        mLogger.debug("Current Position: {}", getArmPosition());
    }

    @Override
    public void reset() {
        resetSensors();
    }

    @Override
    public Map<String, DoubleSupplier> getCSVTelemetry() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("position", this::getArmPosition);
        m.put("wanted_position", () -> mArmWantedPosition);
        return m;
    }

}
