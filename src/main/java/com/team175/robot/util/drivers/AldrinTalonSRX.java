package com.team175.robot.util.drivers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team175.robot.Constants;
import com.team175.robot.util.RobotManager;

/**
 * TODO: Delete PIDF methods
 *
 * @author Arvind
 * @see TalonSRX
 */
public class AldrinTalonSRX extends TalonSRX {

    private int mPDPChannel;

    public AldrinTalonSRX(int portNum) {
        this(portNum, -1);
    }

    public AldrinTalonSRX(int portNum, int pdpChannel) {
        super(portNum);
        mPDPChannel = pdpChannel;
    }

    public ErrorCode setPrimarySensorPosition(int sensorPos) {
        return setSelectedSensorPosition(sensorPos, 0, Constants.TIMEOUT_MS);
    }

    public ErrorCode setAuxSensorPosition(int sensorPos) {
        return setSelectedSensorPosition(sensorPos, 1, Constants.TIMEOUT_MS);
    }

    public void setBrakeMode(boolean enable) {
        super.setNeutralMode(enable ? NeutralMode.Brake : NeutralMode.Coast);
    }

    public double getPDPCurrent() {
        if (mPDPChannel == -1) {
            throw new UnsupportedOperationException("Talon SRX " + super.getDeviceID() +
                    " is not configured to read PDP current!");
        } else {
            return RobotManager.getInstance().getCurrentForPDPChannel(mPDPChannel);
        }
    }

}