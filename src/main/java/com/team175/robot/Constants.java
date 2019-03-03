package com.team175.robot;

import com.team175.robot.util.ClosedLoopGains;
import com.team175.robot.util.Transmission;

/**
 * @author Arvind
 */
public final class Constants {

    // Prevent Constants from being instantiated
    private Constants() {
    }

    /* Hardware Map */
    // CAN Ports
    public static final int LEFT_MASTER_DRIVE_PORT = 5;
    public static final int LEFT_SLAVE_DRIVE_PORT = 6;
    public static final int RIGHT_MASTER_DRIVE_PORT = 4;
    public static final int RIGHT_SLAVE_DRIVE_PORT = 3;
    public static final int ELEVATOR_PORT = 8;
    public static final int LATERAL_DRIVE_PORT = 7;
    public static final int MANIPULATOR_ARM_MASTER_PORT = 10;
    public static final int MANIPULATOR_ARM_SLAVE_PORT = 9;
    public static final int CANIFIER_PORT = -1; // TODO: Fix

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
    public static final int COMPRESSOR_PCM_ID = 1;

    // PWM
    public static final int LIFT_FRONT_PORT = 8;
    public static final int LIFT_REAR_PORT = 5;
    public static final int LIFT_DRIVE_PORT = 9;
    public static final int MANIPULATOR_FRONT_ROLLER = 7;
    public static final int MANIPULATOR_REAR_ROLLER = 6;

    // Digital Inputs
    public static final int LIFT_FRONT_FORWARD_LIMIT_PORT = 0;
    public static final int LIFT_REAR_FORWARD_LIMIT_PORT = 1;
    public static final int LIFT_FRONT_REVERSE_LIMIT_PORT = 2;
    public static final int LIFT_REAR_REVERSE_LIMIT_PORT = 3;
    public static final int LIFT_FRONT_HAB_SENSOR_PORT = 4;
    public static final int LIFT_REAR_HAB_SENSOR_PORT = 5;
    /*public static final int LEFT_TWO_SENSOR_PORT = 5;
    public static final int LEFT_ONE_SENSOR_PORT = 4;
    public static final int CENTER_SENSOR_PORT = 3;
    public static final int RIGHT_ONE_SENSOR_PORT = 2;
    public static final int RIGHT_TWO_SENSOR_PORT = 1;*/

    /* Input Map */
    // Joystick Ports
    public static final int DRIVER_STICK_PORT = 0;
    public static final double DRIVER_STICK_DEAD_ZONE = 0.05;
    public static final int OPERATOR_STICK_PORT = 1;
    public static final double OPERATOR_STICK_DEAD_ZONE = 0.05;

    // Driver Stick Buttons
    public static final int LATERAL_DRIVE_TRIGGER = 1; // 1 is the trigger button
    public static final int SHIFT_BUTTON = 2;
    public static final int STRAIGHT_DRIVE_BUTTON = 3;
    public static final int LEVEL_THREE_CLIMB_BUTTON = 12;
    public static final int CANCEL_AUTO_BUTTON = 6;
    // public static final int LINE_ALIGN_BUTTON = 12;

    // Operator Stick Buttons
    public static final int MANUAL_ELEVATOR_TRIGGER = 1;
    public static final int TOGGLE_MANIPULATOR_BUTTON = 2;
    public static final int SCORE_HATCH_BUTTON = 5;
    public static final int GRAB_HATCH_BUTTON = 3;
    public static final int SCORE_CARGO_BUTTON = 6;
    public static final int GRAB_CARGO_BUTTON = 4;
    public static final int ELEVATOR_POSITION_ONE_BUTTON = 7;
    public static final int ELEVATOR_POSITION_TWO_BUTTON = 9;
    public static final int ELEVATOR_POSITION_THREE_BUTTON = 11;
    public static final int ELEVATOR_POSITION_FOUR_BUTTON = 8;
    public static final int ELEVATOR_POSITION_FIVE_BUTTON = 10;
    public static final int ELEVATOR_POSITION_SIX_BUTTON = 12;
    public static final int MANIPULATOR_ARM_STOW_POSITION_BUTTON = 2;
    public static final int MANIPULATOR_ARM_SCORE_POSITION_BUTTON = 8;
    public static final int MANIPULATOR_ARM_BALL_PICKUP_POSITION_BUTTON = 10;
    public static final int MANIPULATOR_ARM_GROUND_POSITION_BUTTON = 12;

