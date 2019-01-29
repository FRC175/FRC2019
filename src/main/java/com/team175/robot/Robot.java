/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import java.util.Arrays;
import java.util.List;

import com.team175.robot.commands.ClosedLoopTuner;
import com.team175.robot.commands.deprecated.ExampleCommand;
import com.team175.robot.subsystems.*;

import com.team175.robot.util.ClosedLoopTunable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 *
 * TODO:
 * - Create a periodic method in each subsystem that runs at double the normal robot refresh rate (20 ms => 10 ms)
 * in a separate thread in order to increase robot accuracy.
 * - Utilize csv logging in order to capture robot telemetry data such as encoder counts and graph them in a python
 * program.
 * - Run csv logger at double the normal refresh rate.
 *
 * @author Arvind
 */
public class Robot extends TimedRobot {

    /* Declarations */
	/*private ExampleSubsystem mExampleSubsystem;
	private Breadboard mBreadboard;*/
    private Drive mDrive;
    private Elevator mElevator;
    private LateralDrive mLateralDrive;
    private Lift mLift;
    private Manipulator mManipulator;
    private Vision mVision;
    private OI mOI;

    private Command mAutonomousCommand;
    private SendableChooser<Command> mChooser;

    private List<AldrinSubsystem> mSubsystems;

    @Override
    public void robotInit() {
        /* Instantiations */
        mDrive = Drive.getInstance();
        mElevator = Elevator.getInstance();
        mLateralDrive = LateralDrive.getInstance();
        mLift = Lift.getInstance();
        mManipulator = Manipulator.getInstance();
        // mVision = Vision.getInstance();
        mOI = OI.getInstance();
        // mVision.run();

        mChooser = new SendableChooser<>();
        mSubsystems = Arrays.asList();
        // mSubsystems = Arrays.asList(mBreadboard, mCamera);

        mChooser.setDefaultOption("Default Auto", new ExampleCommand());
        // mChooser.addOption("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto Mode", mChooser);
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
        mAutonomousCommand = mChooser.getSelected();

        /*
         * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
         * switch(autoSelected) { case "My Auto": autonomousCommand = new
         * MyAutoCommand(); break; case "Default Auto": default: autonomousCommand = new
         * ExampleCommand(); break; }
         */

        // schedule the autonomous command (example)
        if (mAutonomousCommand != null) {
            mAutonomousCommand.start();
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
        if (mAutonomousCommand != null) {
            mAutonomousCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testInit() {
        SmartDashboard.putData("Test PIDTuner", new ClosedLoopTuner(new ClosedLoopTunable() {
            int time = 0;
            int output = 10;

            @Override
            public String toCSVHeader() {
                return "time,output";
            }

            @Override
            public String toCSVPeriodic() {
                String s = time + "," + output;

                time++;
                output += 10;

                return s;
            }

            @Override
            public void updatePID() {
            }
        }));

        /*SmartDashboard.putData("Drive PIDTuner", new PIDTuner(Drive.getInstance()));
        SmartDashboard.putData("Elevator PIDTuner", new PIDTuner(Elevator.getInstance()));
        SmartDashboard.putData("Lateral Drive PIDTuner", new PIDTuner(LateralDrive.getInstance()));*/
    }

    @Override
    public void testPeriodic() {
    }

}
