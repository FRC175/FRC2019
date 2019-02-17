/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.subsystems.*;
import com.team175.robot.util.choosers.AutoModeChooser;
import com.team175.robot.util.choosers.TunerChooser;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

import java.util.List;

/**
 * TODO: Consider using FastTimedRobot.
 *
 * @author Arvind
 */
public class Robot extends TimedRobot {

    /* Declarations */
    private Drive mDrive;
    private Elevator mElevator;
    private LateralDrive mLateralDrive;
    private Lift mLift;
    // private Manipulator mManipulator;
    private Vision mVision;

    private OI mOI;

    private AutoModeChooser mAutoModeChooser;
    private TunerChooser mTunerChooser;

    private List<AldrinSubsystem> mSubsystems;

    @Override
    public void robotInit() {
        /* Instantiations */
        mDrive = Drive.getInstance();
        mElevator = Elevator.getInstance();
        mLateralDrive = LateralDrive.getInstance();
        mLift = Lift.getInstance();
        // mManipulator = Manipulator.getInstance();
        mVision = Vision.getInstance();

        mOI = OI.getInstance();

        mAutoModeChooser = AutoModeChooser.getInstance();
        mTunerChooser = new TunerChooser();

        mSubsystems = List.of(mDrive, mElevator, mLateralDrive, mLift); // mManipulator
        // Comment out in production robot
        mSubsystems.forEach(AldrinSubsystem::outputToDashboard);
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

        // Comment out in production robot
        mSubsystems.forEach(AldrinSubsystem::updateFromDashboard);
    }

    @Override
    public void autonomousInit() {
        mAutoModeChooser.updateFromDashboard();
        mAutoModeChooser.start();
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        mAutoModeChooser.stop();

        // Comment out in production robot
        // mTunerChooser.updateFromDashboard();
        // mTunerChooser.start();
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

        // Comment out in production robot
        mSubsystems.forEach(AldrinSubsystem::onPeriodic);
    }

    @Override
    public void testInit() {
        mTunerChooser.updateFromDashboard();
        mTunerChooser.start();
    }

    @Override
    public void testPeriodic() {
        Scheduler.getInstance().run();
    }

}
