/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.team175.robot.commands.PIDTuner;
import com.team175.robot.subsystems.*;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
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

    private Command mAutoMode;
    private Command mPIDTunerSubsystem;
    private SendableChooser<Command> mAutoModeChooser;
    private SendableChooser<Command> mPIDTunerChooser;

    // private List<AldrinSubsystem> mSubsystems;

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
        /*mAutoModeChooser.setDefaultOption("Default Auto", new ExampleCommand());
        SmartDashboard.putData("Auto Mode", mAutoModeChooser);*/

        mPIDTunerChooser = new SendableChooser<>();
        mAutoModeChooser.setDefaultOption("Drive PID Tuning" , new PIDTuner(mDrive));
        mAutoModeChooser.addOption("Elevator PID Tuning", new PIDTuner(mElevator));
        mAutoModeChooser.addOption("Lateral Drive PID Tuning", new PIDTuner(mLateralDrive));
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

        if (mPIDTunerSubsystem != null) {
            mAutoMode.cancel();
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
    }

    @Override
    public void testInit() {
        mPIDTunerSubsystem = mPIDTunerChooser.getSelected();

        if (mPIDTunerSubsystem != null) {
            mPIDTunerSubsystem.start();
        }
    }

    @Override
    public void testPeriodic() {
    }

}
