package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.util.*;
import com.team175.robot.util.drivers.AldrinTalon;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import com.team175.robot.util.tuning.ClosedLoopGains;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * TODO: Bring up Arm to score position when current spike is detected when grabbing ball
 *
 * @author Arvind
 */
public final class Manipulator extends AldrinSubsystem implements ClosedLoopTunable {

    private final AldrinTalonSRX mArmMaster, mArmSlave;
    private final AldrinTalon mFrontRoller, mRearRoller;
    private final SimpleDoubleSolenoid mHatchPunch, mBrake, mDeploy;

    private int mArmWantedPosition;
    private ClosedLoopGains mArmForwardGains, mArmReverseGains;

    private static final int ALLOWED_POSITION_DEVIATION = 10;

    private static Manipulator sInstance;

    public static Manipulator getInstance() {
        if (sInstance == null) {
            sInstance = new Manipulator();
        }

        return sInstance;
    }

    private Manipulator() {
        // CTREFactory.getMasterTalon(portNum : int)
        mArmMaster = CTREFactory.getMasterTalon(Constants.MANIPULATOR_ARM_MASTER_PORT);
        mArmSlave = CTREFactory.getSlaveTalon(Constants.MANIPULATOR_ARM_SLAVE_PORT, mArmMaster);

        // Talon(portNum : int)
        mFrontRoller = new AldrinTalon(Constants.MANIPULATOR_FRONT_ROLLER);
        mRearRoller = new AldrinTalon(Constants.MANIPULATOR_REAR_ROLLER);

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int)
        mHatchPunch = new SimpleDoubleSolenoid(Constants.MANIPULATOR_HATCH_PUNCH_FORWARD_CHANNEL,
                Constants.MANIPULATOR_HATCH_PUNCH_REVERSE_CHANNEL);
        mBrake = new SimpleDoubleSolenoid(Constants.MANIPULATOR_BRAKE_FORWARD_CHANNEL, Constants.MANIPULATOR_BRAKE_REVERSE_CHANNEL);
        mDeploy = new SimpleDoubleSolenoid(Constants.MANIPULATOR_DEPLOY_FORWARD_CHANNEL, Constants.MANIPULATOR_DEPLOY_REVERSE_CHANNEL);

        // mArmWantedPosition = 0;
        mArmWantedPosition = ManipulatorArmPosition.SCORE.getPosition();

        RobotProfile profile = RobotManager.getProfile();
        CTREConfiguration.config(mArmMaster, profile.getManipulatorArmMasterConfig(), "ManipulatorArm");
        CTREConfiguration.config(mArmSlave, profile.getManipulatorArmSlaveConfig(), "ManipulatorArmSlave");
        mArmForwardGains = CTREConfiguration.getGains(profile.getManipulatorArmMasterConfig(), true);
        mArmReverseGains = CTREConfiguration.getGains(profile.getManipulatorArmMasterConfig(), false);

