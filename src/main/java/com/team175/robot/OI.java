/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.commands.teleop.ManualElevator;
import com.team175.robot.commands.teleop.ManualLateralDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import com.team175.robot.commands.teleop.LineAlignment;

/**
 * @author Arvind
 */
public class OI {

    /* Declarations */
    // Joysticks
    private Joystick mDriverStick;
    private Joystick mOperatorStick;

    // Driver Stick Buttons
    private Button mToggleLateralDrive;
    private Button mLineAlign;

    // Operator Stick Buttons
    private Button mManualElevator;

    // Singleton Instance
    private static OI sInstance;

    public static OI getInstance() {
        if (sInstance == null) {
            try {
                sInstance = new OI();
            } catch (Exception e) {
                // DriverStation.reportError("OI failed to instantiate.\n" + e, true);
            }
        }

        return sInstance;
    }

    private OI() {
        /* Instantiations */
        // Joystick
        mDriverStick = new Joystick(Constants.DRIVER_STICK_PORT);
        mOperatorStick = new Joystick(Constants.OPERATOR_STICK_PORT);

        // Driver Stick Buttons
        mToggleLateralDrive = new JoystickButton(mDriverStick, Constants.LATERAL_DRIVE_TRIGGER); // 1 is the trigger button
        mLineAlign = new JoystickButton(mDriverStick, Constants.LINE_ALIGN_BUTTON);

        // Operator Stick Buttons
        mManualElevator = new JoystickButton(mOperatorStick, Constants.MANUAL_ELEVATOR_TRIGGER);

        /* Command Assignment */
        // Driver Stick
        mToggleLateralDrive.whileHeld(new ManualLateralDrive());
        mLineAlign.whenPressed(new LineAlignment());

        // Operator Stick
        mManualElevator.whileHeld(new ManualElevator());
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
