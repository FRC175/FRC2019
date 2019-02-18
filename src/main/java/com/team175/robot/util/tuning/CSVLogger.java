package com.team175.robot.util.tuning;

import edu.wpi.first.wpilibj.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public class CSVLogger implements Runnable {

    private final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    private static final String FILE_PATH = "/home/lvuser/csvlog/telemetry.csv";
    private static final String DELIMITER = ",";

    private CSVWriter mWriter;

    public CSVLogger(CSVLoggable target) {
        Map<String, DoubleSupplier> m = target.getCSVTelemetry();
        m.put("time", Timer::getFPGATimestamp);

        try {
            mWriter = new CSVWriter(m, FILE_PATH, DELIMITER);
        } catch (FileNotFoundException e) {
            mLogger.error("Failed to instantiate!", getClass().getSimpleName());
        }
    }

    public void stop() {
        mWriter.close();
    }

    @Override
    public void run() {
        mWriter.write();
    }

}
