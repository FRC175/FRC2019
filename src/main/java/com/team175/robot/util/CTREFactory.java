package com.team175.robot.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

/**
 * @author Arvind
 */
public class CTREFactory {

    // Prevent TalonSRXFactory from being instantiated
    private CTREFactory() {
    }

    private static void config(BaseMotorController bmc) {
        bmc.configNominalOutputForward(0, Constants.kTimeoutMs);
        bmc.configNominalOutputReverse(0, Constants.kTimeoutMs);
        bmc.configPeakOutputForward(1, Constants.kTimeoutMs);
        bmc.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        bmc.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        bmc.configAllowableClosedloopError(Constants.kSlotIdx, 10, Constants.kTimeoutMs);
        bmc.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

        bmc.setNeutralMode(NeutralMode.Brake);
    }

    public static AldrinTalonSRX getTalon(int portNum) {
        AldrinTalonSRX talon = new AldrinTalonSRX(portNum);
        config(talon);
        return talon;
    }

    public static AldrinVictorSPX getVictor(int portNum) {
        AldrinVictorSPX victor = new AldrinVictorSPX(portNum);
        config(victor);
        return victor;
    }

}
