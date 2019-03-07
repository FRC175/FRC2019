package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.Constants;
import com.team175.robot.commands.drive.ArcadeDrive;
import com.team175.robot.paths.Path;
import com.team175.robot.profiles.RobotProfile;
import com.team175.robot.util.*;
import com.team175.robot.util.RobotManager;
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

    /* Declarations */
    private final AldrinTalonSRX mLeftMaster, mLeftSlave, mRightMaster, mRightSlave;
    private final PigeonIMU mPigeon;
    private final SimpleDoubleSolenoid mShift;
    private final PathHelper mPathHelper;
    private final DriveHelper mDriveHelper;

    private int mWantedPosition;
    private double mWantedAngle;
    private ClosedLoopGains mLeftGains, mRightGains, mPigeonGains;

    private static Drive sInstance;

    public static final double RAMP_TIME = 0;

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
        mPigeon = new PigeonIMU(mLeftSlave);

        // SimpleDoubleSolenoid(forwardChannel : int, reverseChannel : int)
        mShift = new SimpleDoubleSolenoid(Constants.SHIFT_FORWARD_CHANNEL, Constants.SHIFT_REVERSE_CHANNEL);

        // PathHelper(master : TalonSRX, follower : TalonSRX, pigeon : PigeonIMU)
        mPathHelper = new PathHelper(mRightMaster, mLeftMaster, mPigeon);
        // DriveHelper(left : TalonSRX, right : TalonSRX)
        mDriveHelper = new DriveHelper(mLeftMaster, mRightMaster);

        mWantedPosition = 0;
        mWantedAngle = 0.0;

        /* Configuration */
        RobotProfile profile = RobotManager.getInstance().getProfile();
        CTREConfiguration.config(mLeftMaster, profile.getLeftMasterConfig(), "LeftMaster");
        CTREConfiguration.config(mLeftSlave, profile.getLeftSlaveConfig(), "LeftSlave");
        CTREConfiguration.config(mRightMaster, profile.getRightMasterConfig(), "RightMaster");
        CTREConfiguration.config(mRightSlave, profile.getRightSlaveConfig(), "RightSlave");
        mLeftGains = CTREConfiguration.getGains(profile.getLeftMasterConfig(), true);
        mRightGains = CTREConfiguration.getGains(profile.getRightMasterConfig(), true);
        mPigeonGains = CTREConfiguration.getGains(profile.getRightMasterConfig(), false);

        /*mLeftGains = Constants.LEFT_DRIVE_GAINS;
        mRightGains = Constants.RIGHT_DRIVE_GAINS;
        CTREDiagnostics.checkCommand(mLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder),
                "Failed to config LeftMaster encoder!");
        CTREDiagnostics.checkCommand(mRightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder),
                "Failed to config RightMaster encoder!");
        setLeftGains(mLeftGains);
        setRightGains(mRightGains);
        mLeftMaster.setInverted(false);
        mLeftSlave.setInverted(false);
        mRightMaster.setInverted(true);
        mRightSlave.setInverted(true);*/

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
        /*CTREDiagnostics.checkCommand(mLeftMaster.configPIDF(mLeftGains.getKp(), mLeftGains.getKi(), mLeftGains.getKd(),
                mLeftGains.getKf()), "Failed to config LeftMaster PID gains!");
        CTREDiagnostics.checkCommand(mLeftMaster.configMotionAcceleration(mLeftGains.getAcceleration()),
                "Failed to config LeftMaster acceleration!");
        CTREDiagnostics.checkCommand(mLeftMaster.configMotionCruiseVelocity(mLeftGains.getCruiseVelocity()),
                "Failed to config LeftMaster cruise velocity!");*/
        mLeftGains = gains;
        CTREConfiguration.setGains(mLeftMaster, mLeftGains, true, "LeftMaster");
    }

    public void setRightGains(ClosedLoopGains gains) {
        /*CTREDiagnostics.checkCommand(mRightMaster.configPIDF(mRightGains.getKp(), mRightGains.getKi(), mRightGains.getKd(),
                mRightGains.getKf()), "Failed to config RightMaster PID gains!");
        CTREDiagnostics.checkCommand(mRightMaster.configMotionAcceleration(mRightGains.getAcceleration()),
                "Failed to config RightMaster acceleration!");
        CTREDiagnostics.checkCommand(mRightMaster.configMotionCruiseVelocity(mRightGains.getCruiseVelocity()),
                "Failed to config RightMaster cruise velocity!");*/
        mRightGains = gains;
        CTREConfiguration.setGains(mRightMaster, mRightGains, true,"RightMaster");
    }

    public void setPigeonGains(ClosedLoopGains gains) {
        /*mRightMaster.configAuxPIDF(mPigeonGains.getKp(), mPigeonGains.getKi(), mPigeonGains.getKd(), mPigeonGains.getKf());*/
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
    protected void initDefaultCommand() {
        setDefaultCommand(new ArcadeDrive());
        // setDefaultCommand(new CheesyDrive(false));
    }

    @Override
    public void stop() {
        setPower(0);
    }

    @Override
    public Map<String, Supplier> getTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();

        m.put("LDriveKp", () -> mLeftGains.getKp());
        m.put("LDriveKd", () -> mLeftGains.getKd());
        m.put("LDriveError", this::getLeftClosedLoopError);
        m.put("LDrivePos", this::getLeftPosition);
        m.put("LDrivePower", this::getLeftPower);

        m.put("RDriveKp", () -> mRightGains.getKp());
        m.put("RDriveKd", () -> mRightGains.getKd());
        m.put("RDriveError", this::getRightClosedLoopError);
        m.put("RDrivePos", this::getRightPosition);
        m.put("RDrivePower", this::getRightPower);

        m.put("DriveKf", () -> mRightGains.getKf());
        m.put("DriveAccel", () -> mRightGains.getAcceleration());
        m.put("DriveCruiseVel", () -> mRightGains.getCruiseVelocity());

        m.put("DriveWantedPos", () -> mWantedPosition);
        m.put("DriveWantedAngle", () -> mWantedAngle);
        m.put("DriveIsHighGear", this::isHighGear);
        m.put("GyroAngle", this::getAngle);

        return m;
    }

    @Override
    public void updateFromDashboard() {
        setLeftGains(new ClosedLoopGains(SmartDashboard.getNumber("LDriveKp", 0),
                0,
                SmartDashboard.getNumber("LDriveKd", 0),
                SmartDashboard.getNumber("DriveKf", 0),
                (int) SmartDashboard.getNumber("DriveAccel", 0),
                (int) SmartDashboard.getNumber("DriveCruiseVel", 0)));
        setRightGains(new ClosedLoopGains(SmartDashboard.getNumber("RDriveKp", 0),
                0,
                SmartDashboard.getNumber("RDriveKd", 0),
                SmartDashboard.getNumber("DriveKf", 0),
                (int) SmartDashboard.getNumber("DriveAccel", 0),
                (int) SmartDashboard.getNumber("DriveCruiseVel", 0)));
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
    public void resetSensors() {
        resetEncoders();
        resetGyro();
    }

    @Override
    public Map<String, Supplier> getCSVTelemetry() {
        LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
        m.put("left_position", this::getLeftPosition);
        m.put("right_position", this::getRightPosition);
        m.put("wanted_position", () -> mWantedPosition);
        m.put("left_velocity", this::getLeftVelocity);
        m.put("right_velocity", this::getRightVelocity);
        return m;
    }

}
