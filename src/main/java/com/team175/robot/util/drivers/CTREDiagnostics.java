package com.team175.robot.util.drivers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import edu.wpi.first.wpilibj.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CTREDiagnostics {

    private final BaseMotorController mBMC;
    private final String mName;
    private static final Logger sLogger = LoggerFactory.getLogger(CTREDiagnostics.class.getSimpleName());

    public CTREDiagnostics(BaseMotorController bmc, String name) {
        mBMC = bmc;
        mName = name;
    }

    public boolean checkOutputDirection() {
        double prevPower = mBMC.getMotorOutputPercent();
        mBMC.set(ControlMode.PercentOutput, 0.5);
        Timer.delay(2); // Put in Constants file
        return mBMC.getMotorOutputPercent() > prevPower;
    }

    public boolean checkSensorPhase(boolean isInverse) {
        double prevPos = mBMC.getSelectedSensorPosition();
        mBMC.set(ControlMode.PercentOutput, 0.75);
        Timer.delay(2); // Put in Constants file
        return mBMC.getSelectedSensorPosition() > prevPos;
    }

    public boolean checkMotor() {
        boolean isGood = false;
        double i = 0;

        do {
            isGood &= checkSensorPhase(checkOutputDirection());
        } while (!isGood && i < 5); // Put 5 in Constants file

        return isGood;
    }

    public static boolean checkCommand(ErrorCode code, String nameOfTalon) {
        if (code != ErrorCode.OK) {
            sLogger.warn("{} failed to perform set operation!\n" +
                    "ErrorCode: {}", nameOfTalon, code.toString());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "";
    }

}
