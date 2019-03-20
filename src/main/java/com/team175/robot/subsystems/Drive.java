package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.Constants;
import com.team175.robot.commands.drive.ArcadeDrive;
import com.team175.robot.paths.Path;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.util.*;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.SimpleDoubleSolenoid;
import com.team175.robot.util.tuning.ClosedLoopGains;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * TODO: Add gyro PID tuning.
 *
 * @author Arvind
 */
public final class Drive extends AldrinSubsystem implements ClosedLoopTunable {

    private final AldrinTalonSRX mLeftMaster, mLeftSlave, mRightMaster, mRightSlave;
    private final PigeonIMU mPigeon;
    private final SimpleDoubleSolenoid mShift;
    private final PathHelper mPathHelper;
    private final DriveHelper mDriveHelper;

    private int mLeftWantedPosition, mRightWantedPosition;
    private double mWantedAngle;
    private ClosedLoopGains mLeftGains, mRightGains, mPigeonGains;
    private DriveState mWantedState;

    public static final double RAMP_TIME = 0;

    private static Drive sInstance;

    private enum DriveState {
        OPEN_LOOP,
        MOTION_MAGIC,
        TURN_TO_ANGLE,
        OFF;
    }

    public static Drive getInstance() {
        if (sInstance == null) {
            sInstance = new Drive();
        }

        return sInstance;
    }

    private Drive() {
        // CTREFactory.getMasterTalon(portNum : int)
        // CTREFactory.getSlaveTalon(portNum : int, master : BaseMotorController)
        mLeftMaster = CTREFactory.getMasterTalon(Constants.LEFT_MASTER_DRIVE_PORT);
        mLeftSlave = CTREFactory.getSlaveTalon(Constants.LEFT_SLAVE_DRIVE_PORT, mLeftMaster);
        mRightMaster = CTREFactory.getMasterTalon(Constants.RIGHT_MASTER_DRIVE_PORT);
        mRightSlave = CTREFactory.getSlaveTalon(Constants.RIGHT_SLAVE_DRIVE_PORT, mRightMaster);

        // PigeonIMU(talonSRX : TalonSRX)
        mPigeon = new PigeonIMU(mLeftSlave);

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int)
        mShift = new SimpleDoubleSolenoid(Constants.SHIFT_FORWARD_CHANNEL, Constants.SHIFT_REVERSE_CHANNEL);

        // PathHelper(master : TalonSRX, follower : TalonSRX, pigeon : PigeonIMU)
        mPathHelper = new PathHelper(mRightMaster, mLeftMaster, mPigeon);
        // DriveHelper(left : TalonSRX, right : TalonSRX)
        mDriveHelper = new DriveHelper(mLeftMaster, mRightMaster);

        mLeftWantedPosition = mRightWantedPosition = 0;
        mWantedAngle = 0;
        mWantedState = DriveState.OPEN_LOOP;

        RobotProfile profile = RobotManager.getProfile();
        CTREConfiguration.config(mLeftMaster, profile.getLeftMasterConfig(), "LeftMaster");
        CTREConfiguration.config(mLeftSlave, profile.getLeftSlaveConfig(), "LeftSlave");
        CTREConfiguration.config(mRightMaster, profile.getRightMasterConfig(), "RightMaster");
        CTREConfiguration.config(mRightSlave, profile.getRightSlaveConfig(), "RightSlave");
        mLeftGains = CTREConfiguration.getGains(profile.getLeftMasterConfig(), true);
        mRightGains = CTREConfiguration.getGains(profile.getRightMasterConfig(), true);
        mPigeonGains = CTREConfiguration.getGains(profile.getRightMasterConfig(), false);

        CTREDiagnostics.checkCommand(mLeftMaster.configOpenloopRamp(RAMP_TIME, Constants.TIMEOUT_MS),
                "Failed to config LeftMaster ramp!");
        CTREDiagnostics.checkCommand(mRightMaster.configOpenloopRamp(RAMP_TIME, Constants.TIMEOUT_MS),
                "Failed to config RightMaster ramp!");
        mPathHelper.configTalons();
        resetSensors();

