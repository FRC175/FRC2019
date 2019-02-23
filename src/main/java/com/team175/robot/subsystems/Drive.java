package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.Constants;
import com.team175.robot.commands.CheesyDrive;
import com.team175.robot.paths.Path;
import com.team175.robot.util.DriveHelper;
import com.team175.robot.util.PathHelper;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.CTREDiagnostics;
import com.team175.robot.util.drivers.CTREFactory;
import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import com.team175.robot.util.tuning.ClosedLoopGains;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * TODO: Add gyro PID tuning.
 *
 * @author Arvind
 */
public final class Drive extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private final AldrinTalonSRX mLeftMaster, mLeftSlave, mRightMaster, mRightSlave;
    private final PigeonIMU mPigeon;
    private final SimpleDoubleSolenoid mShift;
    private final PathHelper mPathHelper;
    private final DriveHelper mDriveHelper;

    private int mWantedPosition;
    private double mWantedAngle;
    private ClosedLoopGains mLeftGains, mRightGains, mPigeonGains;

    // Singleton Instance
    private static Drive sInstance;

    public static Drive getInstance() {
        if (sInstance == null) {
            sInstance = new Drive();
        }

        return sInstance;
    }

    private Drive() {
        /* Instantiations */
        // CTREFactory.getMasterTalon(portNum : int)
        // CTREFactory.getSlaveTalon(portNum : int, master : BaseMotorController)
        mLeftMaster = CTREFactory.getMasterTalon(Constants.LEFT_MASTER_DRIVE_PORT);
        mLeftSlave = CTREFactory.getSlaveTalon(Constants.LEFT_SLAVE_DRIVE_PORT, mLeftMaster);
        mRightMaster = CTREFactory.getMasterTalon(Constants.RIGHT_MASTER_DRIVE_PORT);
        mRightSlave = CTREFactory.getSlaveTalon(Constants.RIGHT_SLAVE_DRIVE_PORT, mRightMaster);

        // PigeonIMU(talonSRX : TalonSRX)
        // mPigeon = new PigeonIMU(mRightSlave);
        mPigeon = null;

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int)
        mShift = new SimpleDoubleSolenoid(Constants.SHIFT_FORWARD_CHANNEL, Constants.SHIFT_REVERSE_CHANNEL);

        // PathHelper(master : TalonSRX, follower : TalonSRX, pigeon : PigeonIMU)
        // mPathHelper = new PathHelper(mRightMaster, mLeftMaster, mPigeon);
        mPathHelper = null;
        // DriveHelper(left : TalonSRX, right : TalonSRX)
        mDriveHelper = new DriveHelper(mLeftMaster, mRightMaster);

        mWantedPosition = 0;
        mWantedAngle = 0.0;
        mLeftGains = Constants.LEFT_DRIVE_GAINS;
        mRightGains = Constants.RIGHT_DRIVE_GAINS;

        /* Configuration */
        CTREDiagnostics.checkCommand(mLeftMaster.configOpenloopRamp(Constants.RAMP_TIME, Constants.TIMEOUT_MS),
                "Failed to config LeftMaster ramp!");
        CTREDiagnostics.checkCommand(mRightMaster.configOpenloopRamp(Constants.RAMP_TIME, Constants.TIMEOUT_MS),
                "Failed to config RightMaster ramp!");
        CTREDiagnostics.checkCommand(mLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder),
                "Failed to config LeftMaster encoder!");
        CTREDiagnostics.checkCommand(mRightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder),
                "Failed to config RightMaster encoder!");
        mLeftMaster.setInverted(false);
        mLeftSlave.setInverted(false);
        mRightMaster.setInverted(true);
        mRightSlave.setInverted(true);
        setLeftGains(mLeftGains);
        setRightGains(mRightGains);
        setHighGear(false);
        // mPathHelper.configTalons();
        resetSensors();
        stop();
    }

    public void arcadeDrive(double throttle, double turn) {
        mDriveHelper.arcadeDrive(throttle, turn);
        // mDriveHelper.altArcadeDrive(throttle, turn);
        // mDriveHelper.wpiArcadeDrive(throttle, turn);
    }

    public void cheesyDrive(double throttle, double turn, boolean isQuickTurn) {
        mDriveHelper.cheesyDrive(throttle, turn, isQuickTurn, isHighGear());
    }

    public void straightDrive(double throttle) {
        mDriveHelper.straightDrive(throttle);
    }

    public void setLeftPower(double power) {
        mLeftMaster.set(ControlMode.PercentOutput, power);
    }

    public void setRightPower(double power) {
        mRightMaster.set(ControlMode.PercentOutput, power);
    }

    public void setPower(double power) {
        setLeftPower(power);
        setRightPower(power);
    }

    public double getLeftPower() {
        return mLeftMaster.getMotorOutputPercent();
    }

    public double getRightPower() {
        return mRightMaster.getMotorOutputPercent();
    }

    public void setHighGear(boolean enable) {
        mShift.set(!enable); // Shift is in high gear by default
    }

    public boolean isHighGear() {
        return !mShift.get(); // Shift is in high gear by default
    }

    public void setBrakeMode(boolean enable) {
        mLeftMaster.setBrakeMode(enable);
        mRightMaster.setBrakeMode(enable);
    }

    public void setLeftPosition(int position) {
        mRightMaster.set(ControlMode.MotionMagic, position);
    }

    public void setRightPosition(int position) {
        mLeftMaster.set(ControlMode.MotionMagic, position);
    }

    public void setPosition(int position) {
        mWantedPosition = position;
        setLeftPosition(mWantedPosition);
        setRightPosition(mWantedPosition);
    }

    public int getLeftPosition() {
        return mLeftMaster.getSelectedSensorPosition();
    }

    public int getLeftVelocity() {
        return mLeftMaster.getSelectedSensorVelocity();
    }

    public int getLeftClosedLoopError() {
        return mLeftMaster.getClosedLoopError();
    }

    public int getRightPosition() {
        return mRightMaster.getSelectedSensorPosition();
    }

    public int getRightVelocity() {
        return mRightMaster.getSelectedSensorVelocity();
    }

    public int getRightClosedLoopError() {
        return mLeftMaster.getClosedLoopError();
    }

    public void setPath(Path path) {
        mPathHelper.reset();
        mPathHelper.follow(path);
    }

    public void stopPathFollowing() {
        mPathHelper.stop();
    }

    public boolean isPathFinished() {
        return mPathHelper.isFinished();
    }

    public void setLeftGains(ClosedLoopGains gains) {
        mLeftGains = gains;
        CTREDiagnostics.checkCommand(mLeftMaster.configPIDF(mLeftGains.getKp(), mLeftGains.getKi(), mLeftGains.getKd(),
                mLeftGains.getKf()), "Failed to config LeftMaster PID gains!");
        CTREDiagnostics.checkCommand(mLeftMaster.configMotionAcceleration(mLeftGains.getAcceleration()),
                "Failed to config LeftMaster acceleration!");
        CTREDiagnostics.checkCommand(mLeftMaster.configMotionCruiseVelocity(mLeftGains.getCruiseVelocity()),
                "Failed to config LeftMaster cruise velocity!");
    }

    public void setRightGains(ClosedLoopGains gains) {
        mRightGains = gains;
        CTREDiagnostics.checkCommand(mRightMaster.configPIDF(mRightGains.getKp(), mRightGains.getKi(), mRightGains.getKd(),
                mRightGains.getKf()), "Failed to config RightMaster PID gains!");
        CTREDiagnostics.checkCommand(mRightMaster.configMotionAcceleration(mRightGains.getAcceleration()),
                "Failed to config RightMaster acceleration!");
        CTREDiagnostics.checkCommand(mRightMaster.configMotionCruiseVelocity(mRightGains.getCruiseVelocity()),
                "Failed to config RightMaster cruise velocity!");
    }

    public void setPigeonGains(ClosedLoopGains gains) {
        mPigeonGains = gains;
        mRightMaster.configAuxPIDF(mPigeonGains.getKp(), mPigeonGains.getKi(), mPigeonGains.getKd(), mPigeonGains.getKf());
    }

    @Override
    public void resetSensors() {
        CTREDiagnostics.checkCommand(mLeftMaster.setSelectedSensorPosition(0), "Failed to zero LeftMaster encoder!");
        CTREDiagnostics.checkCommand(mRightMaster.setSelectedSensorPosition(0), "Failed to zero RightMaster encoder!");
        // CTREDiagnostics.checkCommand(mPigeon.setYaw(0, Constants.TIMEOUT_MS), "Failed to zero Pigeon yaw!");
    }

    @Override
    protected void initDefaultCommand() {
        // setDefaultCommand(new ArcadeDrive(false));
        setDefaultCommand(new CheesyDrive(false));
    }

    @Override
    public void stop() {
        setPower(0);
    }

    @Override
    public Map<String, Object> getTelemetry() {
        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("LDriveKp", mLeftGains.getKp());
        m.put("LDriveKd", mLeftGains.getKd());
        m.put("LDriveKf", mLeftGains.getKf());
        m.put("LDriveAccel", mLeftGains.getAcceleration());
        m.put("LDriveCruiseVel", mLeftGains.getCruiseVelocity());
        m.put("LDriveError", getLeftClosedLoopError());
        m.put("LDrivePos", getLeftPosition());
        m.put("LDrivePower", getLeftPower());
        m.put("RDriveKp", mRightGains.getKp());
        m.put("RDriveKd", mRightGains.getKd());
        m.put("RDriveKf", mRightGains.getKf());
        m.put("RDriveAccel", mRightGains.getAcceleration());
        m.put("RDriveCruiseVel", mRightGains.getCruiseVelocity());
        m.put("RDriveError", getRightClosedLoopError());
        m.put("RDrivePos", getRightPosition());
        m.put("RDrivePower", getRightPower());
        m.put("DriveWantedPos", mWantedPosition);
        m.put("DriveWantedAngle", mWantedAngle);
        m.put("DriveIsLowGear", isHighGear());
        return m;
    }

    @Override
    public void updateFromDashboard() {
        setLeftGains(new ClosedLoopGains(SmartDashboard.getNumber("LDriveKp", 0), 0,
                SmartDashboard.getNumber("LDriveKd", 0),
                SmartDashboard.getNumber("LDriveKf", 0),
                (int) SmartDashboard.getNumber("LDriveAccel", 0),
                (int) SmartDashboard.getNumber("LDriveCruiseVel", 0)));
        setRightGains(new ClosedLoopGains(SmartDashboard.getNumber("RDriveKp", 0), 0,
                SmartDashboard.getNumber("RDriveKd", 0),
                SmartDashboard.getNumber("RDriveKf", 0),
                (int) SmartDashboard.getNumber("RDriveAccel", 0),
                (int) SmartDashboard.getNumber("RDriveCruiseVel", 0)));
        setPosition((int) SmartDashboard.getNumber("DriveWantedPos", 0));
    }

    @Override
    public boolean checkSubsystem() {
        LinkedHashMap<String, TalonSRX> talons = new LinkedHashMap<>(2);
        talons.put("LeftMaster", mLeftMaster);
        talons.put("RightMaster", mLeftMaster);

        List<CTREDiagnostics> diags = new ArrayList<>(2);
        talons.forEach((k, v) -> diags.add(new CTREDiagnostics(v, k)));

        mLogger.info("Beginning diagnostics test for Drive subsystem.");

        boolean isGood = true;
        for (CTREDiagnostics cd : diags) {
            isGood &= cd.checkMotorController();
            mLogger.info(cd.toString());
        }

        if (!isGood) {
            mLogger.error("Drive subsystem failed diagnostics test!");
        } else {
            mLogger.info("Drive subsystem passed diagnostics test!");
        }

        return isGood;
    }

    @Override
    public void updateGains() {
        updateFromDashboard();
        mLogger.debug("Wanted Position: {}", mWantedPosition);
        mLogger.debug("Current Left Position: {}", getLeftPosition());
        mLogger.debug("Current Right Position: {}", getRightPosition());
    }

    @Override
    public void reset() {
        resetSensors();
    }

    @Override
    public Map<String, DoubleSupplier> getCSVTelemetry() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("left_position", this::getLeftPosition);
        m.put("right_position", this::getRightPosition);
        m.put("wanted_position", () -> mWantedPosition);
        m.put("left_velocity", this::getLeftVelocity);
        m.put("right_velocity", this::getRightVelocity);
        return m;
    }

}
