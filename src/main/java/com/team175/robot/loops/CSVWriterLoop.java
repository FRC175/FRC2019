package com.team175.robot.loops;

import com.team175.robot.util.tuning.CSVWriter;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.function.Supplier;

public final class CSVWriterLoop implements Loop {

    private final CSVWriter mCSVWriter;

    private static final String DELIMITER = ",";

    public CSVWriterLoop(Map<String, Supplier> data, String filePath) throws FileNotFoundException {
        mCSVWriter = new CSVWriter(data, filePath, DELIMITER);
    }

    @Override
    public void start() {
        // Write CSV header
        mCSVWriter.write();
    }

    @Override
    public void loop() {
        // Continuously write CSV body
        mCSVWriter.write();
    }

    @Override
    public void stop() {
        // Flush to CSV file
        mCSVWriter.flush();
    }

}