        setHighGear(false);
        stop();
    }

    public void arcadeDrive(double throttle, double turn) {
        mDriveHelper.arcadeDrive(throttle, turn);
    }

    public void cheesyDrive(double throttle, double turn, boolean isQuickTurn) {
        mDriveHelper.cheesyDrive(throttle, turn, isQuickTurn, isHighGear());
    }

    public void straightDrive(double throttle) {
        mDriveHelper.straightDrive(throttle);
    }

    public void setLeftPower(double power) {
        mWantedState = DriveState.OPEN_LOOP;
        mLeftMaster.set(ControlMode.PercentOutput, power);
    }

    public void setRightPower(double power) {
        mWantedState = DriveState.OPEN_LOOP;
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
        mShift.set(enable);
    }

    public boolean isHighGear() {
        return mShift.get();
    }

    public void setBrakeMode(boolean enable) {
        mLeftMaster.setBrakeMode(enable);
        mRightMaster.setBrakeMode(enable);
    }

    public void setLeftPosition(int position) {
        mWantedState = DriveState.MOTION_MAGIC;
        mLeftWantedPosition = position;
        mLogger.debug("Setting left position to {}.", mLeftWantedPosition);
        mLogger.debug("Current left position: {}", getLeftPosition());
        mRightMaster.set(ControlMode.MotionMagic, mLeftWantedPosition);
    }

    public void setRightPosition(int position) {
        mWantedState = DriveState.MOTION_MAGIC;
        mRightWantedPosition = position;
        mLogger.debug("Setting right position to {}.", mRightWantedPosition);
        mLogger.debug("Current right position: {}", getRightPosition());
        mLeftMaster.set(ControlMode.MotionMagic, mRightWantedPosition);
    }

    public void setPosition(int position) {
        setLeftPosition(position);
        setRightPosition(position);
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
        CTREConfiguration.setGains(mLeftMaster, mLeftGains, true, "LeftMaster");
    }

    public void setRightGains(ClosedLoopGains gains) {
        mRightGains = gains;
        CTREConfiguration.setGains(mRightMaster, mRightGains, true,"RightMaster");
    }

    public void setPigeonGains(ClosedLoopGains gains) {
        mPigeonGains = gains;
        CTREConfiguration.setGains(mRightMaster, mPigeonGains, false, "RightMaster");
    }

    public void resetEncoders() {
        CTREDiagnostics.checkCommand(mLeftMaster.setSelectedSensorPosition(0), "Failed to zero LeftMaster encoder!");
        CTREDiagnostics.checkCommand(mRightMaster.setSelectedSensorPosition(0), "Failed to zero RightMaster encoder!");
        // CTREDiagnostics.checkCommand(mRightMaster.setSelectedSensorPosition(0, 1), "Failed to zero RightMaster aux sensor!");
    }

    public double getAngle() {
        double[] ypr = new double[3];
        mPigeon.getYawPitchRoll(ypr);
        return ypr[0];
    }

    public void resetGyro() {
        CTREDiagnostics.checkCommand(mPigeon.setYaw(0, Constants.TIMEOUT_MS), "Failed to zero Pigeon yaw!");
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        switch (mWantedState) {
            case OPEN_LOOP:
                break;
            case MOTION_MAGIC:
                break;
            case TURN_TO_ANGLE:
                break;
            case OFF:
                mLeftMaster.set(ControlMode.Disabled, 0);
                mRightMaster.set(ControlMode.Disabled, 0);
                break;
            default:
                break;
        }
        outputToDashboard();
    }

    @Override
    public void stop() {
        setPower(0);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ArcadeDrive());
        // setDefaultCommand(new CheesyDrive(false));
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();

        m.put("LDriveKp", () -> mLeftGains.getKp());
        m.put("LDriveKd", () -> mLeftGains.getKd());
        m.put("LDriveError", this::getLeftClosedLoopError);
        m.put("LDrivePos", this::getLeftPosition);
        m.put("LDriveVel", this::getLeftVelocity);
        m.put("LDrivePower", this::getLeftPower);
        m.put("LDriveWantedPos", () -> mLeftWantedPosition);

        m.put("RDriveKp", () -> mRightGains.getKp());
        m.put("RDriveKd", () -> mRightGains.getKd());
        m.put("RDriveError", this::getRightClosedLoopError);
        m.put("RDrivePos", this::getRightPosition);
        m.put("RDriveVel", this::getRightVelocity);
        m.put("RDrivePower", this::getRightPower);
        m.put("RDriveWantedPos", () -> mRightWantedPosition);

        m.put("DriveKf", () -> mRightGains.getKf());
        m.put("DriveAccel", () -> mRightGains.getAcceleration());
        m.put("DriveCruiseVel", () -> mRightGains.getCruiseVelocity());
        m.put("DriveWantedAngle", () -> mWantedAngle);
        m.put("GyroAngle", this::getAngle);
        m.put("DriveIsHighGear", this::isHighGear);

        return m;
    }

    @Override
    public void updateFromDashboard() {
        setLeftGains(new ClosedLoopGains(
                SmartDashboard.getNumber("LDriveKp", 0),
                0,
                SmartDashboard.getNumber("LDriveKd", 0),
                SmartDashboard.getNumber("DriveKf", 0),
                (int) SmartDashboard.getNumber("DriveAccel", 0),
                (int) SmartDashboard.getNumber("DriveCruiseVel", 0)
        ));
        setRightGains(new ClosedLoopGains(
                SmartDashboard.getNumber("RDriveKp", 0),
                0,
                SmartDashboard.getNumber("RDriveKd", 0),
                SmartDashboard.getNumber("DriveKf", 0),
                (int) SmartDashboard.getNumber("DriveAccel", 0),
                (int) SmartDashboard.getNumber("DriveCruiseVel", 0)
        ));
        setLeftPosition((int) SmartDashboard.getNumber("LDriveWantedPos", 0));
        setRightPosition((int) SmartDashboard.getNumber("RDriveWantedPos", 0));
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
    }

    @Override
    public void resetSensors() {
        resetEncoders();
        resetGyro();
    }

    @Override
    public Map<String, Supplier> getCSVTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("drive_left_position", this::getLeftPosition);
        m.put("drive_right_position", this::getRightPosition);
        m.put("drive_left_velocity", this::getLeftVelocity);
        m.put("drive_right_velocity", this::getRightVelocity);
        m.put("drive_left_wanted_position", () -> mLeftWantedPosition);
        m.put("drive_right_wanted_position", () -> mRightWantedPosition);
        return m;
    }

}
