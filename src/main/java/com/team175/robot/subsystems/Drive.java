package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;

import com.team175.robot.Constants;
import com.team175.robot.commands.ManualArcadeDrive;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CSVLogger;
import com.team175.robot.util.CTREFactory;
import com.team175.robot.util.Diagnosable;
import com.team175.robot.util.Loggable;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * TODO: Maybe implement cheesy drive.
 *
 * @author Arvind
 */
public class Drive extends AldrinSubsystem implements Loggable, Diagnosable {

    /* Declarations */
    // Talon SRXs
    private AldrinTalonSRX mLeftMaster, mLeftSlave, mRightMaster, mRightSlave;

    // Gyro
    // private PigeonIMU mPigeon;

    // Solenoid
    private Solenoid mShift;

    // Loggers
    private CSVLogger mCSVLogger;

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

        // PigeonIMU(portNum : int)
        // mPigeon = new PigeonIMU(Constants.PIGEON_PORT);

        // Solenoid(channel : int)
        // mShift = new Solenoid(Constants.SHIFT_CHANNEL);
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

    public int getLeftPosition() {
        return mLeftMaster.getSelectedSensorPosition();
    }

    public int getRightPosition() {
        return mRightMaster.getSelectedSensorPosition();
    }

    public void resetEncoders() {
        mLeftMaster.setSelectedSensorPosition(0);
        mRightMaster.setSelectedSensorPosition(0);
        // mPigeon.setYaw(0);
    }

    public void setHighGear(boolean enable) {
        // mShift.set(enable);
    }

    public boolean isHighGear() {
        return false;
        // return mShift.get();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ManualArcadeDrive(false));
    }

    @Override
    public void toDashboard() {
    }

    @Override
    public void toLog() {
    }

    @Override
    public boolean checkSubsystem() {
        return false;
    }
}
