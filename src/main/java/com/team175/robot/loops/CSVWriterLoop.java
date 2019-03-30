package com.team175.robot.loops;

import com.team175.robot.util.tuning.CSVWriter;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Writes periodically to a CSV file using a loop.
 *
 * @author Arvind
 */
public class CSVWriterLoop implements Loop {

    protected final CSVWriter mWriter;

    private static final String DELIMITER = ",";

    public CSVWriterLoop(Map<String, Supplier> data, String filePath) throws FileNotFoundException {
        mWriter = new CSVWriter(data, filePath, DELIMITER);
    }

    @Override
    public void start() {
        // Write CSV header
        mWriter.write();
    }

    @Override
    public void loop() {
        // Continuously write CSV body
        mWriter.write();
    }

    @Override
    public void stop() {
        // Flush to CSV file
        mWriter.flush();
    }

}
