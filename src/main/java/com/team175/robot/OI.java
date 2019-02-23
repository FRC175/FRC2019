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
import com.team175.robot.util.drivers.SingleButton;
import com.team175.robot.util.drivers.AldrinJoystick;
import com.team175.robot.util.drivers.NubDoubleButton;
import edu.wpi.first.wpilibj.buttons.Button;

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
    private final Button mManualFrontLift;
    private final Button mManualRearLift;
    // private final Button mLineAlign;

    // Operator Stick Buttons
    private final Button mManualElevator;
    private final Button mManualManipulatorArm;
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
        mManualLateralDrive = new SingleButton(mDriverStick, Constants.LATERAL_DRIVE_TRIGGER);
        mShift = new SingleButton(mDriverStick, Constants.SHIFT_BUTTON);
        mStraightDrive = new SingleButton(mDriverStick, Constants.STRAIGHT_DRIVE_BUTTON);
        mLevelThreeClimb = new SingleButton(mDriverStick, Constants.LEVEL_THREE_CLIMB_BUTTON);
        mCancelAuto = new SingleButton(mDriverStick, Constants.CANCEL_AUTO_BUTTON);
        mManualFrontLift = new SingleButton(mDriverStick, 9);
        mManualRearLift = new SingleButton(mDriverStick, 10);
        // mLineAlign = new SingleButton(mDriverStick, Constants.LINE_ALIGN_BUTTON);

        // Operator Stick Buttons
        mManualElevator = new SingleButton(mOperatorStick, Constants.MANUAL_ELEVATOR_TRIGGER);
        mManualManipulatorArm = new NubDoubleButton(mOperatorStick, Constants.MANUAL_ELEVATOR_TRIGGER);
        mToggleManipulator = new SingleButton(mOperatorStick, Constants.TOGGLE_MANIPULATOR_BUTTON);
        mScoreHatch = new SingleButton(mOperatorStick, Constants.SCORE_HATCH_BUTTON);
        mGrabHatch = new SingleButton(mOperatorStick, Constants.GRAB_HATCH_BUTTON);
        mScoreCargo = new SingleButton(mOperatorStick, Constants.SCORE_CARGO_BUTTON);
        mScoreCargoFast = new NubDoubleButton(mOperatorStick, Constants.SCORE_CARGO_BUTTON);
        mGrabCargo = new SingleButton(mOperatorStick, Constants.GRAB_CARGO_BUTTON);
        mElevatorPositionOne = new SingleButton(mOperatorStick, Constants.ELEVATOR_POSITION_ONE_BUTTON);
        mElevatorPositionTwo = new SingleButton(mOperatorStick, Constants.ELEVATOR_POSITION_TWO_BUTTON);
        mElevatorPositionThree = new SingleButton(mOperatorStick, Constants.ELEVATOR_POSITION_THREE_BUTTON);
        mElevatorPositionFour = new SingleButton(mOperatorStick, Constants.ELEVATOR_POSITION_FOUR_BUTTON);
        mElevatorPositionFive = new SingleButton(mOperatorStick, Constants.ELEVATOR_POSITION_FIVE_BUTTON);
        mElevatorPositionSix = new SingleButton(mOperatorStick, Constants.ELEVATOR_POSITION_SIX_BUTTON);
        mManipulatorArmScorePosition = new NubDoubleButton(mOperatorStick, Constants.MANIPULATOR_ARM_SCORE_POSITION_BUTTON);
        mManipulatorArmBallPickupPosition = new NubDoubleButton(mOperatorStick, Constants.MANIPULATOR_ARM_BALL_PICKUP_POSITION_BUTTON);
        mManipulatorArmGroundPosition = new NubDoubleButton(mOperatorStick, Constants.MANIPULATOR_ARM_GROUND_POSITION_BUTTON);

        /* Command Assignment */
        // Driver Stick
        mManualLateralDrive.whileHeld(new ManualLateralDrive());
        mShift.whileHeld(new ArcadeDrive(true));
        mStraightDrive.whileHeld(new StraightDrive());
        mLevelThreeClimb.whenPressed(new LevelThreeClimb());
        mCancelAuto.whenPressed(new CancelAuto());
        mManualFrontLift.whileHeld(new ManualFrontLift());
        mManualRearLift.whileHeld(new ManualRearLift());
        // mLineAlign.whenPressed(new LineAlignment());

        // Operator Stick
        mManualElevator.whileHeld(new ManualElevator());
        mManualManipulatorArm.whileHeld(new ManualManipulatorArm());
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
