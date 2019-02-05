package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.team175.robot.Constants;
import com.team175.robot.commands.ManualArcadeDrive;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;

import com.team175.robot.util.ClosedLoopTunable;
import com.team175.robot.util.PIDFGains;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;
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

    public void setPosition(int position) {
        mWantedPosition = position;
        mLeftMaster.set(ControlMode.MotionMagic, mWantedPosition);
        mRightMaster.set(ControlMode.MotionMagic, mWantedPosition);
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

    public void setLeftPIDF(PIDFGains gains) {
        mLeftMaster.config_kP(gains.getKp());
        mLeftMaster.config_kI(gains.getKi());
        mLeftMaster.config_kD(gains.getKd());
        mLeftMaster.config_kF(gains.getKf());
    }

    public void setRightPIDF(PIDFGains gains) {
        mRightMaster.config_kP(gains.getKp());
        mRightMaster.config_kI(gains.getKi());
        mRightMaster.config_kD(gains.getKd());
        mRightMaster.config_kF(gains.getKf());
    }

    public void sendToDashboard() {
        SmartDashboard.putNumber("Drive Left kP", 0);
        SmartDashboard.putNumber("Drive Left kD", 0);
        SmartDashboard.putNumber("Drive Left kF", 0);

        SmartDashboard.putNumber("Drive Right kP", 0);
        SmartDashboard.putNumber("Drive Right kD", 0);
        SmartDashboard.putNumber("Drive Right kF", 0);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ManualArcadeDrive(false));
    }

    @Override
    public void updatePIDF() {
        setLeftPIDF(new PIDFGains(SmartDashboard.getNumber("Drive Left kP", 0), 0,
                SmartDashboard.getNumber("Drive Left kD", 0),
                SmartDashboard.getNumber("Drive Left kF", 0)));

        setRightPIDF(new PIDFGains(SmartDashboard.getNumber("Drive Right kP", 0), 0,
                SmartDashboard.getNumber("Drive Right kD", 0),
                SmartDashboard.getNumber("Drive Right kF", 0)));
    }

    @Override
    public void updateWantedPosition() {
        setPosition((int) SmartDashboard.getNumber("Drive Position", 0));
    }

    @Override
    public LinkedHashMap<String, DoubleSupplier> getCSVProperties() {
        LinkedHashMap<String, DoubleSupplier> m = new LinkedHashMap<>();
        m.put("left_position", this::getLeftPosition);
        m.put("right_position", this::getRightPosition);
        m.put("wanted_position", () -> mWantedPosition);
        return m;
    }

}
