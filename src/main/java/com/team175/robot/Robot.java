/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.positions.LEDColor;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.*;
import com.team175.robot.util.AutoModeChooser;
import com.team175.robot.util.RobotManager;
import com.team175.robot.util.TunerChooser;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
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
    // private TunerChooser mTunerChooser;
    private Logger mLogger;
    private RobotManager mRobotManager;

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
        // mTunerChooser = TunerChooser.getInstance();
        mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

        // mRobotManager.startLED();
        // Initialize SmartDashboard values
        mRobotManager.outputToDashboard();
        // Add velocity collection command to dashboard
        // SmartDashboard.putData("Collect Velocity Data", new CollectVelocityData());
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
        // mTunerChooser.stop();
        mRobotManager.stopSubsystems();
        mRobotManager.startMessaging();
        mRobotManager.outputToDashboard();
        // mLED.setStaticColor(LEDColor.DISABLED);

        mLogger.debug("Beginning disabled!");
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();

        mRobotManager.updateFromDashboard();
    }

    @Override
    public void autonomousInit() {
        mAutoModeChooser.updateFromDashboard();
        mAutoModeChooser.start();

        mRobotManager.startSubsystems();
        mRobotManager.stopMessaging();

        // mLED.setStaticColor(LEDColor.DEFAULT);

        // Let people know robot has been enabled
        // mLED.blinkColor(LEDColor.ENABLED);

        mLogger.debug("Beginning auto!");
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        mAutoModeChooser.stop();

        mRobotManager.startSubsystems();
        mRobotManager.stopMessaging();

        // mManipulator.setBrake(true);
        // mLED.setStaticColor(LEDColor.DEFAULT);

        // Let people know robot has been enabled
        // mLED.blinkColor(LEDColor.ENABLED);

        mLogger.debug("Beginning teleop!");
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testInit() {
        mLogger.debug("Beginning test!");

        mRobotManager.stopMessaging();
        // mRobotManager.stopLED();

        /*mLogger.info("Beginning robot diagnostics test.");

        boolean isGood = mRobotManager.checkSubsystems();

        if (!isGood) {
            mLogger.error("Robot failed diagnostics test!");
        } else {
            mLogger.info("Robot passed diagnostics test!");
        }*/

        // mTunerChooser.updateFromDashboard();
        // mTunerChooser.start();
    }

    @Override
    public void testPeriodic() {
        Scheduler.getInstance().run();

        // mLED.updateFromDashboard();
    }

    public static double getDefaultPeriod() {
        return kDefaultPeriod;
    }

}
