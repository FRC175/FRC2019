package com.team175.robot.util.choosers;

import com.team175.robot.commands.ClosedLoopTuner;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.LateralDrive;
import com.team175.robot.subsystems.Manipulator;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TunerChooser implements Chooser {

    private final SendableChooser<SubsystemToTune> mChooser;

    private ClosedLoopTuner mTuner;

    private enum SubsystemToTune {
        DRIVE,
        ELEVATOR,
        LATERAL_DRIVE,
        MANIPULATOR_ARM;
    }

    public TunerChooser() {
        mChooser = new SendableChooser<>();
        mChooser.addOption("Drive", SubsystemToTune.DRIVE);
        mChooser.addOption("Elevator", SubsystemToTune.ELEVATOR);
        mChooser.addOption("Lateral Drive", SubsystemToTune.LATERAL_DRIVE);
        mChooser.addOption("Manipulator Arm", SubsystemToTune.MANIPULATOR_ARM);
        outputToDashboard();
    }

    @Override
    public void updateFromDashboard() {
        SubsystemToTune s = mChooser.getSelected();
        switch (s) {
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
        }
    }

    @Override
    public void outputToDashboard() {
        SmartDashboard.putData("Tuner Chooser", mChooser);
    }

    @Override
    public void start() {
        if (mTuner != null) {
            mTuner.initialize();
        }
    }

    @Override
    public void stop() {
        if (mTuner != null) {
            mTuner.end();
            mTuner = null;
        }
    }

}
