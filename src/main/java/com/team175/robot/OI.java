/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.commands.auto.CancelAuto;
import com.team175.robot.commands.drive.ArcadeDrive;
import com.team175.robot.commands.drive.ResetGyro;
import com.team175.robot.commands.drive.StraightDrive;
import com.team175.robot.commands.elevator.ControlElevator;
import com.team175.robot.commands.elevator.ElevatorToPosition;
import com.team175.robot.commands.lateraldrive.ControlLateralDrive;
import com.team175.robot.commands.lift.ControlFrontLift;
import com.team175.robot.commands.lift.ControlLiftDrive;
import com.team175.robot.commands.lift.ControlRearLift;
import com.team175.robot.commands.manipulator.ControlManipulatorArm;
import com.team175.robot.commands.manipulator.ManipulateGamePieces;
import com.team175.robot.commands.manipulator.ManipulatorArmToPosition;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.positions.LiftPosition;
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

    // Joysticks
    private final AldrinJoystick mDriverStick;
    private final AldrinJoystick mOperatorStick;

    // Driver Stick Buttons
    private final Button mManualLateralDrive;
    private final Button mShift;
    private final Button mStraightDrive;
    private final Button mResetGyro;
    // private final Button mLevelThreeClimb;
    // private final Button mLevelTwoClimb;
    private final Button mCancelAuto;
    /*private final Button mManualLift;
    private final Button mManualFrontLift;
    private final Button mManualRearLift;*/
    private final Button mManualLiftDrive;
    private final Button mExtendFrontLift;
    private final Button mRetractFrontLift;
    private final Button mExtendRearLift;
    private final Button mRetractRearLift;
    // private final Button mLineAlign;

    // Operator Stick Buttons
    private final Button mManualElevator;
    private final Button mManualManipulatorArm;
    // private final Button mToggleManipulator;
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
    private final Button mElevatorCargoLoadingPosition;
    private final Button mElevatorGroundPickupPosition;
    private final Button mManipulatorArmStowPosition;
    private final Button mManipulatorArmScorePosition;
    private final Button mManipulatorArmBallPickupPosition;
    private final Button mManipulatorArmGroundPosition;

    private static OI sInstance;

    public static OI getInstance() {
        if (sInstance == null) {
            sInstance = new OI();
        }

        return sInstance;
    }

    private OI() {
        // ----------------------------------------------------------------------------------------------------
        // Instantiations
        // ----------------------------------------------------------------------------------------------------
        // Joystick
        mDriverStick = new AldrinJoystick(Constants.DRIVER_STICK_PORT, Constants.DRIVER_STICK_DEAD_ZONE);
        mOperatorStick = new AldrinJoystick(Constants.OPERATOR_STICK_PORT, Constants.OPERATOR_STICK_DEAD_ZONE);

        // Driver Stick Buttons
        mManualLateralDrive = new SingleButton(mDriverStick, Constants.LATERAL_DRIVE_TRIGGER);
        mShift = new SingleButton(mDriverStick, Constants.SHIFT_BUTTON);
        mStraightDrive = new SingleButton(mDriverStick, Constants.STRAIGHT_DRIVE_BUTTON);
        mResetGyro = new SingleButton(mDriverStick, Constants.RESET_GYRO_BUTTON);
        // mLevelThreeClimb = new SingleButton(mDriverStick, Constants.LEVEL_THREE_CLIMB_BUTTON);
        // mLevelTwoClimb = new SingleButton(mDriverStick, Constants.LEVEL_TWO_CLIMB_BUTTON);
        mCancelAuto = new SingleButton(mDriverStick, Constants.CANCEL_AUTO_BUTTON);
        /*mManualLift = new SingleButton(mDriverStick, Constants.MANUAL_LIFT_BUTTON);
        mManualFrontLift = new SingleButton(mDriverStick, Constants.MANUAL_FRONT_LIFT_BUTTON);
        mManualRearLift = new SingleButton(mDriverStick, Constants.MANUAL_REAR_LIFT_BUTTON);*/
        mManualLiftDrive = new SingleButton(mDriverStick, Constants.MANUAL_LIFT_DRIVE_BUTTON);
        mExtendFrontLift = new SingleButton(mDriverStick, Constants.EXTEND_FRONT_LIFT_BUTTON);
        mRetractFrontLift = new SingleButton(mDriverStick, Constants.RETRACT_FRONT_LIFT_BUTTON);
        mExtendRearLift = new SingleButton(mDriverStick, Constants.EXTEND_REAR_LIFT_BUTTON);
        mRetractRearLift = new SingleButton(mDriverStick, Constants.RETRACT_REAR_LIFT_BUTTON);
        // mLineAlign = new SingleButton(mDriverStick, Constants.LINE_ALIGN_BUTTON);

        // Operator Stick Buttons
        mManualElevator = new SingleButton(mOperatorStick, Constants.MANUAL_ELEVATOR_TRIGGER);
        mManualManipulatorArm = new NubDoubleButton(mOperatorStick, Constants.MANUAL_ELEVATOR_TRIGGER);
        // mToggleManipulator = new SingleButton(mOperatorStick, Constants.TOGGLE_MANIPULATOR_BUTTON);
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
        mElevatorCargoLoadingPosition = new NubDoubleButton(mOperatorStick, Constants.ELEVATOR_CARGO_LOADING_POSITION_BUTTON);
        mElevatorGroundPickupPosition = new NubDoubleButton(mOperatorStick, Constants.ELEVATOR_GROUND_PICKUP_POSITION_BUTTON);
        mManipulatorArmStowPosition = new SingleButton(mOperatorStick, Constants.MANIPULATOR_ARM_STOW_POSITION_BUTTON);
        mManipulatorArmScorePosition = new NubDoubleButton(mOperatorStick, Constants.MANIPULATOR_ARM_SCORE_POSITION_BUTTON);
        mManipulatorArmBallPickupPosition = new NubDoubleButton(mOperatorStick, Constants.MANIPULATOR_ARM_BALL_PICKUP_POSITION_BUTTON);
        mManipulatorArmGroundPosition = new NubDoubleButton(mOperatorStick, Constants.MANIPULATOR_ARM_GROUND_POSITION_BUTTON);

        // ----------------------------------------------------------------------------------------------------
        // Command Assignment
        // ----------------------------------------------------------------------------------------------------
        // Driver Stick
        mManualLateralDrive.whileHeld(new ControlLateralDrive());
        mShift.whileHeld(new ArcadeDrive(true));
        // mShift.whileHeld(new CheesyDrive(true));
        mStraightDrive.whileHeld(new StraightDrive());
        mResetGyro.whenPressed(new ResetGyro());
        // mLevelThreeClimb.whenPressed(new LevelThreeClimb());
        // mLevelTwoClimb.whenPressed(new LevelTwoClimb());
        mCancelAuto.whenPressed(new CancelAuto());
        /*mManualLift.whileHeld(new ControlLift());
        mManualFrontLift.whileHeld(new ControlFrontLift());
        mManualRearLift.whileHeld(new ControlRearLift());*/
        mManualLiftDrive.whileHeld(new ControlLiftDrive());
        mExtendFrontLift.whileHeld(new ControlFrontLift(LiftPosition.EXTEND));
        mRetractFrontLift.whileHeld(new ControlFrontLift(LiftPosition.RETRACT));
        mExtendRearLift.whileHeld(new ControlRearLift(LiftPosition.EXTEND));
        mRetractRearLift.whileHeld(new ControlRearLift(LiftPosition.RETRACT));
        // mLineAlign.whenPressed(new LineAlignment());

        // Operator Stick
        mManualElevator.whileHeld(new ControlElevator());
        mManualManipulatorArm.whileHeld(new ControlManipulatorArm());
        // mToggleManipulator.toggleWhenPressed(new ToggleManipulatorDeploy());
        mScoreHatch.whileHeld(new ManipulateGamePieces(ManipulatorRollerPosition.SCORE_HATCH));
        mGrabHatch.whileHeld(new ManipulateGamePieces(ManipulatorRollerPosition.GRAB_HATCH));
        mScoreCargo.whileHeld(new ManipulateGamePieces(ManipulatorRollerPosition.SCORE_CARGO));
        mScoreCargoFast.whileHeld(new ManipulateGamePieces(ManipulatorRollerPosition.SCORE_CARGO_FAST));
        mGrabCargo.whileHeld(new ManipulateGamePieces(ManipulatorRollerPosition.GRAB_CARGO));
        mElevatorPositionOne.whenPressed(new ElevatorToPosition(ElevatorPosition.HATCH_LEVEL_ONE));
        mElevatorPositionTwo.whenPressed(new ElevatorToPosition(ElevatorPosition.CARGO_LEVEL_ONE));
        mElevatorPositionThree.whenPressed(new ElevatorToPosition(ElevatorPosition.HATCH_LEVEL_TWO));
        mElevatorPositionFour.whenPressed(new ElevatorToPosition(ElevatorPosition.CARGO_LEVEL_TWO));
        mElevatorPositionFive.whenPressed(new ElevatorToPosition(ElevatorPosition.HATCH_LEVEL_THREE));
        mElevatorPositionSix.whenPressed(new ElevatorToPosition(ElevatorPosition.CARGO_LEVEL_THREE));
        mElevatorCargoLoadingPosition.whenPressed(new ElevatorToPosition(ElevatorPosition.CARGO_LOADING));
        mElevatorGroundPickupPosition.whenPressed(new ElevatorToPosition(ElevatorPosition.GROUND_PICKUP));
        mManipulatorArmStowPosition.whenPressed(new ManipulatorArmToPosition(ManipulatorArmPosition.STOW));
        mManipulatorArmScorePosition.whenPressed(new ManipulatorArmToPosition(ManipulatorArmPosition.SCORE));
        mManipulatorArmBallPickupPosition.whenPressed(new ManipulatorArmToPosition(ManipulatorArmPosition.BALL_PICKUP));
        mManipulatorArmGroundPosition.whenPressed(new ManipulatorArmToPosition(ManipulatorArmPosition.HATCH_PICKUP));
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
