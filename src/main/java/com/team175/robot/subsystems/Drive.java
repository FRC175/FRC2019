package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.Constants;
import com.team175.robot.commands.ManualArcadeDrive;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;

import com.team175.robot.util.ClosedLoopTunable;
import edu.wpi.first.wpilibj.Solenoid;

import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * TODO: Maybe implement cheesy drive.
 *
 * @author Arvind
 */
public class Drive extends AldrinSubsystem implements ClosedLoopTunable {

    /* Declarations */
    private AldrinTalonSRX mLeftMaster, mLeftSlave, mRightMaster, mRightSlave;
    private PigeonIMU mPigeon;
    private Solenoid mShift;

    private int mWantedPosition;

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

        /*mLeftMaster.configFactoryDefault();
        mRightMaster.configFactoryDefault();*/
    }

    public void arcadeDrive(double y, double x) {
        /*mLeftMaster.set(ControlMode.PercentOutput, y, DemandType.ArbitraryFeedForward, +x);
        mRightMaster.set(ControlMode.PercentOutput, y, DemandType.ArbitraryFeedForward, -x);*/

        mLeftMaster.set(ControlMode.PercentOutput, y);
        mRightMaster.set(ControlMode.PercentOutput, y);
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
        mPigeon.setYaw(0);
    }

    public void setLowGear(boolean enable) {
        mShift.set(enable);
    }

    public boolean isLowGear() {
        return mShift.get();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ManualArcadeDrive(false));
    }

    @Override
    public void updatePID() {
    }

    @Override
    public Map<String, DoubleSupplier> getCSVProperties() {
        return Map.of(
                "left_position", this::getRightPosition,
                "right_position", this::getRightPosition,
                "wanted_position", () -> mWantedPosition
        );
    }

}
