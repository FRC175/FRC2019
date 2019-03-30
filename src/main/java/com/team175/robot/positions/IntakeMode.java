package com.team175.robot.positions;

/**
 * @author Arvind
 */
public enum IntakeMode {

    VELCRO_HATCH,
    FINGER_HATCH,
    CARGO;

    // Starts with velcro hatch
    public static IntakeMode sMode = IntakeMode.VELCRO_HATCH;

    public static void setMode(IntakeMode mode) {
        sMode = mode;
    }

    public static IntakeMode getMode() {
        return sMode;
    }

}