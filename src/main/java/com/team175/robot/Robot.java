/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.positions.LEDColor;
import com.team175.robot.subsystems.*;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public final class Robot extends TimedRobot {

    private LED mLED = LED.getInstance();
    private Color mColor = new Color(0, 0, 0);
    private boolean mIsOff = false;
    private int[] mColors = new int[3];
    private Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public void robotInit() {
        // According to ChiefDelphi, disabling this should fix the loop overrun message
        LiveWindow.disableAllTelemetry();
        mLED.outputToDashboard();
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();

        mLED.updateFromDashboard();
    }

    @Override
    public void autonomousInit() {
        mLogger.info("Please work!!!");
        mLogger.warn("Test2!!!");
        mLogger.error("Testing!!!");
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        mColor = mLED.getWantedColor();
        mLED.setColor(new Color(0, 0, 0));
    }

    // Blink color
    @Override
    public void teleopPeriodic() {
        if (mIsOff) {
            mLED.setColor(LEDColor.OFF);
        } else {
            mLED.setColor(mColor);
        }
        mIsOff = !mIsOff;
        Timer.delay(0.2);
    }

    @Override
    public void testInit() {
    }

    // RGB mood lamp
    @Override
    public void testPeriodic() {
        mColors[0] = 255;
        mColors[1] = 0;
        mColors[2] = 0;

        for (int decColor = 0; decColor < 3; decColor++) {
            int incColor = decColor == 2 ? 0 : decColor + 1;

            for (int i = 0; i < 255; i++) {
                mColors[decColor]--;
                mColors[incColor]++;

                mLED.setColor(new Color(mColors[0], mColors[1], mColors[2]));
                Timer.delay(0.01);
            }
        }
    }

    public static double getDefaultPeriod() {
        return kDefaultPeriod;
    }

}
