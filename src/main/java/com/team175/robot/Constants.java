package com.team175.robot;

/**
 * @author Arvind
 */
public final class Constants {

    // Prevent Constants from being instantiated
    private Constants() {
    }

    /* Hardware Map */
    // CAN Ports
    public static final int LEFT_MASTER_DRIVE_PORT = 6;
    public static final int LEFT_SLAVE_DRIVE_PORT = 1;
    public static final int RIGHT_MASTER_DRIVE_PORT = 2;
    public static final int RIGHT_SLAVE_DRIVE_PORT = 3;
    public static final int ELEVATOR_PORT = 4;
    public static final int LATERAL_DRIVE_PORT = 8;
    public static final int MANIPULATOR_ARM_PORT = 7;

    // Pneumatics
    public static final int SHIFT_CHANNEL = 3;
    public static final int LATERAL_DRIVE_DEPLOY_CHANNEL = 2;
    public static final int MANIPULATOR_HATCH_PUSH_CHANNEL = 5;
    public static final int MANIPULATOR_DEPLOY_FORWARD_CHANNEL = 0;
    public static final int MANIPULATOR_DEPLOY_REVERSE_CHANNEL = 1;

    // PWM
    public static final int LIFT_FRONT_PORT = 3;
    public static final int LIFT_REAR_PORT = 0;
    public static final int LIFT_DRIVE_PORT = 4;
    public static final int MANIPULATOR_FRONT_ROLLER = 2;
    public static final int MANIPULATOR_REAR_ROLLER = 1;

    // Digital Inputs
    public static final int LEFT_TWO_SENSOR_PORT = 5;
    public static final int LEFT_ONE_SENSOR_PORT = 4;
    public static final int CENTER_SENSOR_PORT = 3;
    public static final int RIGHT_ONE_SENSOR_PORT = 2;
    public static final int RIGHT_TWO_SENSOR_PORT = 1;
    public static final int LIFT_FRONT_LIMIT_PORT = 0;
    public static final int LIFT_REAR_LIMIT_PORT = 0;

    /* Input Map */
    // Joystick Ports
    public static final int DRIVER_STICK_PORT = 0;
    public static final int OPERATOR_STICK_PORT = 1;

    // Driver Stick Buttons
    public static final int LATERAL_DRIVE_TRIGGER = 1;
    public static final int SHIFT_BUTTON = 2;
    public static final int STRAIGHT_DRIVE_BUTTON = 3;
    public static final int MANUAL_LIFT_BUTTON = 4;
    // public static final int MANUAL_LIFT_DRIVE_BUTTON = ;
    // public static final int LINE_ALIGN_BUTTON = 12;

    // Operator Stick Buttons
    public static final int MANUAL_ELEVATOR_TRIGGER = 1;
    public static final int TOGGLE_MANIPULATOR_BUTTON = 11;
    public static final int SCORE_HATCH_BUTTON = 5;
    public static final int GRAB_HATCH_BUTTON = 3;
    public static final int SCORE_CARGO_BUTTON = 6;
    public static final int GRAB_CARGO_BUTTON = 4;
    public static final int TOGGLE_PUSH_HATCH_BUTTON = 2;
    public static final int MANUAL_ARM_BUTTON = 12;

    /* Software Constants */
    // CTRE Constants
    public static final int SLOT_INDEX = 0;
    public static final int PID_LOOP_INDEX = 0;
    public static final int TIMEOUT_MS = 10;
    public static final int ALLOWED_POSITION_DEVIATION = 10;

    /* Old */
    // Breadboard
    public static final int LEFT_TALON_PORT = 1;
    public static final int RIGHT_VICTOR_PORT = 2;
    public static final int CIM_TALON_PORT = 0;
    public static final int kNEOSparkPort = 3;
    public static final int OPTICAL_SENSOR_PORT = 0;
    public static final int LATERAL_DEPLOY_FORWARD_CHANNEL = 2;
    public static final int LATERAL_DEPLOY_REVERSE_CHANNEL = 1;
}
