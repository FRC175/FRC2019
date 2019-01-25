package com.team175.robot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;

/**
 * TODO: Finish object.
 *
 * @author Arvind
 */
public class CSVLogger implements Runnable {

    /* Declarations */
    // String
    private static final String FILE_PATH = "/home/lvuser/csvlog/telemetry.log";
    private String mHeaderLine, mPeriodicLine;

    // File
    private static final File FILE = new File(FILE_PATH);

    // Logger
    private Logger mLogger = LoggerFactory.getLogger(getClass());

    // Singleton Instance
    private static CSVLogger sInstance;

    public static CSVLogger getInstance() {
        if (sInstance == null) {
            sInstance = new CSVLogger();
        }

        return sInstance;
    }

    private CSVLogger() {
        try {
            mHeaderLine = "";
            mPeriodicLine = "";
        } catch (Exception e) {
            mLogger.error("CSVLogger failed to instantiate.", e);
        }

        mLogger.info("CSVLogger instantiated successfully!");
    }

    public void addData(String... s) {
        for (String str : s) {
            mPeriodicLine += str + ",";
        }
    }

    public void start() {

    }

    public void stop() {

    }

    public void clear() {

    }

    @Override
    public void run() {
        
    }

}