        mArmMaster.setBrakeMode(true);
        deploy(true);
        stop();
    }

    public void setBrake(boolean enable) {
        mBrake.set(!enable); // Brake solenoid is reversed
    }

    public boolean isBraked() {
        return !mBrake.get(); // Brake solenoid is reversed
    }

    public void deploy(boolean enable) {
        mDeploy.set(!enable);
    }

    public boolean isDeployed() {
        return !mDeploy.get();
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
        setRollerPosition(ManipulatorRollerPosition.IDLE);
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
        setBrake(false);
        mArmMaster.set(ControlMode.PercentOutput, power);
    }

    public void stopArm() {
        // setArmPosition(mArmWantedPosition);
        setArmPower(0);
        setBrake(true);
    }

    public double getArmPower() {
        return mArmMaster.getMotorOutputPercent();
    }

    public double getArmVoltage() {
        return mArmMaster.getMotorOutputVoltage();
    }

    public void setArmPosition(int position) {
        setBrake(false);
        mArmWantedPosition = position;

        if (mArmWantedPosition >= getArmPosition()) { // Going down
            mArmMaster.selectProfileSlot(Constants.PRIMARY_GAINS_SLOT, 0); // Forward Gains
        } else { // Going up
            mArmMaster.selectProfileSlot(Constants.AUX_GAINS_SLOT, 0); // Reverse Gains
        }

        mLogger.debug("Setting arm position to {}.", mArmWantedPosition);
        mLogger.debug("Current arm position: {}", getArmPosition());

        mArmMaster.set(ControlMode.MotionMagic, mArmWantedPosition);
    }

    public void setArmPosition(ManipulatorArmPosition ap) {
        // Un-deploy manipulator when going to stow position
        if (ap == ManipulatorArmPosition.STOW) {
            deploy(false);
        }

        setArmPosition(ap.getPosition());
    }

    public int getArmPosition() {
        return mArmMaster.getSelectedSensorPosition();
    }

    public int getArmVelocity() {
        return mArmMaster.getSelectedSensorVelocity();
    }

    public void setArmWantedPosition(int position) {
        mArmWantedPosition = position;
    }

    public boolean isArmAtWantedPosition() {
        return Math.abs(getArmPosition() - mArmWantedPosition) <= ALLOWED_POSITION_DEVIATION;
    }

    public void setArmForwardGains(ClosedLoopGains gains) {
        mArmForwardGains = gains;
        CTREConfiguration.setGains(mArmMaster, mArmForwardGains, true, "ManipulatorArm");
    }

    public void setArmReverseGains(ClosedLoopGains gains) {
        mArmReverseGains = gains;
        CTREConfiguration.setGains(mArmMaster, mArmReverseGains, false, "ManipulatorArm");
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        stopArm();
        stopRollers();
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();

        m.put("ManipArmFwdKp", () -> mArmForwardGains.getKp());
        m.put("ManipArmFwdKd", () -> mArmForwardGains.getKd());

        m.put("ManipArmRevKp", () -> mArmReverseGains.getKp());
        m.put("ManipArmRevKd", () -> mArmReverseGains.getKd());

        m.put("ManipArmKf", () -> mArmForwardGains.getKf());
        m.put("ManipArmAccel", () -> mArmForwardGains.getAcceleration());
        m.put("ManipArmCruiseVel", () -> mArmForwardGains.getCruiseVelocity());

        m.put("ManipArmWantedPos", () -> mArmWantedPosition);
        m.put("ManipArmPos", this::getArmPosition);
        m.put("ManipArmVel", this::getArmVelocity);
        m.put("ManipArmPower", this::getArmPower);
        m.put("ManipArmVolt", this::getArmVoltage);
        m.put("ManipArmCurrent", mArmMaster::getOutputCurrent);

        return m;
    }

    @Override
    public void updateFromDashboard() {
        setArmForwardGains(new ClosedLoopGains(
                SmartDashboard.getNumber("ManipArmFwdKp", 0),
                0,
                SmartDashboard.getNumber("ManipArmFwdKd", 0),
                SmartDashboard.getNumber("ManipArmKf", 0),
                (int) SmartDashboard.getNumber("ManipArmAccel", 0),
                (int) SmartDashboard.getNumber("ManipArmCruiseVel", 0)
        ));
        setArmReverseGains(new ClosedLoopGains(
                SmartDashboard.getNumber("ManipArmRevKp", 0),
                0,
                SmartDashboard.getNumber("ManipArmRevKd", 0),
                SmartDashboard.getNumber("ManipArmKf", 0),
                (int) SmartDashboard.getNumber("ManipArmAccel", 0),
                (int) SmartDashboard.getNumber("ManipArmCruiseVel", 0)
        ));
        setArmPosition((int) SmartDashboard.getNumber("ManipArmWantedPos", 0));
    }

    @Override
    public boolean checkSubsystem() {
        CTREDiagnostics diag = new CTREDiagnostics(mArmMaster, "ManipulatorArm");

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
    public void resetSensors() {
    }

    @Override
    public Map<String, Supplier> getCSVTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("arm_position", this::getArmPosition);
        m.put("arm_wanted_position", () -> mArmWantedPosition);
        m.put("arm_velocity", this::getArmVelocity);
        m.put("arm_voltage", this::getArmVoltage);
        m.put("arm_current", mArmMaster::getOutputCurrent);
        // m.put("rear_roller_current", mRearRoller::getPDPCurrent);
        return m;
    }

}
