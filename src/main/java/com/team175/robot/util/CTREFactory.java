package com.team175.robot.util;

import com.ctre.phoenix.motorcontrol.ControlMode;
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

    private static void configOpenLoop(BaseMotorController bmc) {
        bmc.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        bmc.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        bmc.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        bmc.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

        bmc.setNeutralMode(NeutralMode.Coast);
    }

    private static void configClosedLoop(BaseMotorController bmc) {
        configOpenLoop(bmc);

        bmc.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.PID_LOOP_INDEX, Constants.TIMEOUT_MS);
        bmc.configAllowableClosedloopError(Constants.SLOT_INDEX, 10, Constants.TIMEOUT_MS);
        bmc.setSelectedSensorPosition(0, Constants.PID_LOOP_INDEX, Constants.TIMEOUT_MS);
    }

    public static AldrinTalonSRX getTalon(int portNum) {
        AldrinTalonSRX talon = new AldrinTalonSRX(portNum);
        configOpenLoop(talon);
        return talon;
    }

    public static AldrinTalonSRX getMasterTalon(int portNum) {
        AldrinTalonSRX talon = new AldrinTalonSRX(portNum);
        configClosedLoop(talon);
        return talon;
    }

    public static AldrinTalonSRX getSlaveTalon(int portNum, BaseMotorController master) {
        AldrinTalonSRX talon = new AldrinTalonSRX(portNum);
        configClosedLoop(talon);
        talon.follow(master);
        return talon;
    }

    public static AldrinVictorSPX getVictor(int portNum) {
        AldrinVictorSPX victor = new AldrinVictorSPX(portNum);
        configOpenLoop(victor);
        return victor;
    }

    public static AldrinVictorSPX getMasterVictor(int portNum) {
        AldrinVictorSPX victor = new AldrinVictorSPX(portNum);
        configClosedLoop(victor);
        return victor;
    }

    public static AldrinVictorSPX getSlaveVictor(int portNum, BaseMotorController master) {
        AldrinVictorSPX victor = new AldrinVictorSPX(portNum);
        configClosedLoop(victor);
        victor.follow(master);
        return victor;
    }

}
