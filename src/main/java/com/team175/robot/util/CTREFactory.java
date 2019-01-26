package com.team175.robot.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.team175.robot.Constants;

/**
 * @author Arvind
 */
public class CTREFactory {

    // Prevent CTREFactory from being instantiated
    private CTREFactory() {
    }

    private static void config(BaseMotorController bmc) {
        bmc.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        bmc.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        bmc.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        bmc.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

        /*bmc.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.PID_LOOP_INDEX, Constants.TIMEOUT_MS);
        bmc.configAllowableClosedloopError(Constants.SLOT_INDEX, 10, Constants.TIMEOUT_MS);
        bmc.setSelectedSensorPosition(0, Constants.PID_LOOP_INDEX, Constants.TIMEOUT_MS);*/

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
