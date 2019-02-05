package com.team175.robot.util.logging;

import edu.wpi.first.wpilibj.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * TODO: Test.
 *
 * @author Arvind
 */
public class CSVLogger implements Runnable {

    private Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());
    private CSVWriter mWriter;
    // private boolean mIsRunning;

    public CSVLogger(CSVLoggable target) {
        LinkedHashMap<String, DoubleSupplier> m = target.getCSVProperties();
        m.put("time", Timer::getFPGATimestamp);

        try {
            mWriter = new CSVWriter(m, "/home/lvuser/csvlog/telemetry.csv", ",");
        } catch (FileNotFoundException e) {
            mLogger.error("{} failed to instantiate!", getClass().getSimpleName(), e);
        }
    }

    public void stop() {
        try {
            mWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            mWriter.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
