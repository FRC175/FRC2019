package com.team175.robot.util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team175.robot.Constants;
import edu.wpi.first.wpilibj.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvind
 */
public class CTREDiagnostics {

    private final BaseMotorController mBMC;
    private final String mName;

    private static final Logger sLogger = LoggerFactory.getLogger(CTREDiagnostics.class.getSimpleName());
    private static final double TEST_RETRY_COUNT = 4;
    private static final double DELAY_TIME = 2; // In seconds

    private boolean mIsOutputGood, mDoesEncoderExist, mIsSensorInPhase, mIsFaulted;

    public CTREDiagnostics(BaseMotorController bmc, String name) {
        mBMC = bmc;
        mName = name;

        mIsOutputGood = false;
        mDoesEncoderExist = false;
        mIsSensorInPhase = false;
        mIsFaulted = false;
    }

    private boolean checkOutputDirection() {
        double prevPower = mBMC.getMotorOutputPercent();
        mBMC.set(ControlMode.PercentOutput, 0.75);
        Timer.delay(DELAY_TIME);
        double power = mBMC.getMotorOutputPercent();
        mBMC.set(ControlMode.PercentOutput, 0);

        if (power <= prevPower) {
            sLogger.warn("{} must be inverted!", mName);
            // return false;
            return false;
        } else {
            sLogger.info("{} passed output direction test.", mName);
            return true;
        }
    }

    private boolean checkEncoderExists() {
        if (mBMC instanceof TalonSRX) {
            mBMC.set(ControlMode.PercentOutput, 0.75);
            Timer.delay(DELAY_TIME);
            boolean doesEncoderExist = ((TalonSRX) mBMC).getSensorCollection().getPulseWidthRiseToFallUs() != 0;
            mBMC.set(ControlMode.PercentOutput, 0);

            if (!doesEncoderExist) {
                sLogger.warn("{} does not have an encoder attached!", mName);
                return false;
            } else {
                sLogger.info("{} passed encoder existence test.", mName);
                return true;
            }
        } else {
            sLogger.warn("{} does not support encoder existence testing!", mName);
            return true; // Returns true in order to still run sensor phase test
        }
    }

    private boolean checkSensorPhase() {
        mBMC.setSelectedSensorPosition(0);
        double prePos = mBMC.getSelectedSensorPosition();
        mBMC.set(ControlMode.PercentOutput, (mIsOutputGood ? 0.75 : -0.75));
        Timer.delay(DELAY_TIME);
        mBMC.set(ControlMode.PercentOutput, 0);
        double postPos = mBMC.getSelectedSensorPosition();

        if (postPos <= prePos) {
            sLogger.warn("{} sensor out of phase!", mName);
            return false;
        } else {
            sLogger.info("{} passed sensor phase test.", mName);
            return true;
        }
    }

    private boolean checkFaults() {
        if (mBMC.hasResetOccurred()) {
            if (mBMC.clearStickyFaults(Constants.TIMEOUT_MS) != ErrorCode.OK) {
                sLogger.warn("{} failed to clear faults!", mName);
                return false;
            }
        }

        sLogger.info("{} passed faults test!", mName);
        return true;
    }

    public boolean checkMotorController() {
        boolean isGood = true;
        double i = 0;

        // Run checks at least once and at most five times
        do {
            sLogger.info("Beginning diagnostics test for {}, trial {}.", mName, i + 1);

            mIsOutputGood = checkOutputDirection();
            // mDoesEncoderExist = checkEncoderExists();
            // mIsSensorInPhase = mDoesEncoderExist ? checkSensorPhase() : false;
            mIsSensorInPhase = checkSensorPhase();
            mIsFaulted = !checkFaults(); // Invert to see if motor is faulted

            isGood = true;
            isGood &= mIsOutputGood;
            // isGood &= mDoesEncoderExist;
            isGood &= mIsSensorInPhase;
            isGood &= !mIsFaulted; // Invert again to see if motor is good
        } while (!isGood && i++ < TEST_RETRY_COUNT);

        if (!isGood && i >= TEST_RETRY_COUNT) {
            sLogger.error("{} failed diagnostics test!", mName);
        }

        return isGood;
    }

    public static void checkCommand(ErrorCode code, String msg) {
        if (code != ErrorCode.OK) {
            sLogger.warn("{}\n" +
                    "ErrorCode: {}", msg, code.toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Diagnostics Test Summary for ").append(mName).append(":\n");
        sb.append("Output Direction: ").append(mIsOutputGood).append("\n");
        // sb.append("Encoder Existence: ").append(mDoesEncoderExist).append("\n");
        sb.append("Sensor Phase: ").append(mIsSensorInPhase).append("\n");
        sb.append("Motor Reset?: ").append(mIsFaulted);
        return sb.toString();
    }

}
