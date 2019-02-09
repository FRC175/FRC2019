package com.team175.robot;

import com.team175.robot.util.AldrinMath;
import com.team175.robot.util.tuning.ClosedLoopGains;

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
    public static final int MANIPULATOR_HATCH_PUNCH_CHANNEL = 5;
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
    public static final double DRIVER_STICK_DEAD_ZONE = 0.05;
    public static final int OPERATOR_STICK_PORT = 1;
    public static final double OPERATOR_STICK_DEAD_ZONE = 0.05;

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
    public static final int TOGGLE_PUNCH_HATCH_BUTTON = 2;
    public static final int MANUAL_ARM_BUTTON = 12;

    /* Physical Constants */

    // TODO: Consider replacing empirical with actual velocity

    // TODO: Verify max speed of motors
    private static final int DRIVE_MAX_RPM = 5330;
    private static final int ELEVATOR_MAX_RPM = 154; // May be 18730 if 775 is used
    private static final int LATERAL_DRIVE_MAX_RPM = 19300; // Assuming this is 775
    private static final int MANIPULATOR_ARM_MAX_RPM = 18730; // Assuming this is 775

    // TODO: Determine gear ratio of each motor
    private static final double DRIVE_GEAR_RATIO = 3.66 / 1.0;
    private static final double ELEVATOR_GEAR_RATIO = 1;
    private static final double LATERAL_DRIVE_GEAR_RATIO = 1;
    private static final double MANIPULATOR_ARM_GEAR_RATIO = 1;

    // TODO: Verify manipulator arm encoder counts per revolution
    // talonSRXCounts = counts * 4
    private static final int DRIVE_COUNTS_PER_REVOLUTION = 4096;
    private static final int ELEVATOR_COUNTS_PER_REVOLUTION = 512;
    private static final int LATERAL_DRIVE_COUNTS_PER_REVOLUTION = 512;
    private static final int MANIPULATOR_ARM_COUNTS_PER_REVOLUTION = 4096;

    // TODO: Perhaps measure using Phoenix Tuner
    /*public static final int DRIVE_MAX_VELOCITY = AldrinMath.calculateEmpiricalVelocity(DRIVE_MAX_RPM,
            DRIVE_COUNTS_PER_REVOLUTION, DRIVE_GEAR_RATIO);*/
    public static final int DRIVE_MAX_VELOCITY = 3200;
    public static final int ELEVATOR_MAX_VELOCITY = AldrinMath.calculateEmpiricalVelocity(ELEVATOR_MAX_RPM,
            ELEVATOR_COUNTS_PER_REVOLUTION, ELEVATOR_GEAR_RATIO);
    public static final int LATERAL_DRIVE_MAX_VELOCITY = AldrinMath.calculateEmpiricalVelocity(LATERAL_DRIVE_MAX_RPM,
            LATERAL_DRIVE_COUNTS_PER_REVOLUTION, LATERAL_DRIVE_GEAR_RATIO);
    public static final int MANIPULATOR_ARM_MAX_VELOCITY = AldrinMath.calculateEmpiricalVelocity(MANIPULATOR_ARM_MAX_RPM,
            MANIPULATOR_ARM_COUNTS_PER_REVOLUTION, MANIPULATOR_ARM_GEAR_RATIO);

    /* Software Constants */
    // Closed Loop Gains
    public static final ClosedLoopGains LEFT_DRIVE_GAINS = new ClosedLoopGains(0.1, 0, 0.2,
            AldrinMath.calculateKf(DRIVE_MAX_VELOCITY), DRIVE_MAX_VELOCITY / 2,
            DRIVE_MAX_VELOCITY / 2);
    public static final ClosedLoopGains RIGHT_DRIVE_GAINS = new ClosedLoopGains(0.25, 0, 0.5,
            AldrinMath.calculateKf(DRIVE_MAX_VELOCITY), DRIVE_MAX_VELOCITY / 2,
            DRIVE_MAX_VELOCITY / 2);
    public static final ClosedLoopGains ELEVATOR_GAINS = new ClosedLoopGains(0.1, 0, 0,
            AldrinMath.calculateAltKf(ELEVATOR_MAX_VELOCITY), ELEVATOR_MAX_VELOCITY / 2,
            ELEVATOR_MAX_VELOCITY / 2);
    public static final ClosedLoopGains LATERAL_DRIVE_GAINS = new ClosedLoopGains(0.1, 0, 0,
            AldrinMath.calculateKf(LATERAL_DRIVE_MAX_VELOCITY), LATERAL_DRIVE_MAX_VELOCITY / 2,
            LATERAL_DRIVE_MAX_VELOCITY / 2);
    public static final ClosedLoopGains MANIPULATOR_ARM_GAINS = new ClosedLoopGains(0.1, 0, 0,
            AldrinMath.calculateKf(MANIPULATOR_ARM_MAX_VELOCITY), MANIPULATOR_ARM_MAX_VELOCITY / 2,
            MANIPULATOR_ARM_MAX_VELOCITY / 2);

    // CTRE Constants
    public static final int SLOT_INDEX = 0;
    public static final int AUX_SLOT_INDEX = 1;
    public static final int PID_LOOP_INDEX = 0;
    public static final int TIMEOUT_MS = 10;
    public static final int ALLOWED_POSITION_DEVIATION = 10;

}
