package com.team175.robot.commands.tuning;

import com.team175.robot.Robot;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.loops.CSVWriterLoop;
import com.team175.robot.loops.Looper;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.util.tuning.CSVWriter;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A command used to collect velocity data and save it to a CSV file. This data is then sent to a python script that
 * fits the velocity data through a polynomial fit. The derivative of this polynomial fit is then taken to find the
 * acceleration. In the end, all of the data is graphed and the maximum velocity and acceleration are printed out.
 * These variables are used for generating motion profiles.
 *
 * @author Arvind
 */
public class CollectVelocityData extends AldrinCommand {

    private Looper mLooper;

    private static final double PERIOD = Robot.getDefaultPeriod();
    private static final String FILE_PATH = "/home/lvuser/csvlog/velocity-data.csv";
    private static final int RUN_TIME = 5; // 5 s

    public CollectVelocityData() {
        requires(Drive.getInstance());

        Map<String, Supplier> data = Drive.getInstance().getCSVTelemetry();
        data.put("time", Timer::getFPGATimestamp);
        try {
            mLooper = new Looper(PERIOD, new CSVWriterLoop(data, FILE_PATH));
        } catch (FileNotFoundException e) {
            mLogger.error("Failed to instantiate CSVWriter!", e);
        }

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setPower(0);
        Drive.getInstance().setHighGear(true);
        mLooper.start();

        mLogger.info("Starting velocity collection...");
        Drive.getInstance().setPower(1);
        Timer.delay(RUN_TIME);
        Drive.getInstance().setPower(0);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
        mLooper.stop();

        mLogger.info("Collection of velocity data complete!");
        mLogger.info("Retrieve csv log from RoboRIO and use kinematics script to analyze motion.");

        super.end();
    }

}
