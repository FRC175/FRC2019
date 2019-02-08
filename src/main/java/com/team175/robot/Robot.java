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
    private Manipulator mManipulator;
    private Vision mVision;

    private OI mOI;

    private AutoModeChooser mAutoModeChooser;
    private TunerChooser mTunerChooser;

    @Override
    public void robotInit() {
        /* Instantiations */
        mDrive = Drive.getInstance();
        mElevator = Elevator.getInstance();
        mLateralDrive = LateralDrive.getInstance();
        mLift = Lift.getInstance();
        mManipulator = Manipulator.getInstance();
        mVision = Vision.getInstance();

        mOI = OI.getInstance();

        mAutoModeChooser = new AutoModeChooser();
        mTunerChooser = new TunerChooser();
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
        /*if (mTunable != null) {
            mTunable.end();
            mTunable = null;
        }*/
        mTunerChooser.stop();
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
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
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testInit() {
        /*mTunable = new ClosedLoopTuner(mDrive);

        if (mTunable != null) {
            mTunable.initialize();
        }*/
        mTunerChooser.updateFromDashboard();
        mTunerChooser.start();
    }

    @Override
    public void testPeriodic() {
        Scheduler.getInstance().run();
    }

}
