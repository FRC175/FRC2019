/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.commands.tuning.CollectVelocityData;
import com.team175.robot.subsystems.*;
import com.team175.robot.util.choosers.AutoModeChooser;
import com.team175.robot.util.choosers.RobotChooser;
import com.team175.robot.util.choosers.TunerChooser;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * TODO: Consider using FastTimedRobot.
 *
 * TODO: MAYBE BE ABLE TO CONTROL BOTH COMPRESSORS
 * TODO: NEED TO FIX MANIPULATOR ARM BRAKE!!!
 * TODO: NEED TO FIX ELEVATOR AND MANIPULATOR ARM COUNTS IN POSITION CONTROL!!!
 *
 * @author Arvind
 */
public class Robot extends TimedRobot {

    /* Declarations */
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
    private List<AldrinSubsystem> mSubsystems;

    @Override
    public void robotInit() {
        /* Instantiations */
        RobotChooser.getInstance().setProfile(true);
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
        mSubsystems = List.of(mDrive); //, mElevator, mLateralDrive, mLift, mManipulator

        /* Configuration */
        // Runs camera stream on separate thread
        new Thread(mVision).start();
        // Comment out in production robot
        mSubsystems.forEach(AldrinSubsystem::outputToDashboard);

        // Add velocity collection command to dashboard
        SmartDashboard.putData("Collect Velocity Data", new CollectVelocityData());
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
        mTunerChooser.stop();
        mSubsystems.forEach(AldrinSubsystem::outputToDashboard);
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();

        // mLED.moodLampCycle();
        // Comment out in production robot
        mSubsystems.forEach(AldrinSubsystem::updateFromDashboard);
    }

    @Override
    public void autonomousInit() {
        mAutoModeChooser.updateFromDashboard();
        mAutoModeChooser.start();

        mDrive.setHighGear(true);
        mDrive.setBrakeMode(true);
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        mAutoModeChooser.stop();

        mDrive.setHighGear(false);
        mDrive.setBrakeMode(false);
        // mManipulator.setBrake(true);
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

        // Comment out in production robot
        mSubsystems.forEach(AldrinSubsystem::onPeriodic);
    }

    @Override
    public void testInit() {
        mLogger.info("Beginning robot diagnostics test.");

        boolean isGood = true;
        for (AldrinSubsystem as : mSubsystems) {
            isGood &= as.checkSubsystem();
        }

        if (!isGood) {
            mLogger.error("Robot failed diagnostics test!");
        } else {
            mLogger.info("Robot passed diagnostics test!");
        }

        /*mTunerChooser.updateFromDashboard();
        mTunerChooser.start();*/
    }

    @Override
    public void testPeriodic() {
        Scheduler.getInstance().run();
    }

    public static double getRefreshRate() {
        return kDefaultPeriod;
    }

}
