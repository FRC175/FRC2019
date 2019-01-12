package frc.robot.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * TODO: Make this Factory as configurable as possible. Consider adding Builder class.
 *
 * @author Arvind
 */
public class CTREFactory {

    // Prevent TalonSRXFactory from being instantiated
    private CTREFactory() {
    }

    private static void configSRX(TalonSRX srx) {
        // srx.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.PID_LOOP_INDEX, Constants.TIMEOUT_MS);
        srx.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        srx.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        srx.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        srx.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);
        // TODO: Determine what closed loop error was in Jamie's code
        srx.configAllowableClosedloopError(Constants.SLOT_INDEX, 10, Constants.TIMEOUT_MS);

        // srx.setSelectedSensorPosition(0, Constants.PID_LOOP_INDEX, Constants.TIMEOUT_MS);
        srx.setNeutralMode(NeutralMode.Brake);
    }

    private static void configSPX(VictorSPX spx) {
        // spx.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.PID_LOOP_INDEX, Constants.TIMEOUT_MS);
        spx.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        spx.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        spx.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        spx.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);
        // TODO: Determine what closed loop error was in Jamie's code
        spx.configAllowableClosedloopError(Constants.SLOT_INDEX, 10, Constants.TIMEOUT_MS);

        // spx.setSelectedSensorPosition(0, Constants.PID_LOOP_INDEX, Constants.TIMEOUT_MS);
        spx.setNeutralMode(NeutralMode.Brake);
    }

    public static TalonSRX getSRX(int portNum) {
        TalonSRX srx = new TalonSRX(portNum);
        configSRX(srx);
        return srx;
    }

    public static VictorSPX getSPX(int portNum) {
        VictorSPX spx = new VictorSPX(portNum);
        configSPX(spx);
        return spx;
    }

}
