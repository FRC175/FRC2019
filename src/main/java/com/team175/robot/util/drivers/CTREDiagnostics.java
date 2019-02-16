package com.team175.robot.util.drivers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
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

    private int mPostPos;
    private boolean mIsInverted;
    private boolean mDoesEncoderExist;
    private boolean mIsSensorInPhase;
    private boolean mIsReset;

    public CTREDiagnostics(BaseMotorController bmc, String name) {
        mBMC = bmc;
        mName = name;

        mPostPos = 0;
        mIsInverted = false;
        mDoesEncoderExist = false;
        mIsSensorInPhase = false;
        mIsReset = false;
    }

    private boolean checkOutputDirection() {
        double prevPower = mBMC.getMotorOutputPercent();
        mBMC.set(ControlMode.PercentOutput, 0.75);
        Timer.delay(DELAY_TIME);

        if (!(mBMC.getMotorOutputPercent() > prevPower)) {
            sLogger.warn("{} must be inverted!", mName);
            // return false;
            mIsInverted = false;
        } else {
            sLogger.warn("{} passed output direction test.", mName);
            mIsInverted = true;
        }

        return mIsInverted;
    }

    private boolean checkSensorExists() {
        if (mBMC instanceof TalonSRX) {
            if (((TalonSRX) mBMC).getSensorCollection().getPulseWidthRiseToFallUs() == 0) {
                sLogger.warn("{} does not have an encoder attached!", mName);
                mIsSensorInPhase = false;
            } else {
                sLogger.warn("{} passed encoder existence test.", mName);
                mIsSensorInPhase = true;
            }
        } else {
            sLogger.warn("{} does not support encoder existence testing!", mName);
            mIsSensorInPhase = true;
        }

        return mIsSensorInPhase;
    }

    private boolean checkSensorPhase() {
        double prevPos = mBMC.getSelectedSensorPosition();
        mBMC.set(ControlMode.PercentOutput, (mIsInverted ? -0.75 : 0.75));
        Timer.delay(DELAY_TIME);
        // mPostPos =

        if (!(mBMC.getSelectedSensorPosition() > prevPos)) {
            sLogger.warn("{} sensor out of phase!", mName);
            mDoesEncoderExist = false;
        } else {
            sLogger.warn("{} passed sensor phase test.", mName);
            mDoesEncoderExist = true;
        }

        return mDoesEncoderExist;
    }

    private boolean checkReset() {
        mIsReset = mBMC.hasResetOccurred();
        return mIsReset;
    }

    public int getPostTestPosition() {
        return mPostPos;
    }

    /*public boolean isOutputDirectionGood() {
        return mIsInverted;
    }

    public boolean doesSensorExist() {
        return mDoesEncoderExist;
    }

    public boolean isSensorInPhase() {
        return mIsSensorInPhase;
    }

    public boolean isReset() {
        return mIsReset;
    }*/

    public boolean checkMotorController() {
        boolean isGood = true;
        double i = 0;

        // Run checks at least once and at most five
        do {
            isGood = true;
            // isGood &= checkSensorExists() ? checkSensorPhase(checkOutputDirection()) : checkOutputDirection();
            isGood &= checkSensorPhase();
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
        sb.append("Diagnostics Test Summary for ").append(mName).append(":\n");
        sb.append("Output Direction: ").append(mIsInverted).append("\n");
        sb.append("Encoder Existence: ").append(mDoesEncoderExist).append("\n");
        sb.append("Sensor Phase: ").append(mIsSensorInPhase).append("\n");
        sb.append("Motor Reset?: ").append(mIsReset);
        return sb.toString();
    }

}
