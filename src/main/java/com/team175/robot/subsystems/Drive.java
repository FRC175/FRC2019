package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.sensors.PigeonIMU;

import com.team175.robot.Constants;
import com.team175.robot.commands.teleop.ManualArcadeDrive;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CSVLogger;
import com.team175.robot.util.CTREFactory;
import com.team175.robot.util.Diagnosable;
import com.team175.robot.util.Loggable;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * @author Arvind
 */
public class Drive extends AldrinSubsystem implements Loggable, Diagnosable {

    // Talon SRXs
    private AldrinTalonSRX mLeftMaster, mLeftSlave, mRightMaster, mRightSlave;

    // Gyro
    private PigeonIMU mPigeon;

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

        // CTREFactory.getTalon(portNum : int)
        mLeftMaster = CTREFactory.getTalon(Constants.LEFT_MASTER_DRIVE_PORT);
        mLeftSlave = CTREFactory.getTalon(Constants.LEFT_SLAVE_DRIVE_PORT);
        mRightMaster = CTREFactory.getTalon(Constants.RIGHT_MASTER_DRIVE_PORT);
        mRightSlave = CTREFactory.getTalon(Constants.RIGHT_SLAVE_DRIVE_PORT);

        // mPigeon = new PigeonIMU(Constants.PIGEON_PORT);

        mShift = new Solenoid(Constants.SHIFT_CHANNEL);

        mLeftSlave.follow(mLeftMaster);
        mRightSlave.follow(mRightMaster);
    }

    public void arcadeDrive(double x, double y) {
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

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ManualArcadeDrive());
    }

    @Override
    public void outputToDashboard() {
    }

    @Override
    public void outputToLog() {
    }

    @Override
    public boolean checkSubsystem() {
        return false;
    }
}
