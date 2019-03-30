package com.team175.robot.util.tuning;

import java.io.*;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An immutable CSV writer that writes all the content of a map containing data and a description (value and key) to a
 * CSV file.
 *
 * @author Arvind
 */
public final class CSVWriter {

    /**
     * A map used to hold data with a String description (for the header) and a Supplier (in order to use pass by
     * reference for methods).
     */
    private final Map<String, Supplier> mData;
    /**
     * The writer to write the map to a CSV file.
     */
    private final PrintWriter mWriter;
    /**
     * A specified delimiter for the CSV file.
     */
    private final String mDelimiter;

    /**
     * A boolean to store whether the header has been written.
     */
    private boolean mIsHeaderWritten;

    /**
     * Constructs a new CSVWriter.
     *
     * @param data
     *         The map of the data to write to the CSV file
     * @param filePath
     *         The path in which to write a CSV file
     * @param delimiter
     *         The specified delimiter of the CSV file
     * @throws FileNotFoundException
     */
    public CSVWriter(Map<String, Supplier> data, String filePath, String delimiter)
            throws FileNotFoundException {
        mData = data;
        mDelimiter = delimiter;
        mWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath, false)));

        mIsHeaderWritten = false;
    }

    /**
     * A helper method used to get a formatted String of the header from the keys of the map to write to the CSV file.
     * Only called once.
     *
     * @return A formatted string with the header
     */
    private String getHeader() {
        String header = "";
        boolean isFirst = true;

        for (String s : mData.keySet()) {
            // Don't add delimiter for first method
            if (isFirst) {
                header = s;
                isFirst = false;
            } else {
                header += mDelimiter + s;
            }
        }

        return header;
    }

    /**
     * A helper method used to get a formatted String of all the current content of the map to write to the CSV file.
     *
     * @return A string with the contents of the map
     */
    private String getProperties() {
        String properties = "";
        boolean isFirst = true;

        for (String s : mData.keySet()) {
            // Don't add delimiter for first method
            if (isFirst) {
                properties = mData.get(s).get().toString();
                isFirst = false;
            } else {
                properties += mDelimiter + mData.get(s).get();
            }
        }

        return properties;
    }

    /**
     * Writes all content of the map to the CSV file.
     */
    public void write() {
        if (!mIsHeaderWritten) {
            mWriter.println(getHeader());
            mIsHeaderWritten = true;
        }

        mWriter.println(getProperties());
    }

    /**
     * Flushes the writer's buffer to the CSV file.
     */
    public void flush() {
        mWriter.flush();
    }

    /**
     * Flushes all content of the CSV writer and disables the writer object (nullifies it).
     */
    public void close() {
        flush();
        mWriter.close();
    }

}
