package com.team175.robot.util.choosers;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Arvind
 */
public class AutoModeChooser implements Chooser {

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
