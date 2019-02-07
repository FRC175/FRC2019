/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.commands.ClosedLoopTuner;
import com.team175.robot.subsystems.*;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

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

    private SendableChooser<Command> mAutoModeChooser;
    private SendableChooser<Command> mClosedLoopTuner;

    private Command mAutoMode;
    private ClosedLoopTuner mClosedLoopTunable;

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

        mAutoModeChooser = new SendableChooser<>();
        mClosedLoopTuner = new SendableChooser<>();

        /*mAutoModeChooser.setDefaultOption("Default Auto", new ExampleCommand());
        SmartDashboard.putData("Auto Mode", mAutoModeChooser);*/
        /*mClosedLoopTuner.addOption("Drive PIDF Tuning" , new ClosedLoopTuner(mDrive));
        mClosedLoopTuner.addOption("Elevator PIDF Tuning", new ClosedLoopTuner(mElevator));
        mClosedLoopTuner.addOption("LateralDrive PIDF Tuning", new ClosedLoopTuner(mLateralDrive));
        mClosedLoopTuner.addOption("ManipulatorArm PIDF Tuning", new ClosedLoopTuner(mManipulator));*/
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
        if (mClosedLoopTunable != null) {
            mClosedLoopTunable.end();
            mClosedLoopTunable = null;
        }
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        mAutoMode = mAutoModeChooser.getSelected();

        /*
         * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
         * switch(autoSelected) { case "My Auto": autonomousCommand = new
         * MyAutoCommand(); break; case "Default Auto": default: autonomousCommand = new
         * ExampleCommand(); break; }
         */

        // schedule the autonomous command (example)
        if (mAutoMode != null) {
            mAutoMode.start();
        }
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (mAutoMode != null) {
            mAutoMode.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

        mDrive.outputToDashboard();
        mDrive.updateFromDashboard();
        /*mElevator.outputToDashboard();
        mElevator.updateFromDashboard();*/

        /*mDrive.sendToDashboard();
        mLateralDrive.sendToDashboard();
        mElevator.sendToDashboard();
        mManipulator.sendToDashboard();*/
    }

    @Override
    public void testInit() {
        mClosedLoopTunable = new ClosedLoopTuner(mDrive);

        if (mClosedLoopTunable != null) {
            mClosedLoopTunable.initialize();
        }
    }

    @Override
    public void testPeriodic() {
    }

}
