package com.team175.robot.commands.tuning;

import com.team175.robot.Robot;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.loops.CSVWriterLoop;
import com.team175.robot.loops.Looper;
import com.team175.robot.util.tuning.ClosedLoopTunable;
import edu.wpi.first.wpilibj.Timer;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A command used to set a certain subsystem to a wanted position and records telemetry data of said subsystem into a
 * CSV file. This CSV data is graphed real time and PID gains updated in order to make subsystem reach the wanted
 * position as closed as possible.
 *
 * @author Arvind
 */
public class ClosedLoopTuner extends AldrinCommand {

    private ClosedLoopTunable mSubsystem;
    private Looper mLooper;

    private static final double PERIOD = Robot.getDefaultPeriod() / 2;
    private static final String FILE_PATH = "/home/lvuser/csvlog/tuning-data.csv";

    public ClosedLoopTuner(ClosedLoopTunable subsystem) {
        mSubsystem = subsystem;
        Map<String, Supplier> data = subsystem.getCSVTelemetry();
        data.put("time", Timer::getFPGATimestamp); // Add time to subsystem telemetry

        try {
            mLooper = new Looper(PERIOD, new CSVWriterLoop(data, FILE_PATH) {
                @Override
                public void loop() {
                    mWriter.write();
                    mWriter.flush();
                    mSubsystem.updateGains();
                }
            });
        } catch (FileNotFoundException e) {
            mLogger.error("Failed to instantiate looper!", e);
        }

        super.logInstantiation();
    }

    @Override
    public void initialize() {
        // mSubsystem.resetSensors();

        try {
            mLooper.start();
        } catch (Exception e) {
            mLogger.error("Failed to start looper!", e);
        }

        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        mLooper.stop();

        /*mLogger.info("Collection of data for {} complete!", mSubsystem.getClass().getSimpleName());
        mLogger.info("Plot csv log from RoboRIO to determine gains.");*/
        mLogger.info("Tuning of {} subsystem complete!", mSubsystem.getClass().getSimpleName());

        super.end();
    }

}