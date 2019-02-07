/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.commands.*;
import com.team175.robot.positions.ManipulatorRollerPosition;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * @author Arvind
 */
public final class OI {

    /* Declarations */
    // Joysticks
    private final Joystick mDriverStick;
    private final Joystick mOperatorStick;

    // Driver Stick Buttons
    private final Button mManualLateralDrive;
    private final Button mShift;
    private final Button mStraightDrive;
    private final Button mManualLift;
    // private final Button mManualLiftDrive;
    // private final Button mLineAlign;

    // Operator Stick Buttons
    // TODO: Add automated arm and elevator positions
    private final Button mManualElevator;
    private final Button mToggleManipulator;
    private final Button mScoreHatch;
    private final Button mGrabHatch;
    private final Button mScoreCargo;
    private final Button mGrabCargo;
    private final Button mTogglePunchHatch;
    private final Button mManualManipulatorArm;

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
        mDriverStick = new Joystick(Constants.DRIVER_STICK_PORT);
        mOperatorStick = new Joystick(Constants.OPERATOR_STICK_PORT);

        // Driver Stick Buttons
        mManualLateralDrive = new JoystickButton(mDriverStick, Constants.LATERAL_DRIVE_TRIGGER); // 1 is the trigger button
        mShift = new JoystickButton(mDriverStick, Constants.SHIFT_BUTTON);
        mStraightDrive = new JoystickButton(mDriverStick, Constants.STRAIGHT_DRIVE_BUTTON);
        mManualLift = new JoystickButton(mDriverStick, Constants.MANUAL_LIFT_BUTTON);
        // mManualLiftDrive = new JoystickButton(mDriverStick, Constants.MANUAL_LIFT_DRIVE_BUTTON);
        // mLineAlign = new JoystickButton(mDriverStick, Constants.LINE_ALIGN_BUTTON);

        // Operator Stick Buttons
        mManualElevator = new JoystickButton(mOperatorStick, Constants.MANUAL_ELEVATOR_TRIGGER);
        mToggleManipulator = new JoystickButton(mOperatorStick, Constants.TOGGLE_MANIPULATOR_BUTTON);
        mScoreHatch = new JoystickButton(mOperatorStick, Constants.SCORE_HATCH_BUTTON);
        mGrabHatch = new JoystickButton(mOperatorStick, Constants.GRAB_HATCH_BUTTON);
        mScoreCargo = new JoystickButton(mOperatorStick, Constants.SCORE_CARGO_BUTTON);
        mGrabCargo = new JoystickButton(mOperatorStick, Constants.GRAB_CARGO_BUTTON);
        mTogglePunchHatch = new JoystickButton(mOperatorStick, Constants.TOGGLE_PUNCH_HATCH_BUTTON);
        mManualManipulatorArm = new JoystickButton(mOperatorStick, Constants.MANUAL_ARM_BUTTON);

        /* Command Assignment */
        // Driver Stick
        mManualLateralDrive.whileHeld(new ManualLateralDrive());
        mShift.whileHeld(new ManualArcadeDrive(true));
        // mStraightDrive.whileHeld(new StraightDrive());
        mManualLift.whileHeld(new ManualLift());
        // mManualLiftDrive.whileHeld(new LiftDrive());
        // mLineAlign.whenPressed(new LineAlignment());

        // Operator Stick
        mManualElevator.whileHeld(new ManualElevator());
        mToggleManipulator.toggleWhenPressed(new ToggleManipulatorDeploy());
        mScoreCargo.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.SCORE_CARGO));
        mGrabCargo.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.GRAB_CARGO));
        mScoreHatch.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.SCORE_HATCH));
        mGrabHatch.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.GRAB_HATCH));
        mTogglePunchHatch.toggleWhenPressed(new TogglePunchHatch());
        mManualManipulatorArm.whileHeld(new ManualManipulatorArm());
    }

    public double getDriverStickX() {
        return mDriverStick.getX();
    }

    public double getDriverStickY() {
        return -mDriverStick.getY();
    }

    public double getOperatorStickX() {
        return mOperatorStick.getX();
    }

    public double getOperatorStickY() {
        return -mOperatorStick.getY();
    }

}
