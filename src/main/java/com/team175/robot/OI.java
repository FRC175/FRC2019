/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.auto.LevelThreeClimb;
import com.team175.robot.commands.*;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.util.drivers.AldrinJoystick;
import com.team175.robot.util.drivers.NubButton;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * @author Arvind
 */
public final class OI {

    /* Declarations */
    // Joysticks
    private final AldrinJoystick mDriverStick;
    private final AldrinJoystick mOperatorStick;

    // Driver Stick Buttons
    private final Button mManualLateralDrive;
    private final Button mShift;
    private final Button mStraightDrive;
    private final Button mLevelThreeClimb;
    private final Button mCancelAuto;
    // private final Button mLineAlign;

    // Operator Stick Buttons
    private final Button mManualElevator;
    private final Button mToggleManipulator;
    private final Button mScoreHatch;
    private final Button mGrabHatch;
    private final Button mScoreCargo;
    private final Button mScoreCargoFast;
    private final Button mGrabCargo;
    private final Button mElevatorPositionOne;
    private final Button mElevatorPositionTwo;
    private final Button mElevatorPositionThree;
    private final Button mElevatorPositionFour;
    private final Button mElevatorPositionFive;
    private final Button mElevatorPositionSix;
    private final Button mManipulatorArmScorePosition;
    private final Button mManipulatorArmBallPickupPosition;
    private final Button mManipulatorArmGroundPosition;

    // Singleton Instance
    private static OI sInstance;

    public static OI getInstance() {
        if (sInstance == null) {
            sInstance = new OI();
        }

        return sInstance;
    }

    private OI() {
        /* Instantiations */
        // Joystick
        mDriverStick = new AldrinJoystick(Constants.DRIVER_STICK_PORT, Constants.DRIVER_STICK_DEAD_ZONE);
        mOperatorStick = new AldrinJoystick(Constants.OPERATOR_STICK_PORT, Constants.OPERATOR_STICK_DEAD_ZONE);

        // Driver Stick Buttons
        mManualLateralDrive = new JoystickButton(mDriverStick, Constants.LATERAL_DRIVE_TRIGGER);
        mShift = new JoystickButton(mDriverStick, Constants.SHIFT_BUTTON);
        mStraightDrive = new JoystickButton(mDriverStick, Constants.STRAIGHT_DRIVE_BUTTON);
        mLevelThreeClimb = new JoystickButton(mDriverStick, Constants.LEVEL_THREE_CLIMB_BUTTON);
        mCancelAuto = new JoystickButton(mDriverStick, Constants.CANCEL_AUTO_BUTTON);
        // mLineAlign = new JoystickButton(mDriverStick, Constants.LINE_ALIGN_BUTTON);

        // Operator Stick Buttons
        mManualElevator = new JoystickButton(mOperatorStick, Constants.MANUAL_ELEVATOR_TRIGGER);
        mToggleManipulator = new JoystickButton(mOperatorStick, Constants.TOGGLE_MANIPULATOR_BUTTON);
        mScoreHatch = new JoystickButton(mOperatorStick, Constants.SCORE_HATCH_BUTTON);
        mGrabHatch = new JoystickButton(mOperatorStick, Constants.GRAB_HATCH_BUTTON);
        mScoreCargo = new JoystickButton(mOperatorStick, Constants.SCORE_CARGO_BUTTON);
        mScoreCargoFast = new NubButton(mOperatorStick, Constants.SCORE_CARGO_BUTTON);
        mGrabCargo = new JoystickButton(mOperatorStick, Constants.GRAB_CARGO_BUTTON);
        mElevatorPositionOne = new JoystickButton(mOperatorStick, Constants.ELEVATOR_POSITION_ONE_BUTTON);
        mElevatorPositionTwo = new JoystickButton(mOperatorStick, Constants.ELEVATOR_POSITION_TWO_BUTTON);
        mElevatorPositionThree = new JoystickButton(mOperatorStick, Constants.ELEVATOR_POSITION_THREE_BUTTON);
        mElevatorPositionFour = new JoystickButton(mOperatorStick, Constants.ELEVATOR_POSITION_FOUR_BUTTON);
        mElevatorPositionFive = new JoystickButton(mOperatorStick, Constants.ELEVATOR_POSITION_FIVE_BUTTON);
        mElevatorPositionSix = new JoystickButton(mOperatorStick, Constants.ELEVATOR_POSITION_SIX_BUTTON);
        mManipulatorArmScorePosition = new NubButton(mOperatorStick, Constants.MANIPULATOR_ARM_SCORE_POSITION_BUTTON);
        mManipulatorArmBallPickupPosition = new NubButton(mOperatorStick, Constants.MANIPULATOR_ARM_BALL_PICKUP_POSITION_BUTTON);
        mManipulatorArmGroundPosition = new NubButton(mOperatorStick, Constants.MANIPULATOR_ARM_GROUND_POSITION_BUTTON);

        /* Command Assignment */
        // Driver Stick
        mManualLateralDrive.whileHeld(new ManualLateralDrive());
        mShift.whileHeld(new ArcadeDrive(true));
        mStraightDrive.whileHeld(new StraightDrive());
        mLevelThreeClimb.whenPressed(new LevelThreeClimb());
        mCancelAuto.whenPressed(new CancelAuto());
        // mLineAlign.whenPressed(new LineAlignment());

        // Operator Stick
        mManualElevator.whileHeld(new ManualElevator());
        mToggleManipulator.toggleWhenPressed(new ToggleManipulatorDeploy());
        mScoreHatch.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.SCORE_HATCH));
        mGrabHatch.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.GRAB_HATCH));
        mScoreCargo.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.SCORE_CARGO));
        mScoreCargoFast.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.SCORE_CARGO_FAST));
        mGrabCargo.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.GRAB_CARGO));
        mElevatorPositionOne.whenPressed(new PositionElevator(ElevatorPosition.HATCH_LEVEL_ONE));
        mElevatorPositionTwo.whenPressed(new PositionElevator(ElevatorPosition.CARGO_LEVEL_ONE));
        mElevatorPositionThree.whenPressed(new PositionElevator(ElevatorPosition.HATCH_LEVEL_TWO));
        mElevatorPositionFour.whenPressed(new PositionElevator(ElevatorPosition.CARGO_LEVEL_TWO));
        mElevatorPositionFive.whenPressed(new PositionElevator(ElevatorPosition.HATCH_LEVEL_THREE));
        mElevatorPositionSix.whenPressed(new PositionElevator(ElevatorPosition.CARGO_LEVEL_THREE));
        mManipulatorArmScorePosition.whenPressed(new PositionManipulatorArm(ManipulatorArmPosition.SCORE));
        mManipulatorArmBallPickupPosition.whenPressed(new PositionManipulatorArm(ManipulatorArmPosition.BALL_PICKUP));
        mManipulatorArmGroundPosition.whenPressed(new PositionManipulatorArm(ManipulatorArmPosition.GROUND));
    }

    public double getDriverStickX() {
        return mDriverStick.getRawAxis(0);
    }

    public double getDriverStickY() {
        return -mDriverStick.getRawAxis(1);
    }

    public double getOperatorStickX() {
        return mOperatorStick.getRawAxis(0);
    }

    public double getOperatorStickY() {
        return -mOperatorStick.getRawAxis(1);
    }

}
