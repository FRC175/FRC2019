package com.team175.robot.util;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Arvind
 */
public class AutoModeChooser {

    private final SendableChooser<Command> mChooser;

    private Command mMode;

    private static AutoModeChooser sInstance;

    public static AutoModeChooser getInstance() {
        if (sInstance == null) {
            sInstance = new AutoModeChooser();
        }

        return sInstance;
    }

    private AutoModeChooser() {
        mChooser = new SendableChooser<>();
        // mChooser.setDefaultOption("Default Auto", new ExampleCommand());
        mChooser.setDefaultOption("No Auto", null);
    }

    public boolean isAutoModeSelected() {
        return mMode != null;
    }

    public void outputToDashboard() {
        SmartDashboard.putData("Auto Mode Chooser", mChooser);
    }

    public void updateFromDashboard() {
        mMode = mChooser.getSelected();
    }

    public void start() {
        if (isAutoModeSelected()) {
            mMode.start();
        }
    }

    public void stop() {
        if (isAutoModeSelected()) {
            mMode.cancel();
            mMode = null;
        }
    }

}
