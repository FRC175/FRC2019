package com.team175.robot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * TODO: Test.
 * TODO: Consider using different file writer.
 *
 * @author Arvind
 */
public class CSVLogger implements Runnable {

    /* Declarations */
    // File
    private final File mFile;

    // CSVLoggable
    private CSVLoggable mTarget;

    // Print Writer
    private PrintWriter mWriter;

    // Boolean
    private boolean mIsRunning;

    // Logger
    private final Logger mLogger;

    // Singleton Instance
    private static CSVLogger sInstance;

    public static CSVLogger getInstance() {
        if (sInstance == null) {
            sInstance = new CSVLogger();
        }

        return sInstance;
    }

    private CSVLogger() {
        mFile = new File("/home/lvuser/csvlog/telemetry.log");
        mLogger = LoggerFactory.getLogger(getClass().getSimpleName());
        init();
    }

    public void setTarget(CSVLoggable target) {
        mTarget = target;
    }

    @Override
    public void run() {
        if (!mIsRunning) {
            start();
        } else {
            periodic();
        }
    }

    public void stop() {
        mWriter.close();
        mIsRunning = false;
    }

    private void periodic() {
        mWriter.write(mTarget.toCSVPeriodic());
    }

    private void start() {
        init();
        mWriter.write(mTarget.toCSVHeader());
        mIsRunning = true;
    }

    private void init() {
        try {
            mWriter = new PrintWriter(new FileOutputStream(mFile, false));
            mIsRunning = false;
        } catch (FileNotFoundException e) {
            mLogger.error("Could not open file!", e);
        }
    }

}
