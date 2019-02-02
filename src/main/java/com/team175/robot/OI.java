/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.commands.*;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.util.TwoButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * @author Arvind
 */
public class OI {

    /* Declarations */
    // Joysticks
    private Joystick mDriverStick;
    private Joystick mOperatorStick;

    // Driver Stick Buttons
    private Button mManualLateralDrive;
    // private Button mLineAlign;

    private Button mLift;

    // Operator Stick Buttons
    private Button mManualElevator;

    private Button mRollers;
    private Button mManualArm;

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
        // mLineAlign = new JoystickButton(mDriverStick, Constants.LINE_ALIGN_BUTTON);

        mLift = new TwoButton(mDriverStick, 3, 4);

        // Operator Stick Buttons
        mManualElevator = new JoystickButton(mOperatorStick, Constants.MANUAL_ELEVATOR_TRIGGER);

        mRollers = new JoystickButton(mOperatorStick, 3);
        mManualArm = new JoystickButton(mOperatorStick, 12);

        /* Command Assignment */
        // Driver Stick
        mManualLateralDrive.whileHeld(new ManualLateralDrive());
        // mLineAlign.whenPressed(new LineAlignment());

        mLift.whileHeld(new PositionLift(LiftPosition.IDLE));

        // Operator Stick
        mManualElevator.whileHeld(new ManualElevator());

        mRollers.whileHeld(new ManipulateGamePiece(ManipulatorRollerPosition.IDLE));
        mManualArm.whileHeld(new ManualManipulatorArm());
    }

    public double getDriverStickX() {
        return mDriverStick.getX();
    }

    public double getDriverStickY() {
        return mDriverStick.getY();
    }

    public double getDriverStickTwist() {
        return mDriverStick.getTwist();
    }

    public double getOperatorStickX() {
        return mOperatorStick.getX();
    }

    public double getOperatorStickY() {
        return mOperatorStick.getY();
    }

}
