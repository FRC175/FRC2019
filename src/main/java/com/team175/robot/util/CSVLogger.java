package com.team175.robot.util;

import java.io.File;

/**
 * TODO: Finish object.
 *
 * @author Arvind
 */
public class CSVLogger implements Runnable {

    /* Declarations */
    // String
    private final String mFilePath;

    // File
    private final File mFile;

    // Singleton Instance
    private static CSVLogger sInstance;

    public static CSVLogger getInstance() {
        if (sInstance == null) {
            sInstance = new CSVLogger();
        }

        return sInstance;
    }

    private CSVLogger() {
        /* Instantiations */
        mFilePath = "/home/lvuser/csvlog/telemetry.log";
        mFile = new File(mFilePath);
    }

    public void setTarget(Loggable l) {

    }

    public void addPeriodic() {

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
