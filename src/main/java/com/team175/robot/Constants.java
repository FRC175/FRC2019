package com.team175.robot;

/**
 * @author Arvind
 */
public final class Constants {

    // Prevent Constants from being instantiated
    private Constants() {
    }

    // ----------------------------------------------------------------------------------------------------
    // Hardware Map
    // ----------------------------------------------------------------------------------------------------
    // CAN Ports
    public static final int LEFT_MASTER_DRIVE_PORT = 5;
    public static final int LEFT_SLAVE_DRIVE_PORT = 6;
    public static final int RIGHT_MASTER_DRIVE_PORT = 4;
    public static final int RIGHT_SLAVE_DRIVE_PORT = 3;
    public static final int ELEVATOR_PORT = 8;
    public static final int LATERAL_DRIVE_PORT = 7;
    public static final int MANIPULATOR_ARM_MASTER_PORT = 10;
    public static final int MANIPULATOR_ARM_SLAVE_PORT = 9;
    public static final int CANIFIER_PORT = 60; // TODO: Fix
    public static final int COMPRESSOR_PORT = 0; // TODO: Fix
    public static final int PDP_PORT = 2;
    public static final int FRONT_LIFT_PORT = 15;
    public static final int REAR_LIFT_PORT = 16;


    // Pneumatics
    public static final int SHIFT_FORWARD_CHANNEL = 4;
    public static final int SHIFT_REVERSE_CHANNEL = 5;
    public static final int LATERAL_DRIVE_DEPLOY_FORWARD_CHANNEL = 6; // PCM 1
    public static final int LATERAL_DRIVE_DEPLOY_REVERSE_CHANNEL = 7;
    public static final int MANIPULATOR_HATCH_PUNCH_FORWARD_CHANNEL = 0;
    public static final int MANIPULATOR_HATCH_PUNCH_REVERSE_CHANNEL = 1;
    public static final int MANIPULATOR_DEPLOY_FORWARD_CHANNEL = 2;
    public static final int MANIPULATOR_DEPLOY_REVERSE_CHANNEL = 3;
    public static final int MANIPULATOR_BRAKE_FORWARD_CHANNEL = 6;
    public static final int MANIPULATOR_BRAKE_REVERSE_CHANNEL = 7;
    public static final int LIFT_FRONT_BRAKE_FORWARD_CHANNEL = 4; // PCM 1
    public static final int LIFT_FRONT_BRAKE_REVERSE_CHANNEL = 5;
    public static final int LIFT_REAR_BRAKE_FORWARD_CHANNEL = 2; // PCM 1
    public static final int LIFT_REAR_BRAKE_REVERSE_CHANNEL = 3;
    public static final int PCM_NUMBER_ONE_ID = 0;
    public static final int PCM_NUMBER_TWO_ID = 1;
    // public static final int COMPRESSOR_PCM_ID = 1;

    // PWM
    public static final int LIFT_FRONT_PORT = 8;
    public static final int LIFT_REAR_PORT = 5;
    public static final int LIFT_DRIVE_PORT = 9;
    public static final int MANIPULATOR_FRONT_ROLLER_PORT = 7;
    public static final int MANIPULATOR_REAR_ROLLER_PORT = 6;
    public static final int CAMERA_ROTATOR_PORT = 4;

    // Digital Inputs
    public static final int LIFT_FRONT_FORWARD_LIMIT_PORT = 0;
    public static final int LIFT_REAR_FORWARD_LIMIT_PORT = 1;
    public static final int LIFT_FRONT_REVERSE_LIMIT_PORT = 2;
    public static final int LIFT_REAR_REVERSE_LIMIT_PORT = 3;
    public static final int LIFT_FRONT_HAB_SENSOR_PORT = 0;
    public static final int LIFT_REAR_HAB_SENSOR_PORT = 1;
    /*public static final int LEFT_TWO_SENSOR_PORT = 5;
    public static final int LEFT_ONE_SENSOR_PORT = 4;
    public static final int CENTER_SENSOR_PORT = 3;
    public static final int RIGHT_ONE_SENSOR_PORT = 2;
    public static final int RIGHT_TWO_SENSOR_PORT = 1;*/

    // ----------------------------------------------------------------------------------------------------
    // Input Map
    // ----------------------------------------------------------------------------------------------------
    // Joystick Ports
    public static final int DRIVER_STICK_PORT = 0;
    public static final double DRIVER_STICK_DEAD_ZONE = 0.1;
    public static final int OPERATOR_STICK_PORT = 1;
    public static final double OPERATOR_STICK_DEAD_ZONE = 0.05;

