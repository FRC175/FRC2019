package com.team175.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team175.robot.Constants;
import com.team175.robot.util.AldrinTalonSRX;
import com.team175.robot.util.CTREFactory;
import com.team175.robot.util.Diagnosable;

/**
 * @author Arvind
 */
public class Elevator extends AldrinSubsystem implements Diagnosable {

    // Integer
    public int mWantedPosition;

    // Talon SRX
    private AldrinTalonSRX mMaster;

    // Singleton Instance
    private static Elevator sInstance;

    public static Elevator getInstance() {
        if (sInstance == null) {
            sInstance = new Elevator();
        }

        return sInstance;
    }

    private Elevator() {
        mMaster = CTREFactory.getTalon(Constants.ELEVATOR_PORT);

        mWantedPosition = 0;
    }

    public void setMotionMagicPosition(int position) {
        mWantedPosition = position;
        mMaster.set(ControlMode.MotionMagic, mWantedPosition);
    }

    public int getPosition() {
        return mMaster.getSelectedSensorPosition();
    }

    public boolean isAtWantedPosition() {
        return Math.abs(getPosition() - mWantedPosition) <= Constants.ALLOWED_POSITION_DEVIATION;
    }

    public void resetEncoder() {
        mMaster.setSelectedSensorPosition(0);
    }

    public void setPower(double power) {
        mMaster.set(ControlMode.PercentOutput, power);
    }

    public double getPower() {
        return mMaster.getMotorOutputPercent();
    }

    public double getVoltage() {
        return mMaster.getMotorOutputVoltage();
    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public boolean checkSubsystem() {
        return false;
    }
}
