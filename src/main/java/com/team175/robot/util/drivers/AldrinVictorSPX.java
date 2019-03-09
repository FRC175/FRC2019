package com.team175.robot.util.drivers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team175.robot.Constants;
import com.team175.robot.util.RobotManager;
import com.team175.robot.util.choosers.RobotChooser;

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

    public ErrorCode config_kP(double value) {
        return super.config_kP(Constants.PRIMARY_GAINS_SLOT, value, Constants.TIMEOUT_MS);
    }

    public ErrorCode config_kI(double value) {
        return super.config_kI(Constants.PRIMARY_GAINS_SLOT, value, Constants.TIMEOUT_MS);
    }

    public ErrorCode config_kD(double value) {
        return super.config_kD(Constants.PRIMARY_GAINS_SLOT, value, Constants.TIMEOUT_MS);
    }

    public ErrorCode config_kF(double value) {
        return super.config_kF(Constants.PRIMARY_GAINS_SLOT, value, Constants.TIMEOUT_MS);
    }

    public ErrorCode config_aux_kP(double value) {
        return super.config_kP(Constants.AUX_GAINS_SLOT, value, Constants.TIMEOUT_MS);
    }

    public ErrorCode config_aux_kI(double value) {
        return super.config_kI(Constants.AUX_GAINS_SLOT, value, Constants.TIMEOUT_MS);
    }

    public ErrorCode config_aux_kD(double value) {
        return super.config_kD(Constants.AUX_GAINS_SLOT, value, Constants.TIMEOUT_MS);
    }

    public ErrorCode config_aux_kF(double value) {
        return super.config_kF(Constants.AUX_GAINS_SLOT, value, Constants.TIMEOUT_MS);
    }

    public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice, int pidIdx) {
        return super.configSelectedFeedbackSensor(feedbackDevice, pidIdx, Constants.TIMEOUT_MS);
    }

    public ErrorCode setSelectedSensorPosition(int sensorPos, int pidIdx) {
        return super.setSelectedSensorPosition(sensorPos, pidIdx, Constants.TIMEOUT_MS);
    }

    @Override
    public ErrorCode setSelectedSensorPosition(int sensorPos) {
        return setSelectedSensorPosition(sensorPos, 0);
    }

    @Override
    public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice) {
        return configSelectedFeedbackSensor(feedbackDevice, Constants.PRIMARY_GAINS_SLOT);
    }

    public void setBrakeMode(boolean enable) {
        super.setNeutralMode(enable ? NeutralMode.Brake : NeutralMode.Coast);
    }

    public ErrorCode configPIDF(double kP, double kI, double kD, double kF) {
        ErrorCode[] ec = new ErrorCode[4];
        ec[0] = config_kP(kP);
        ec[1] = config_kI(kI);
        ec[2] = config_kD(kD);
        ec[3] = config_kF(kF);
        return getFinalError(ec);
    }

    public ErrorCode configAuxPIDF(double kP, double kI, double kD, double kF) {
        ErrorCode[] ec = new ErrorCode[4];
        ec[0] = config_aux_kP(kP);
        ec[1] = config_aux_kI(kI);
        ec[2] = config_aux_kD(kD);
        ec[3] = config_aux_kF(kF);
        return getFinalError(ec);
    }

    /**
     * Checks if all errors in array are okay and if not return first error that's not okay
     */
    private ErrorCode getFinalError(ErrorCode[] codes) {
        for (ErrorCode code : codes) {
            if (code != ErrorCode.OK) {
                return code;
            }
        }

        return ErrorCode.OK;
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