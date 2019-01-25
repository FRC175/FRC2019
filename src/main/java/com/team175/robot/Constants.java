package com.team175.robot;

/**
 * @author Arvind
 */
public final class Constants {

    // Prevent Constants from being instantiated
    private Constants() {
    }

    // CAN Ports
    public static final int LEFT_TALON_PORT = 1;
    public static final int RIGHT_VICTOR_PORT = 2;
    public static final int CIM_TALON_PORT = 0;
    // public static final int kNEOSparkPort = 3;

    // Pneumatics
    public static final int LATERAL_DEPLOY_FORWARD_CHANNEL = 2;
    public static final int LATERAL_DEPLOY_REVERSE_CHANNEL = 1;
    public static final int SHIFT_CHANNEL = 0;

    // Digital Inputs
    public static final int OPTICAL_SENSOR_PORT = 0;
    public static final int LEFT_TWO_SENSOR_PORT = 5;
    public static final int LEFT_ONE_SENSOR_PORT = 4;
    public static final int CENTER_SENSOR_PORT = 3;
    public static final int RIGHT_ONE_PORT = 2;
    public static final int RIGHT_TWO_PORT = 1;

    // CTRE Constants
    public static final int SLOT_INDEX = 0;
    public static final int PID_LOOP_INDEX = 0;
    public static final int TIMEOUT_MS = 10;

    // Joystick Ports
    public static final int DRIVER_STICK_PORT = 0;
    public static final int OPERATOR_STICK_PORT = 1;

    // Driver Stick Button Map
    public static final int LINE_ALIGN_BUTTON = 12;

    // Operator Stick Button Map

}
