/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.commands.led.ControlLED;
import com.team175.robot.commands.tuning.CollectVelocityData;
import com.team175.robot.subsystems.*;
import com.team175.robot.util.AutoModeChooser;
import com.team175.robot.util.RobotManager;
import com.team175.robot.util.TunerChooser;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Consider using FastTimedRobot.
 *
 * @author Arvind
 */
public final class Robot extends TimedRobot {

    private Drive mDrive;
    private Elevator mElevator;
    private LateralDrive mLateralDrive;
    // private LED mLED;
    private Lift mLift;
    private Manipulator mManipulator;
    private Vision mVision;
    private OI mOI;
    private AutoModeChooser mAutoModeChooser;
    private TunerChooser mTunerChooser;
    private Logger mLogger;
    private RobotManager mRobotManager;

    private String[] mNaan = {
            "Abdullah is watching you.",
            "Jamie is the all supreme lord.",
            "Bret is MVP",
            "Andrew Jones was here.",
            "Aaron likes his wifu!",
            "Aaron's girlfriend is fake!"
    };
    private int mBriyani;

    @Override
    public void robotInit() {
        RobotManager.setProfile(false);
        mRobotManager = RobotManager.getInstance();
        mDrive = Drive.getInstance();
        mElevator = Elevator.getInstance();
        mLateralDrive = LateralDrive.getInstance();
        // mLED = LED.getInstance();
        mLift = Lift.getInstance();
        mManipulator = Manipulator.getInstance();
        mVision = Vision.getInstance();
        mOI = OI.getInstance();
        mAutoModeChooser = AutoModeChooser.getInstance();
        mTunerChooser = TunerChooser.getInstance();
        mLogger = LoggerFactory.getLogger(getClass().getSimpleName());
        mBriyani = (int) (Math.random() * 10) + 1;

        // Runs camera stream on separate thread
        new Thread(mVision).start();
        mRobotManager.outputToDashboard();
        // Add velocity collection command to dashboard
        SmartDashboard.putData("Collect Velocity Data", new CollectVelocityData());
        // Add LED tuning command to dashboard
        SmartDashboard.putData("LED Color Chooser", new ControlLED());
        // According to ChiefDelphi, disabling this should fix the loop overrun message
        LiveWindow.disableAllTelemetry();
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
        mTunerChooser.stop();
        mRobotManager.outputToDashboard();

        /*mRobotManager.stopCompressor();*/
        mLogger.debug("Beginning disabled!");
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();

        // mLED.moodLampCycle();
        mRobotManager.updateFromDashboard();
        // mElevator.updateFromDashboard();
        // mManipulator.updateFromDashboard();
    }

    @Override
    public void autonomousInit() {
        mAutoModeChooser.updateFromDashboard();
        mAutoModeChooser.start();

        if (mAutoModeChooser.isAutoModeSelected()) {
            mDrive.setHighGear(true);
            mDrive.setBrakeMode(true);
        } else {
            mDrive.setHighGear(false);
            mDrive.setBrakeMode(false);
        }
        mDrive.resetSensors();
        mManipulator.setBrake(true);
        mLateralDrive.deploy(false);

        mLogger.debug("Beginning auto!");
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();

        mRobotManager.outputToDashboard();
    }

    @Override
    public void teleopInit() {
        mAutoModeChooser.stop();

        mDrive.setHighGear(false);
        mDrive.setBrakeMode(false);
        mDrive.resetSensors();
        mManipulator.setBrake(true);
        mLateralDrive.deploy(false);

        mLogger.debug("Beginning teleop!");
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

        mRobotManager.outputToDashboard();


    }

    @Override
    public void testInit() {
        mLogger.debug("Beginning test!");

        /*mLogger.info("Beginning robot diagnostics test.");

        boolean isGood = mRobotManager.checkSubsystems();

        if (!isGood) {
            mLogger.error("Robot failed diagnostics test!");
        } else {
            mLogger.info("Robot passed diagnostics test!");
        }*/

        mTunerChooser.updateFromDashboard();
        mTunerChooser.start();

        /*mRobotManager.startCompressor();*/
    }

    @Override
    public void testPeriodic() {
        Scheduler.getInstance().run();
    }

    public static double getDefaultPeriod() {
        return kDefaultPeriod;
    }

}
