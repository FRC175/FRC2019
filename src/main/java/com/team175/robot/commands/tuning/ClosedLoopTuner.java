package com.team175.robot.commands.tuning;

import com.team175.robot.Robot;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.util.CSVWriter;
import com.team175.robot.util.ClosedLoopTunable;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A command used to set a certain subsystem to a wanted position and records telemetry data of said subsystem into a
 * CSV file. This CSV data is then graphed to be able to determine appropriate PID gains.
 *
 * @author Arvind
 */
public class ClosedLoopTuner extends AldrinCommand {

    private ClosedLoopTunable mSubsystem;
    private CSVWriter mWriter;
    private Notifier mNotifier; // A WPILib object that spawns a new thread and calls run() at a certain refresh rate

    private static final String FILE_PATH = "/home/lvuser/csvlog/telemetry.csv";
    private static final String DELIMITER = ",";

    public ClosedLoopTuner(ClosedLoopTunable subsystem) {
        mSubsystem = subsystem;
        // Add time to subsystem telemetry
        Map<String, Supplier> m = subsystem.getCSVTelemetry();
        m.put("time", Timer::getFPGATimestamp);

        try {
            mWriter = new CSVWriter(m, FILE_PATH, DELIMITER);
        } catch (FileNotFoundException e) {
            mLogger.error("Failed to instantiate CSVWriter!", e);
        }

        // Create Notifier with Runnable that writes data to log
        mNotifier = new Notifier(() -> mWriter.write());

        super.logInstantiation();
    }

    @Override
    public void initialize() {
        mSubsystem.resetSensors();

        /*double refreshRate;
        try {
            // Take half of kDefaultPeriod in the Robot's superclass (in case either FastTimedRobot or TimeRobot is used
            refreshRate = Double.parseDouble(Robot.class.getSuperclass().getField("kDefaultPeriod").get(null).toString())
                    / 2.0;
        } catch (IllegalAccessException | NoSuchFieldException | NumberFormatException e) {
            mLogger.warn("Failed to parse refresh rate! Using default of 10 ms instead...", e);
            refreshRate = 0.01; // 10 ms
        }*/
        double refreshRate = Robot.getRefreshRate() / 2;
        mLogger.debug("Refresh Rate: {}", refreshRate);

        try {
            mNotifier.startPeriodic(refreshRate);
            mSubsystem.updateGains();
        } catch (Exception e) {
            mLogger.error("Failed to start ClosedLoopTuner thread!", e);
        }

        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        mNotifier.stop();
        mWriter.close();

        mLogger.info("Collection of data for {} complete!", mSubsystem.getClass().getSimpleName());
        mLogger.info("Plot csv log from RoboRIO to determine gains.");

        super.end();
    }

}