package com.team175.robot.util.drivers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team175.robot.Constants;
import edu.wpi.first.wpilibj.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Vet code.
 *
 * @author Arvind
 */
public class CTREDiagnostics {

    private final BaseMotorController mBMC;
    private final String mName;

    private static final Logger sLogger = LoggerFactory.getLogger(CTREDiagnostics.class.getSimpleName());
    private static final double TEST_RETRY_COUNT = 4;
    private static final double DELAY_TIME = 2; // In seconds

    private boolean isOutputDirectionGood;
    private boolean doesSensorExist;
    private boolean isSensorInPhase;
    private boolean isReset;

    public CTREDiagnostics(BaseMotorController bmc, String name) {
        mBMC = bmc;
        mName = name;
    }

    private boolean checkOutputDirection() {
        double prevPower = mBMC.getMotorOutputPercent();
        mBMC.set(ControlMode.PercentOutput, 0.5);
        Timer.delay(DELAY_TIME);

        if (!(mBMC.getMotorOutputPercent() > prevPower)) {
            sLogger.warn("{} must be inverted!", mName);
            return false;
        } else {
            sLogger.warn("{} passed output direction test!", mName);
            return true;
        }
    }

    private boolean checkSensorExists() {
        if (mBMC instanceof TalonSRX) {
            if (((TalonSRX) mBMC).getSensorCollection().getPulseWidthRiseToFallUs() == 0) {
                sLogger.warn("{} passed encoder existence test!", mName);
                return true;
            } else {
                sLogger.warn("{} does not have an encoder attached!", mName);
                return false;
            }
        } else {
            sLogger.warn("Cannot check if sensor exists on {}!", mName);
            return true;
        }
    }

    private boolean checkSensorPhase(boolean isInverse) {
        double prevPos = mBMC.getSelectedSensorPosition();
        mBMC.set(ControlMode.PercentOutput, 0.75);
        Timer.delay(DELAY_TIME); // Put in Constants file

        if (!(mBMC.getSelectedSensorPosition() > prevPos)) {
            sLogger.warn("{} sensor out of phase!", mName);
            return false;
        } else {
            sLogger.warn("{} passed sensor phase test!", mName);
            return true;
        }
    }

    private boolean checkReset() {
        return mBMC.hasResetOccurred();
    }

    public boolean isOutputDirectionGood() {
        return isOutputDirectionGood;
    }

    public boolean isDoesSensorExist() {
        return doesSensorExist;
    }

    public boolean isSensorInPhase() {
        return isSensorInPhase;
    }

    public boolean isReset() {
        return isReset;
    }

    public boolean checkMotorController() {
        boolean isGood = true;
        double i = 0;

        // Run checks at least once and at most five
        do {
            isGood = true;
            // isGood &= checkSensorExists() ? checkSensorPhase(checkOutputDirection()) : checkOutputDirection();
            isGood &= checkSensorPhase(checkOutputDirection());
            isGood &= checkReset();
        } while (!isGood && i++ < TEST_RETRY_COUNT);

        return isGood;
    }

    public static boolean checkCommand(ErrorCode code, String msg) {
        if (code != ErrorCode.OK) {
            sLogger.warn("{}!\n" +
                    "ErrorCode: {}", msg, code.toString());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Diagnostics Test for ").append(mName).append(":\n");
        sb.append("Output Direction: ").append(isOutputDirectionGood).append("\n");
        sb.append("Sensor Existence: ").append(doesSensorExist).append("\n");
        sb.append("Sensor Phase: ").append(isSensorInPhase).append("\n");
        sb.append("Motor Reset?: ").append(isReset);
        return sb.toString();
    }

}
