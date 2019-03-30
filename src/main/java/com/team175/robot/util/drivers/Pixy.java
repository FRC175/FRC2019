package com.team175.robot.util.drivers;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Reads voltage and target detection from the analog and digital outputs of the Pixy camera.
 *
 * @author Arvind
 */
public class Pixy {

    private AnalogInput mTargetInfo;
    private DigitalInput mTargetDetector;

    public Pixy(int analogIn, int digitalIn) {
        mTargetInfo = new AnalogInput(analogIn);
        mTargetDetector = new DigitalInput(digitalIn);
    }

    public double getVoltage() {
        return mTargetInfo.getVoltage();
    }

    public boolean isTargetDetected() {
        return mTargetDetector.get();
    }

    // Basic understanding:
    // 0 v => target on left
    // 3.3 v => target on right
    // ~1.65 v => target in center
    // 
    // This function returns -1 if target is not found
    public double getTargetInfo() {
        return isTargetDetected() ? getVoltage() : -1;
    }

}
