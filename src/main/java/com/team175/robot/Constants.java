package com.team175.robot;

/**
 * @author Arvind
 */
public final class Constants {

    // Prevent Constants from being instantiated
    private Constants() {
    }

    // CAN Ports
    public static final int LEFT_MASTER_DRIVE_PORT = 0;
    public static final int LEFT_SLAVE_DRIVE_PORT = 1;
    public static final int RIGHT_MASTER_DRIVE_PORT = 2;
    public static final int RIGHT_SLAVE_DRIVE_PORT = 3;
    public static final int PIGEON_PORT = 0;
    public static final int ELEVATOR_PORT = 7;
    public static final int LATERAL_DRIVE_PORT = 4;
    public static final int MANIPULATOR_ARM_PORT = 8;

    // CTRE Constants
    public static final int SLOT_INDEX = 0;
    public static final int PID_LOOP_INDEX = 0;
    public static final int TIMEOUT_MS = 10;
    public static final int ALLOWED_POSITION_DEVIATION = 10;

    // PWM
    public static final int LIFT_FRONT_PORT = 3;
    public static final int LIFT_REAR_PORT = 2;
    public static final int LIFT_DRIVE_PORT = 9; // CAN for now
    public static final int MANIPULATOR_FRONT_ROLLER = 1;
    public static final int MANIPULATOR_REAR_ROLLER = 0;

    // Digital Inputs
    public static final int LEFT_TWO_SENSOR_PORT = 5;
    public static final int LEFT_ONE_SENSOR_PORT = 4;
    public static final int CENTER_SENSOR_PORT = 3;
    public static final int RIGHT_ONE_SENSOR_PORT = 2;
    public static final int RIGHT_TWO_SENSOR_PORT = 1;
    public static final int LIFT_FRONT_LIMIT_PORT = 0;
    public static final int LIFT_REAR_LIMIT_PORT = 0;

    // Pneumatics
    public static final int SHIFT_CHANNEL = 0;
    public static final int LATERAL_DRIVE_DEPLOY_FORWARD_CHANNEL = 2;
    public static final int LATERAL_DRIVE_DEPLOY_REVERSE_CHANNEL = 1;

    // Joystick Ports
    public static final int DRIVER_STICK_PORT = 0;
    public static final int OPERATOR_STICK_PORT = 1;

    // Driver Stick Button Map
    public static final int LATERAL_DRIVE_TRIGGER = 1;
    public static final int LINE_ALIGN_BUTTON = 12;

    // Operator Stick Button Map
    public static final int MANUAL_ELEVATOR_TRIGGER = 1;

    /* Old */
    public static final int LEFT_TALON_PORT = 1;
    public static final int RIGHT_VICTOR_PORT = 2;
    public static final int CIM_TALON_PORT = 0;
    public static final int kNEOSparkPort = 3;
    public static final int OPTICAL_SENSOR_PORT = 0;
    public static final int LATERAL_DEPLOY_FORWARD_CHANNEL = 2;
    public static final int LATERAL_DEPLOY_REVERSE_CHANNEL = 1;
}
