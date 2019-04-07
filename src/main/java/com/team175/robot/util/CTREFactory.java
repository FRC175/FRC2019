package com.team175.robot.util;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.team175.robot.Constants;
import com.team175.robot.util.drivers.AldrinTalonSRX;
import com.team175.robot.util.drivers.AldrinVictorSPX;

/**
 * A static factory class containing different builders for the Talon SRX and Victor SPX.
 *
 * @author Arvind
 */
public final class CTREFactory {

    private static final int S_CURVE_STRENGTH = 4; // Half smoothing (0-8)
    private static final int ALLOWABLE_CLOSED_LOOP_ERROR = 100;

    // Prevent CTREFactory from being instantiated
    private CTREFactory() {
    }

    private static void configOpenLoop(BaseMotorController bmc) {
        bmc.configFactoryDefault(Constants.TIMEOUT_MS);

        bmc.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        bmc.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        bmc.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        bmc.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

        bmc.setNeutralMode(NeutralMode.Coast);
    }

    private static void configClosedLoop(BaseMotorController bmc) {
        configOpenLoop(bmc);
        bmc.configMotionSCurveStrength(S_CURVE_STRENGTH, Constants.TIMEOUT_MS);
        // bmc.configAllowableClosedloopError(Constants.PRIMARY_GAINS_SLOT, ALLOWABLE_CLOSED_LOOP_ERROR, Constants.TIMEOUT_MS);
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
        configOpenLoop(talon);
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
        configOpenLoop(victor);
        victor.follow(master);
        return victor;
    }

}