    /* Physical Constants */
    // TODO: Clean up!
    // TODO: Consider replacing empirical with actual velocity

    // TODO: Verify max speed of motors
    public static final int DRIVE_MAX_RPM = 5330;
    public static final int ELEVATOR_MAX_RPM = 5840;
    public static final int LATERAL_DRIVE_MAX_RPM = 19300; // Assuming this is 655
    public static final int MANIPULATOR_ARM_MAX_RPM = 90;

    // TODO: Determine gear ratio of each motor
    public static final double DRIVE_GEAR_RATIO = 3.66 / 1;
    public static final double ELEVATOR_GEAR_RATIO = 1;
    public static final double LATERAL_DRIVE_GEAR_RATIO = 1;
    public static final double MANIPULATOR_ARM_GEAR_RATIO = 1 / 1;

    // TODO: Verify manipulator arm encoder counts per revolution
    // talonSRXCounts = counts * 4
    public static final int DRIVE_COUNTS_PER_REVOLUTION = 4096;
    public static final int ELEVATOR_COUNTS_PER_REVOLUTION = 4096;
    public static final int LATERAL_DRIVE_COUNTS_PER_REVOLUTION = 512;
    public static final int MANIPULATOR_ARM_COUNTS_PER_REVOLUTION = 4096;

    // TODO: Perhaps measure using Phoenix Tuner
    /*public static final int DRIVE_MAX_VELOCITY = 3200;
    public static final int ELEVATOR_MAX_VELOCITY = 0;
    public static final int LATERAL_DRIVE_MAX_VELOCITY = 0;
    public static final int MANIPULATOR_ARM_MAX_VELOCITY = 0;*/

    /*private static final Transmission DRIVE_TRANSMISSION = new Transmission(DRIVE_MAX_VELOCITY,
            DRIVE_COUNTS_PER_REVOLUTION);
    private static final Transmission ELEVATOR_TRANSMISSION = new Transmission(ELEVATOR_MAX_RPM,
            ELEVATOR_COUNTS_PER_REVOLUTION, ELEVATOR_GEAR_RATIO);
    private static final Transmission LATERAL_DRIVE_TRANSMISSION = new Transmission(LATERAL_DRIVE_MAX_RPM,
            LATERAL_DRIVE_COUNTS_PER_REVOLUTION, LATERAL_DRIVE_GEAR_RATIO);
    private static final Transmission MANIPULATOR_ARM_TRANSMISSION = new Transmission(MANIPULATOR_ARM_MAX_RPM,
            MANIPULATOR_ARM_COUNTS_PER_REVOLUTION, MANIPULATOR_ARM_GEAR_RATIO);*/

    /* Software Constants */
    // Closed Loop Gains
    public static final double COMPETITION_LEFT_DRIVE_KP = 0.1;
    public static final double COMPETITION_LEFT_DRIVE_KD = 0.0;
    public static final int COMPETITION_LEFT_DRIVE_ACCELERATION = 0;
    public static final int COMPETITION_LEFT_DRIVE_CRUISE_VELOCITY = 0;

    public static final double PRACTICE_LEFT_DRIVE_KP = 0.1;
    public static final double PRACTICE_LEFT_DRIVE_KD = 0.0;
    public static final int PRACTICE_LEFT_DRIVE_ACCELERATION = 0;
    public static final int PRACTICE_LEFT_DRIVE_CRUISE_VELOCITY = 0;

    public static final double COMPETITION_RIGHT_DRIVE_KP = 0.1;
    public static final double COMPETITION_RIGHT_DRIVE_KD = 0.0;
    public static final int COMPETITION_RIGHT_DRIVE_ACCELERATION = 0;
    public static final int COMPETITION_RIGHT_DRIVE_CRUISE_VELOCITY = 0;

    public static final double PRACTICE_RIGHT_DRIVE_KP = 0.1;
    public static final double PRACTICE_RIGHT_DRIVE_KD = 0.0;
    public static final int PRACTICE_RIGHT_DRIVE_ACCELERATION = 0;
    public static final int PRACTICE_RIGHT_DRIVE_CRUISE_VELOCITY = 0;

