package com.team175.robot.util;

import com.team175.robot.commands.tuning.ClosedLoopTuner;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.LateralDrive;
import com.team175.robot.subsystems.Manipulator;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Arvind
 */
public class TunerChooser {

    private final SendableChooser<SubsystemToTune> mChooser;

    private ClosedLoopTuner mTuner;

    private static TunerChooser sInstance;

    private enum SubsystemToTune {
        DRIVE,
        ELEVATOR,
        LATERAL_DRIVE,
        MANIPULATOR_ARM;
    }

    public static TunerChooser getInstance() {
        if (sInstance == null) {
            sInstance = new TunerChooser();
        }

        return sInstance;
    }

    private TunerChooser() {
        mChooser = new SendableChooser<>();
        mChooser.addOption("Drive", SubsystemToTune.DRIVE);
        mChooser.addOption("Elevator", SubsystemToTune.ELEVATOR);
        mChooser.addOption("Lateral Drive", SubsystemToTune.LATERAL_DRIVE);
        mChooser.addOption("Manipulator Arm", SubsystemToTune.MANIPULATOR_ARM);
        outputToDashboard();
    }

    public void outputToDashboard() {
        SmartDashboard.putData("Tuner Chooser", mChooser);
    }

    public void updateFromDashboard() {
        switch (mChooser.getSelected()) {
            case DRIVE:
                mTuner = new ClosedLoopTuner(Drive.getInstance());
                break;
            case ELEVATOR:
                mTuner = new ClosedLoopTuner(Elevator.getInstance());
                break;
            case LATERAL_DRIVE:
                mTuner = new ClosedLoopTuner(LateralDrive.getInstance());
                break;
            case MANIPULATOR_ARM:
                mTuner = new ClosedLoopTuner(Manipulator.getInstance());
                break;
            default:
                mTuner = null;
                break;
        }
    }

    public void start() {
        if (mTuner != null) {
            mTuner.initialize();
        }
    }

    public void stop() {
        if (mTuner != null) {
            mTuner.end();
            mTuner = null;
        }
    }

}
