package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team175.robot.Constants;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.util.*;
import com.team175.robot.util.choosers.RobotChooser;
import com.team175.robot.util.drivers.AldrinTalon;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
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

    /* Declarations */
    private final AldrinTalonSRX mArmMaster, mArmSlave;
    private final AldrinTalon mFrontRoller, mRearRoller;
    private final SimpleDoubleSolenoid mHatchPunch, mBrake, mDeploy;

    private int mArmWantedPosition;
    private ClosedLoopGains mArmForwardGains, mArmReverseGains;

    private static Manipulator sInstance;

    private static final int ALLOWED_POSITION_DEVIATION = 10;

    public static Manipulator getInstance() {
        if (sInstance == null) {
            sInstance = new Manipulator();
        }

        return sInstance;
    }

    private Manipulator() {
        /* Instantiations */
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

        mArmWantedPosition = 0;
        // mArmWantedPosition = ManipulatorArmPosition.SCORE.positionToMove();

        /* Configuration */
        RobotProfile profile = RobotChooser.getInstance().getProfile();
        CTREConfiguration.config(mArmMaster, profile.getManipulatorArmMasterConfig(), "ManipulatorArm");
        CTREConfiguration.config(mArmSlave, profile.getManipulatorArmSlaveConfig(), "ManipulatorArmSlave");
        mArmForwardGains = CTREConfiguration.getGainsFromConfig(profile.getManipulatorArmMasterConfig(), true);
        mArmReverseGains = CTREConfiguration.getGainsFromConfig(profile.getManipulatorArmMasterConfig(), false);

        /*mArmGains = Constants.MANIPULATOR_ARM_GAINS;
        CTREDiagnostics.checkCommand(mArmMaster.configSelectedFeedbackSensor(FeedbackDevice.Analog),
                "Failed to config ManipulatorArm encoder!");
        setArmGains(mArmGains);*/

        mArmMaster.setBrakeMode(true);
        deploy(true);
        stop();
        // setBrake(true);
        // stopRollers(); // Maybe change to stop() if encoder works correctly
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
            Timer.delay(2 / 10);
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
            setArmGains(mArmForwardGains);
            // mArmMaster.selectProfileSlot(Constants.PRIMARY_GAINS_SLOT, 0);
        } else { // Going up
            setArmGains(mArmReverseGains);
            // mArmMaster.selectProfileSlot(Constants.AUX_GAINS_SLOT, 0);
        }

        mArmMaster.set(ControlMode.MotionMagic, mArmWantedPosition);
    }

    public void setArmPosition(ManipulatorArmPosition ap) {
        // Un-deploy manipulator when going to stow position
        if (ap == ManipulatorArmPosition.STOW) {
            deploy(false);
        } else {
            deploy(true);
        }

        setArmPosition(ap.positionToMove());
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
        CTREConfiguration.setPrimaryGains(mArmMaster, mArmForwardGains, "ManipulatorArm");
    }

    public void setArmReverseGains(ClosedLoopGains gains) {
        mArmReverseGains = gains;
        CTREConfiguration.setAuxGains(mArmMaster, mArmReverseGains, "ManipulatorArm");
    }

    public void setArmGains(ClosedLoopGains gains) {
        CTREDiagnostics.checkCommand(mArmMaster.configPIDF(gains.getKp(), gains.getKi(), gains.getKd(), gains.getKf()),
                "Failed to config Arm PID gains!");
        CTREDiagnostics.checkCommand(mArmMaster.configMotionAcceleration(gains.getAcceleration()),
                "Failed to config Arm acceleration!");
        CTREDiagnostics.checkCommand(mArmMaster.configMotionCruiseVelocity(gains.getCruiseVelocity()),
                "Failed to config Arm cruise velocity!");
    }

    @Override
    public void stop() {
        stopArm();
        stopRollers();
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();

        m.put("ManipArmForKp", () -> mArmForwardGains.getKp());
        m.put("ManipArmForKd", () -> mArmForwardGains.getKd());
        m.put("ManipArmForKf", () -> mArmForwardGains.getKf());
        m.put("ManipArmForAccel", () -> mArmForwardGains.getAcceleration());
        m.put("ManipArmForCruiseVel", () -> mArmForwardGains.getCruiseVelocity());

        m.put("ManipArmRevKp", () -> mArmReverseGains.getKp());
        m.put("ManipArmRevKd", () -> mArmReverseGains.getKd());
        m.put("ManipArmRevKf", () -> mArmReverseGains.getKf());
        m.put("ManipArmRevAccel", () -> mArmReverseGains.getAcceleration());
        m.put("ManipArmRevCruiseVel", () -> mArmReverseGains.getCruiseVelocity());

        m.put("ManipArmWantedPos", () -> mArmWantedPosition);
        m.put("ManipArmPos", this::getArmPosition);
        m.put("ManipArmPower", this::getArmPower);
        m.put("ManipArmVolt", this::getArmVoltage);

        return m;
    }

    @Override
    public void updateFromDashboard() {
        /*setArmGains(new ClosedLoopGains(SmartDashboard.getNumber("ManipArmKp", 0), 0,
                SmartDashboard.getNumber("ManipArmKd", 0),
                SmartDashboard.getNumber("ManipArmKf", 0),
                (int) SmartDashboard.getNumber("ManipArmAccel", 0),
                (int) SmartDashboard.getNumber("ManipArmCruiseVel", 0)));*/
        setArmForwardGains(new ClosedLoopGains(
                SmartDashboard.getNumber("ManipArmForKp", 0),
                0,
                SmartDashboard.getNumber("ManipArmForKd", 0),
                SmartDashboard.getNumber("ManipArmForKf", 0),
                (int) SmartDashboard.getNumber("ManipArmForAccel", 0),
                (int) SmartDashboard.getNumber("ManipArmForCruiseVel", 0)
        ));
        setArmReverseGains(new ClosedLoopGains(
                SmartDashboard.getNumber("ManipArmRevKp", 0),
                0,
                SmartDashboard.getNumber("ManipArmRevKd", 0),
                SmartDashboard.getNumber("ManipArmRevKf", 0),
                (int) SmartDashboard.getNumber("ManipArmRevAccel", 0),
                (int) SmartDashboard.getNumber("ManipArmRevCruiseVel", 0)
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
        // CTREDiagnostics.checkCommand(mArmMaster.setSelectedSensorPosition(0), "Failed to zero arm encoder!");
    }

    @Override
    public Map<String, Supplier> getCSVTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("position", this::getArmPosition);
        m.put("wanted_position", () -> mArmWantedPosition);
        return m;
    }

}
