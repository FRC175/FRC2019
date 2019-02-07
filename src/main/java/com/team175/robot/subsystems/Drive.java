package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.Constants;
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

        mWantedPosition = 0;
        mWantedYaw = 0.0;
        mLeftGains = Constants.LEFT_DRIVE_GAINS;
        mRightGains = Constants.RIGHT_DRIVE_GAINS;

        setLeftGains(mLeftGains);
        setRightGains(mRightGains);

        mLeftMaster.setSensorPhase(true);
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

    public int getRightPosition() {
        return mRightMaster.getSelectedSensorPosition();
    }

    public int getRightVelocity() {
        return mRightMaster.getSelectedSensorVelocity();
    }

    public void setLeftGains(ClosedLoopGains gains) {
        mLeftGains = gains;
        mLeftMaster.config_kP(mLeftGains.getKp());
        mLeftMaster.config_kI(mLeftGains.getKi());
        mLeftMaster.config_kD(mLeftGains.getKd());
        mLeftMaster.config_kF(mLeftGains.getKf());
        mLeftMaster.configMotionAcceleration(mLeftGains.getAcceleration());
        mLeftMaster.configMotionCruiseVelocity(mLeftGains.getCruiseVelocity());
    }

    public void setRightGains(ClosedLoopGains gains) {
        mRightGains = gains;
        mRightMaster.config_kP(mRightGains.getKp());
        mRightMaster.config_kI(mRightGains.getKi());
        mRightMaster.config_kD(mRightGains.getKd());
        mRightMaster.config_kF(mRightGains.getKf());
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
