package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.Constants;
import com.team175.robot.auto.Trajectory;
import com.team175.robot.auto.TrajectoryFollower;
import com.team175.robot.commands.ManualArcadeDrive;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.CTREFactory;

import com.team175.robot.util.tuning.ClosedLoopTunable;
import com.team175.robot.util.tuning.ClosedLoopGains;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * TODO: Maybe implement cheesy drive.
 * TODO: Add gyro PID tuning.
 * TODO: Add motion profiling config.
 *
 * @author Arvind
 */
public final class Drive extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private final AldrinTalonSRX mLeftMaster, mLeftSlave, mRightMaster, mRightSlave;
    private final PigeonIMU mPigeon;
    private final Solenoid mShift;
    private final TrajectoryFollower mTrajectoryFollower;

    private int mWantedPosition;
    private double mWantedYaw;
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
        mPigeon = new PigeonIMU(mRightSlave);

        // Solenoid(channel : int)
        mShift = new Solenoid(Constants.SHIFT_CHANNEL);

        mTrajectoryFollower = new TrajectoryFollower(mRightMaster, mLeftMaster, mPigeon);

        mWantedPosition = 0;
        mWantedYaw = 0.0;
        mLeftGains = Constants.LEFT_DRIVE_GAINS;
        mRightGains = Constants.RIGHT_DRIVE_GAINS;

        setLeftGains(mLeftGains);
        setRightGains(mRightGains);

        /*// Configure left polling rate at 5 ms
        mLeftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.TIMEOUT_MS);
        mLeftMaster.setSensorPhase(true);

        // Config right sensor to be average of left and right
        mRightMaster.configRemoteFeedbackFilter(mLeftMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor,
                Constants.SLOT_INDEX, Constants.TIMEOUT_MS);
        mRightMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, Constants.TIMEOUT_MS);
        mRightMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, Constants.TIMEOUT_MS);
        mRightMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, Constants.SLOT_INDEX, Constants.TIMEOUT_MS);
        mRightMaster.configSelectedFeedbackCoefficient(0.5, Constants.SLOT_INDEX, Constants.TIMEOUT_MS);

        // Config right secondary sensor to be pigeon
        mRightMaster.configRemoteFeedbackFilter(mPigeon.getDeviceID(), RemoteSensorSource.Pigeon_Yaw,
                Constants.AUX_SLOT_INDEX, Constants.TIMEOUT_MS);
        mRightMaster.configSelectedFeedbackCoefficient((3600.0 / 8192.0), Constants.AUX_SLOT_INDEX, Constants.TIMEOUT_MS);*/
    }

    public void arcadeDrive(double y, double x) {
        mLeftMaster.set(ControlMode.PercentOutput, y, DemandType.ArbitraryFeedForward, x);
        mRightMaster.set(ControlMode.PercentOutput, y, DemandType.ArbitraryFeedForward, x);
    }

    public void setPower(double leftPower, double rightPower) {
        mLeftMaster.set(ControlMode.PercentOutput, leftPower);
        mRightMaster.set(ControlMode.PercentOutput, rightPower);
    }

    public double getLeftPower() {
        return mLeftMaster.getMotorOutputPercent();
    }

    public double getRightPower() {
        return mRightMaster.getMotorOutputPercent();
    }

    public void setLowGear(boolean enable) {
        mShift.set(enable);
    }

    public boolean isLowGear() {
        return mShift.get();
    }

    public void setBrakeMode(boolean enable) {
        mLeftMaster.setBrakeMode(enable);
        mRightMaster.setBrakeMode(enable);
    }

    public void setPosition(int position) {
        mWantedPosition = position;
        mLeftMaster.set(ControlMode.MotionMagic, mWantedPosition);
        mRightMaster.set(ControlMode.MotionMagic, mWantedPosition);
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

    public void setTrajectory(Trajectory trajectory) {
        mTrajectoryFollower.init();
        mTrajectoryFollower.follow(trajectory);
    }

    public void stopTrajectory() {
        mTrajectoryFollower.reset();
    }

    public boolean isTrajectoryFinished() {
        return mTrajectoryFollower.isFinished();
    }

    public void setLeftGains(ClosedLoopGains gains) {
        mLeftGains = gains;
        mLeftMaster.configPIDF(mLeftGains.getKp(), mLeftGains.getKi(), mLeftGains.getKd(), mLeftGains.getKf());
        mLeftMaster.configMotionAcceleration(mLeftGains.getAcceleration());
        mLeftMaster.configMotionCruiseVelocity(mLeftGains.getCruiseVelocity());
    }

    public void setRightGains(ClosedLoopGains gains) {
        mRightGains = gains;
        mRightMaster.configPIDF(mRightGains.getKp(), mRightGains.getKi(), mRightGains.getKd(), mRightGains.getKf());
        mRightMaster.configMotionAcceleration(mRightGains.getAcceleration());
        mRightMaster.configMotionCruiseVelocity(mRightGains.getCruiseVelocity());
    }

    public void setPigeonGains(ClosedLoopGains gains) {
    }

    public void resetEncoders() {
        mLeftMaster.setSelectedSensorPosition(0);
        mRightMaster.setSelectedSensorPosition(0);
        mPigeon.setYaw(0);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ManualArcadeDrive(false));
    }

    @Override
    public void stop() {
        setPower(0, 0);
    }

    @Override
    public Map<String, Object> getTelemetry() {
        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("LDriveKp", mLeftGains.getKp());
        m.put("LDriveKd", mLeftGains.getKd());
        m.put("LDriveKf", mLeftGains.getKf());
        m.put("LDriveAccel", mLeftGains.getAcceleration());
        m.put("LDriveCruiseVel", mLeftGains.getCruiseVelocity());
        m.put("LDrivePos", getLeftPosition());
        m.put("LDrivePower", getLeftPower());
        m.put("RDriveKp", mRightGains.getKp());
        m.put("RDriveKd", mRightGains.getKd());
        m.put("RDriveKf", mRightGains.getKf());
        m.put("RDriveAccel", mRightGains.getAcceleration());
        m.put("RDriveCruiseVel", mRightGains.getCruiseVelocity());
        m.put("RDrivePos", getRightPosition());
        m.put("RDrivePower", getRightPower());
        m.put("DriveWantedPos", mWantedPosition);
        m.put("DriveWantedYaw", mWantedYaw);
        m.put("DriveIsLowGear", isLowGear());
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
    public void updateGains() {
        updateFromDashboard();
        mLogger.debug("Wanted Position: {}", mWantedPosition);
        mLogger.debug("Current Left Position: {}", getLeftPosition());
        mLogger.debug("Current Right Position: {}", getRightPosition());
    }

    @Override
    public void reset() {
        resetEncoders();
    }

    @Override
    public Map<String, DoubleSupplier> getCSVTelemetry() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("left_position", this::getLeftPosition);
        m.put("right_position", this::getRightPosition);
        m.put("wanted_position", () -> mWantedPosition);
        return m;
    }

}
