package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.positions.ManipulatorMode;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.util.*;
import com.team175.robot.util.drivers.AldrinTalon;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import com.team175.robot.util.tuning.ClosedLoopGains;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public final class Manipulator extends AldrinSubsystem implements ClosedLoopTunable {

    private final AldrinTalonSRX mArmMaster, mArmSlave;
    private final AldrinTalon mFrontRoller, mRearRoller;
    private final SimpleDoubleSolenoid mHatchPunch, mBrake, mDeploy;

    private int mArmWantedPosition;
    private ClosedLoopGains mArmForwardGains, mArmReverseGains;
    private ManipulatorMode mMode;
    private ManipulatorState mWantedState;

    private static final int ALLOWED_POSITION_DEVIATION = 3;

    private static Manipulator sInstance;

    private enum ManipulatorState {
        POSITION,
        HOLD_POSITION,
        MANUAL;
    }

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
        mFrontRoller = new AldrinTalon(Constants.MANIPULATOR_FRONT_ROLLER_PORT);
        mRearRoller = new AldrinTalon(Constants.MANIPULATOR_REAR_ROLLER_PORT);

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int)
        mHatchPunch = new SimpleDoubleSolenoid(Constants.MANIPULATOR_HATCH_PUNCH_FORWARD_CHANNEL,
                Constants.MANIPULATOR_HATCH_PUNCH_REVERSE_CHANNEL); // Punch solenoid also controls fingers
        mBrake = new SimpleDoubleSolenoid(Constants.MANIPULATOR_BRAKE_FORWARD_CHANNEL, Constants.MANIPULATOR_BRAKE_REVERSE_CHANNEL);
        mDeploy = new SimpleDoubleSolenoid(Constants.MANIPULATOR_DEPLOY_FORWARD_CHANNEL, Constants.MANIPULATOR_DEPLOY_REVERSE_CHANNEL);

        // mArmWantedPosition = 0;
        mArmWantedPosition = ManipulatorArmPosition.STOW.getPosition();
        mMode = ManipulatorMode.VELCRO_HATCH;
        mWantedState = ManipulatorState.MANUAL;

        RobotProfile profile = RobotManager.getProfile();
        CTREConfiguration.config(mArmMaster, profile.getManipulatorArmMasterConfig(), "ManipulatorArm");
        CTREConfiguration.config(mArmSlave, profile.getManipulatorArmSlaveConfig(), "ManipulatorArmSlave");
        mArmForwardGains = CTREConfiguration.getGains(profile.getManipulatorArmMasterConfig(), true);
        mArmReverseGains = CTREConfiguration.getGains(profile.getManipulatorArmMasterConfig(), false);

        mArmMaster.setBrakeMode(true);
        deploy(false);
        stop();

        super.logInstantiation();
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

    public void setFrontRollerPower(double power) {
        mFrontRoller.set(power);
    }

    public void setRearRollerPower(double power) {
        mRearRoller.set(power);
    }

    public void setRollerPower(double power) {
        setFrontRollerPower(power);
        setRearRollerPower(power);
    }

    public void setRollerPosition(ManipulatorRollerPosition position) {
        setFrontRollerPower(position.getFrontPower());
        setRearRollerPower(position.getRearPower());

        if (position == ManipulatorRollerPosition.SCORE_HATCH) {
            punchHatch(true);
        }
    }

    public void stopRollers() {
        setRollerPower(0);
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

    public synchronized void setArmPower(double power) {
        mWantedState = ManipulatorState.MANUAL;
        setBrake(false);
        mArmMaster.set(ControlMode.PercentOutput, power);
    }

    public void stopArm() {
        setArmPower(0);
        setBrake(true);
        mWantedState = ManipulatorState.HOLD_POSITION;
    }

    public double getArmPower() {
        return mArmMaster.getMotorOutputPercent();
    }

    public double getArmVoltage() {
        return mArmMaster.getMotorOutputVoltage();
    }

    public synchronized void setArmPosition(int position) {
        mWantedState = ManipulatorState.POSITION;
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

    public void setArmPosition(ManipulatorArmPosition position) {
        setArmPosition(position.getPosition());
    }

    public int getArmPosition() {
        return mArmMaster.getSelectedSensorPosition();
    }

    public int getArmVelocity() {
        return mArmMaster.getSelectedSensorVelocity();
    }

    public boolean isArmAtPosition(ManipulatorArmPosition position) {
        return Math.abs(getArmPosition() - position.getPosition()) <= ALLOWED_POSITION_DEVIATION;
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

    public void setMode(ManipulatorMode mode) {
        mMode = mode;
    }

    public ManipulatorMode getMode() {
        return mMode;
    }

    @Override
    public void start() {
        if (DriverStation.getInstance().isAutonomous()) {
            setArmPosition(ManipulatorArmPosition.STOW);
        } else {
            stopArm();
        }
    }

    @Override
    public void loop() {
        synchronized (this) {
            switch (mWantedState) {
                case POSITION:
                    if (isArmAtWantedPosition()) {
                        // TODO: Determine way to deploy manipulator on time
                        // Deploy manipulator after going to most positions
                        if (mArmWantedPosition != ManipulatorArmPosition.STOW.getPosition()
                                && mArmWantedPosition != ManipulatorArmPosition.FINGER_HATCH_PICKUP.getPosition()
                                && mArmWantedPosition != ManipulatorArmPosition.FINGER_HATCH_TILT.getPosition()) {
                            deploy(true);
                        }
                        stopArm();
                    } else {
                        // Stow manipulator before moving to stow or finger hatch pickup
                        if (mArmWantedPosition == ManipulatorArmPosition.STOW.getPosition()
                                || mArmWantedPosition == ManipulatorArmPosition.FINGER_HATCH_PICKUP.getPosition()
                                || mArmWantedPosition == ManipulatorArmPosition.FINGER_HATCH_TILT.getPosition()) {
                            deploy(false);
                        }
                        setBrake(false);
                    }
                    break;
                // TODO: Determine best way to auto correct arm
                case HOLD_POSITION:
                    if (isArmAtWantedPosition()) {
                        setArmPower(0);
                        setBrake(true);
                    } else {
                        setArmPosition(mArmWantedPosition);
                    }
                    break;
                case MANUAL:
                default:
                    mArmWantedPosition = getArmPosition();
                    break;
            }

            // outputToDashboard();

            /*
            if ballIsCollected {
                LED.getInstance().blinkColor(LEDColor.GRABBED);
            }
             */
        }
    }

    @Override
    public void stop() {
        stopArm();
        stopRollers();
        mWantedState = ManipulatorState.MANUAL;
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
