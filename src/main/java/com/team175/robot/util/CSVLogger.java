package com.team175.robot.util;

import java.io.File;
import java.time.LocalDate;

/**
 * @author Arvind
 */
public class CSVLogger implements Runnable {

    private static final File FILE;
    private static LocalDate date;

    static {
        date = LocalDate.now();
        FILE = new File("~/csv-log/telemetry-" + date);
    }

    public CSVLogger() {

    }

    @Override
    public void run() {
        
    }

}
