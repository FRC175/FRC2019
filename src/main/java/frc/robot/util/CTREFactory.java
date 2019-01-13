package frc.robot.util;

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
        // bmc.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        bmc.configNominalOutputForward(0, Constants.kTimeoutMs);
        bmc.configNominalOutputReverse(0, Constants.kTimeoutMs);
        bmc.configPeakOutputForward(1, Constants.kTimeoutMs);
        bmc.configPeakOutputReverse(-1, Constants.kTimeoutMs);
        // TODO: Determine what closed loop error was in Jamie's code
        bmc.configAllowableClosedloopError(Constants.kSlotIdx, 10, Constants.kTimeoutMs);

        // bmc.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        bmc.setNeutralMode(NeutralMode.Brake);
    }

    public static AldrinTalonSRX getSRX(int portNum) {
        AldrinTalonSRX srx = new AldrinTalonSRX(portNum);
        config(srx);
        return srx;
    }

    public static AldrinVictorSPX getSPX(int portNum) {
        AldrinVictorSPX spx = new AldrinVictorSPX(portNum);
        config(spx);
        return spx;
    }

}
