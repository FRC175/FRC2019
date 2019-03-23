package com.team175.robot.util.drivers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team175.robot.Constants;
import com.team175.robot.util.RobotManager;

/**
 * @author Arvind
 * @see VictorSPX
 */
public class AldrinVictorSPX extends VictorSPX {

    private int mPDPChannel;

    public AldrinVictorSPX(int portNum) {
        this(portNum, -1);
    }

    public AldrinVictorSPX(int portNum, int pdpChannel) {
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
            throw new UnsupportedOperationException("Victor SPX " + super.getDeviceID() +
                    " is not configured to read PDP current!");
        } else {
            return RobotManager.getInstance().getCurrentForPDPChannel(mPDPChannel);
        }
    }

}