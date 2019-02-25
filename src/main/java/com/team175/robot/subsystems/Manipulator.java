package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team175.robot.Constants;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.CTREDiagnostics;
import com.team175.robot.util.drivers.CTREFactory;
import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import com.team175.robot.util.tuning.ClosedLoopGains;
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
    private final AldrinTalonSRX mArmMaster, mArmSlave;
    private final Talon mFrontRoller, mRearRoller;
    private final SimpleDoubleSolenoid mHatchPunch, mBrake, mDeploy;

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
        mArmMaster = CTREFactory.getMasterTalon(Constants.MANIPULATOR_ARM_MASTER_PORT);
        mArmSlave = CTREFactory.getSlaveTalon(Constants.MANIPULATOR_ARM_SLAVE_PORT, mArmMaster);

        // Talon(portNum : int)
        mFrontRoller = new Talon(Constants.MANIPULATOR_FRONT_ROLLER);
        mRearRoller = new Talon(Constants.MANIPULATOR_REAR_ROLLER);

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int)
        mHatchPunch = new SimpleDoubleSolenoid(Constants.MANIPULATOR_HATCH_PUNCH_FORWARD_CHANNEL,
                Constants.MANIPULATOR_HATCH_PUNCH_REVERSE_CHANNEL);
        mBrake = new SimpleDoubleSolenoid(Constants.MANIPULATOR_BRAKE_FORWARD_CHANNEL, Constants.MANIPULATOR_BRAKE_REVERSE_CHANNEL);
        mDeploy = new SimpleDoubleSolenoid(Constants.MANIPULATOR_DEPLOY_FORWARD_CHANNEL, Constants.MANIPULATOR_DEPLOY_REVERSE_CHANNEL);

        mArmWantedPosition = 0;
        // mArmWantedPosition = ManipulatorArmPosition.STOW.positionToMove();
        mArmGains = Constants.MANIPULATOR_ARM_GAINS;

        /* Configuration */
        CTREDiagnostics.checkCommand(mArmMaster.configSelectedFeedbackSensor(FeedbackDevice.Analog),
                "Failed to config ManipulatorArm encoder!");
        setArmGains(mArmGains);
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
        mDeploy.set(enable);
    }

    public boolean isDeployed() {
        return mDeploy.get();
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
        mArmMaster.set(ControlMode.MotionMagic, mArmWantedPosition);
    }

    public void setArmPosition(ManipulatorArmPosition ap) {
        // Un-deploy manipulator when going to stow position
        if (ap == ManipulatorArmPosition.STOW) {
            deploy(false);
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
        return Math.abs(getArmPosition() - mArmWantedPosition) <= Constants.ALLOWED_POSITION_DEVIATION;
    }

    public void setArmGains(ClosedLoopGains gains) {
        mArmGains = gains;
        CTREDiagnostics.checkCommand(mArmMaster.configPIDF(mArmGains.getKp(), mArmGains.getKi(), mArmGains.getKd(), mArmGains.getKf()),
                "Failed to config Arm PID gains!");
        CTREDiagnostics.checkCommand(mArmMaster.configMotionAcceleration(mArmGains.getAcceleration()),
                "Failed to config Arm acceleration!");
        CTREDiagnostics.checkCommand(mArmMaster.configMotionCruiseVelocity(mArmGains.getCruiseVelocity()),
                "Failed to config Arm cruise velocity!");
    }

    @Override
    public void resetSensors() {
        // CTREDiagnostics.checkCommand(mArmMaster.setSelectedSensorPosition(0), "Failed to zero arm encoder!");
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
        m.put("ManipArmVolt", getArmVoltage());
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
