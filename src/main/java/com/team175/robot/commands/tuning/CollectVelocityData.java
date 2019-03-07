package com.team175.robot.commands.tuning;

import com.team175.robot.commands.AldrinCommand;
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

    private Map<String, Supplier> mData;
    private CSVWriter mWriter;
    private Notifier mNotifier;

    private static final String FILE_PATH = "/home/lvuser/csvlog/velocity-data.csv";
    private static final String DELIMITER = ",";
    private static final int RUN_TIME = 5; // 5 s
    private static final double NOTIFIER_LOOP_RATE = 0.02; // 20 ms

    public CollectVelocityData() {
        requires(Drive.getInstance());

        mData = new LinkedHashMap<>();
        mData.put("velocity", () -> (Drive.getInstance().getLeftVelocity() + Drive.getInstance().getRightVelocity()) / 2);
        mData.put("time", Timer::getFPGATimestamp);
        try {
            mWriter = new CSVWriter(mData, FILE_PATH, DELIMITER);
        } catch (FileNotFoundException e) {
            mLogger.error("Failed to instantiate CSVWriter!", e);
        }
        mNotifier = new Notifier(() -> mWriter.write());

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setPower(0);
        Drive.getInstance().setHighGear(true);
        mNotifier.startPeriodic(NOTIFIER_LOOP_RATE);

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
        mNotifier.stop();
        mWriter.close();

        mLogger.info("Collection of velocity data complete!");
        mLogger.info("Retrieve csv log from RoboRIO and use kinematics script to analyze motion.");

        super.end();
    }

}
