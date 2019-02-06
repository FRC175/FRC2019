package com.team175.robot.util.logging;

import java.io.*;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public final class CSVWriter {

    private final Map<String, DoubleSupplier> mData;
    private final PrintWriter mWriter;
    private final String mDelimiter;

    private boolean mIsHeaderWritten;

    public CSVWriter(Map<String, DoubleSupplier> data, String filePath, String delimiter)
        throws FileNotFoundException {
        mData = data;
        mDelimiter = delimiter;
        mWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath, false)));

        mIsHeaderWritten = false;
    }

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

    private String getProperties() {
        String properties = "";
        boolean isFirst = true;

        for (String s : mData.keySet()) {
            // Don't add delimiter for first method
            if (isFirst) {
                properties = Double.toString(mData.get(s).getAsDouble());
                isFirst = false;
            } else {
                properties += mDelimiter + mData.get(s).getAsDouble();
            }
        }

        return properties;
    }

    public void write() {
        if (!mIsHeaderWritten) {
            mWriter.println(getHeader());
            mIsHeaderWritten = true;
        }

        mWriter.println(getProperties());
    }

    public void flush() {
        mWriter.flush();
    }

    public void close() {
        flush();
        mWriter.close();
    }

}