    public static final double COMPETITION_ELEVATOR_KP = 0.1;
    public static final double COMPETITION_ELEVATOR_KD = 0.0;
    public static final int COMPETITION_ELEVATOR_ACCELERATION = 0;
    public static final int COMPETITION_ELEVATOR_CRUISE_VELOCITY = 0;

    public static final double PRACTICE_ELEVATOR_KP = 0.1;
    public static final double PRACTICE_ELEVATOR_KD = 0.0;
    public static final int PRACTICE_ELEVATOR_ACCELERATION = 0;
    public static final int PRACTICE_ELEVATOR_CRUISE_VELOCITY = 0;

    public static final double COMPETITION_LATERAL_DRIVE_KP = 0.1;
    public static final double COMPETITION_LATERAL_DRIVE_KD = 0.0;
    public static final int COMPETITION_LATERAL_DRIVE_ACCELERATION = 0;
    public static final int COMPETITION_LATERAL_DRIVE_CRUISE_VELOCITY = 0;

    public static final double PRACTICE_LATERAL_DRIVE_KP = 0.1;
    public static final double PRACTICE_LATERAL_DRIVE_KD = 0.0;
    public static final int PRACTICE_LATERAL_DRIVE_ACCELERATION = 0;
    public static final int PRACTICE_LATERAL_DRIVE_CRUISE_VELOCITY = 0;

    public static final double COMPETITION_MANIPULATOR_ARM_KP = 0.1;
    public static final double COMPETITION_MANIPULATOR_ARM_KD = 0.0;
    public static final int COMPETITION_MANIPULATOR_ARM_ACCELERATION = 0;
    public static final int COMPETITION_MANIPULATOR_ARM_CRUISE_VELOCITY = 0;

    public static final double PRACTICE_MANIPULATOR_ARM_KP = 0.1;
    public static final double PRACTICE_MANIPULATOR_ARM_KD = 0.0;
    public static final int PRACTICE_MANIPULATOR_ARM_ACCELERATION = 0;
    public static final int PRACTICE_MANIPULATOR_ARM_CRUISE_VELOCITY = 0;

    /*public static final ClosedLoopGains LEFT_DRIVE_GAINS = new ClosedLoopGains(0.1, 0, 0,
            DRIVE_TRANSMISSION.getKf(), DRIVE_TRANSMISSION.getVelocity() / 2,
            DRIVE_TRANSMISSION.getVelocity() / 2);
    public static final ClosedLoopGains RIGHT_DRIVE_GAINS = new ClosedLoopGains(0.1, 0, 0,
            DRIVE_TRANSMISSION.getKf(), DRIVE_TRANSMISSION.getVelocity() / 2,
            DRIVE_TRANSMISSION.getVelocity() / 2);
    public static final ClosedLoopGains ELEVATOR_GAINS = new ClosedLoopGains(0.1, 0, 0,
            ELEVATOR_TRANSMISSION.getKf(), ELEVATOR_TRANSMISSION.getVelocity() / 2,
            ELEVATOR_TRANSMISSION.getVelocity() / 2);
    public static final ClosedLoopGains LATERAL_DRIVE_GAINS = new ClosedLoopGains(0.1, 0, 0,
            LATERAL_DRIVE_TRANSMISSION.getKf(), LATERAL_DRIVE_TRANSMISSION.getVelocity() / 2,
            LATERAL_DRIVE_TRANSMISSION.getVelocity() / 2);
    public static final ClosedLoopGains MANIPULATOR_ARM_GAINS = new ClosedLoopGains(0.1, 0, 0,
            MANIPULATOR_ARM_TRANSMISSION.getKf(), MANIPULATOR_ARM_TRANSMISSION.getVelocity() / 2,
            MANIPULATOR_ARM_TRANSMISSION.getVelocity() / 2);*/

    // CTRE Constants
    public static final int SLOT_INDEX = 0;
    public static final int AUX_SLOT_INDEX = 1;
    public static final int TIMEOUT_MS = 10;
    public static final int ALLOWED_POSITION_DEVIATION = 10;
    public static final double QUICK_TURN_THRESHOLD = 0.2; // Used for Cheesy Drive
    public static final double RAMP_TIME = 0;
    public static final int S_CURVE_STRENGTH = 4; // Half smoothing (0-8)

}