    // Driver Stick Buttons
    public static final int LATERAL_DRIVE_TRIGGER = 1; // 1 is the trigger button
    public static final int SHIFT_BUTTON = 2;
    public static final int STRAIGHT_DRIVE_BUTTON = 3;
    public static final int RESET_GYRO_BUTTON = 5;
    // public static final int LEVEL_THREE_CLIMB_BUTTON = 12;
    // public static final int LEVEL_TWO_CLIMB_BUTTON = 8;
    // public static final int LINE_ALIGN_BUTTON = 12;
    public static final int CANCEL_AUTO_BUTTON = 6;
    public static final int MANUAL_LIFT_BUTTON = 9;
    public static final int MANUAL_FRONT_LIFT_BUTTON = 12;
    public static final int MANUAL_REAR_LIFT_BUTTON = 11;
    public static final int MANUAL_LIFT_DRIVE_BUTTON = 7;
    public static final int EXTEND_FRONT_LIFT_BUTTON = 12;
    public static final int RETRACT_FRONT_LIFT_BUTTON = 10;
    public static final int EXTEND_REAR_LIFT_BUTTON = 11;
    public static final int RETRACT_REAR_LIFT_BUTTON = 9;
    public static final int ROTATE_CAMERA_BUTTON = 7;



    // Operator Stick Buttons
    public static final int MANUAL_ELEVATOR_TRIGGER = 1;
    public static final int TOGGLE_MANIPULATOR_BUTTON = 2;
    public static final int SCORE_HATCH_BUTTON = 5;
    public static final int GRAB_HATCH_BUTTON = 3;
    public static final int SCORE_CARGO_BUTTON = 6;
    public static final int GRAB_CARGO_BUTTON = 4;
    public static final int ELEVATOR_POSITION_ONE_BUTTON = 7;
    public static final int ELEVATOR_POSITION_TWO_BUTTON = 8;
    public static final int ELEVATOR_POSITION_THREE_BUTTON = 9;
    public static final int ELEVATOR_POSITION_FOUR_BUTTON = 10;
    public static final int ELEVATOR_POSITION_FIVE_BUTTON = 11;
    public static final int ELEVATOR_POSITION_SIX_BUTTON = 12;
    public static final int ELEVATOR_CARGO_LOADING_POSITION_BUTTON = 9;
    public static final int ELEVATOR_GROUND_PICKUP_POSITION_BUTTON = 7;
    public static final int MANIPULATOR_ARM_STOW_POSITION_BUTTON = 2;
    public static final int MANIPULATOR_ARM_SCORE_POSITION_BUTTON = 8;
    public static final int MANIPULATOR_ARM_BALL_PICKUP_POSITION_BUTTON = 10;
    public static final int MANIPULATOR_ARM_GROUND_POSITION_BUTTON = 12;

    // ----------------------------------------------------------------------------------------------------
    // Physical Constants
    // ----------------------------------------------------------------------------------------------------
    // TODO: Verify max speed of motors
    public static final int DRIVE_MAX_RPM = 5330;
    public static final int ELEVATOR_MAX_RPM = 5840;
    public static final int LATERAL_DRIVE_MAX_RPM = 19300; // Assuming this is 655
    public static final int LIFT_MAX_RPM = 19300; // Assuming this is 655
    public static final int MANIPULATOR_ARM_MAX_RPM = 90;

    // TODO: Verify gear ratio of LateralDrive and Elevator
    public static final double DRIVE_GEAR_RATIO = 3.66 / 1;
    public static final double ELEVATOR_GEAR_RATIO = 35 / 1;
    public static final double LATERAL_DRIVE_GEAR_RATIO = 64 / 1;
    public static final double LIFT_GEAR_RATIO = 132 / 1;
    public static final double MANIPULATOR_ARM_GEAR_RATIO = 1 / 1;

    // talonSRXCounts = counts * 4
    public static final int DRIVE_COUNTS_PER_REVOLUTION = 4096;
    public static final int ELEVATOR_COUNTS_PER_REVOLUTION = 4096;
    public static final int LATERAL_DRIVE_COUNTS_PER_REVOLUTION = 512;
    public static final int LIFT_COUNTS_PER_REVOLUTION = 512;
    public static final int MANIPULATOR_ARM_COUNTS_PER_REVOLUTION = 4096;

    // ----------------------------------------------------------------------------------------------------
    // Software Constants
    // ----------------------------------------------------------------------------------------------------
    // CTRE Constants
    public static final int PRIMARY_GAINS_SLOT = 0;
    public static final int AUX_GAINS_SLOT = 1;
    public static final int TIMEOUT_MS = 10;

}
