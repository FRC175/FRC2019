/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import com.team175.robot.commands.teleop.LineAlignment;

/**
 * @author Arvind
 */
public class OI {

    // Joysticks
    private Joystick mDriverStick;
    private Joystick mOperatorStick;

    // Driver Stick Buttons
    private Button mLineAlign;

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
        mDriverStick = new Joystick(Constants.kDriverStickPort);
        mOperatorStick = new Joystick(Constants.kOperatorStickPort);

        mLineAlign = new JoystickButton(mDriverStick, Constants.kLineAlignButton);

        mLineAlign.whileHeld(new LineAlignment());
    }

    public double getDriverStickX() {
        return mDriverStick.getX();
    }

    public double getDriverStickY() {
        return mDriverStick.getY();
    }

    public double getOperatorStickX() {
        return mOperatorStick.getX();
    }

    public double getOperatorStickY() {
        return mOperatorStick.getY();
    }

}
