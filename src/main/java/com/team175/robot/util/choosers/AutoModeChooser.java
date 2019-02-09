package com.team175.robot.util.choosers;

import com.team175.robot.commands.ExampleCommand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoModeChooser implements Chooser {

    private final SendableChooser<Command> mChooser;

    private Command mMode;

    public AutoModeChooser() {
        mChooser = new SendableChooser<>();
        mChooser.setDefaultOption("Default Auto", new ExampleCommand());
    }

    @Override
    public void outputToDashboard() {
        SmartDashboard.putData("Auto Mode Chooser", mChooser);

    }

    @Override
    public void updateFromDashboard() {
        mMode = mChooser.getSelected();
    }

    @Override
    public void start() {
        if (mMode != null) {
            mMode.start();
        }
    }

    @Override
    public void stop() {
        if (mMode != null) {
            mMode.cancel();
            mMode = null;
        }
    }

}