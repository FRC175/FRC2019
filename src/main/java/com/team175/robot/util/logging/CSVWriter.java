package com.team175.robot.util.logging;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public final class CSVWriter {

    private final LinkedHashMap<String, DoubleSupplier> mData;
    private final String mDelimiter;
    private final PrintWriter mWriter;

    private boolean mIsHeaderWritten;

    public CSVWriter(LinkedHashMap<String, DoubleSupplier> data, String filePath, String delimiter)
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

    public void write() throws IOException {
        if (!mIsHeaderWritten) {
            mWriter.println(getHeader());
            mIsHeaderWritten = true;
        }

        mWriter.println(getProperties());
    }

    public void flush() throws IOException {
        mWriter.flush();
    }

    public void close() throws IOException {
        flush();
        mWriter.close();
    }

}
