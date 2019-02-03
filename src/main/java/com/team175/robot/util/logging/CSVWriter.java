package com.team175.robot.util.logging;

import java.io.*;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
final class CSVWriter {

    private final Map<String, DoubleSupplier> mData;
    private final String mDelimiter;
    private final BufferedWriter mWriter;

    private boolean mIsHeaderWritten;

    public CSVWriter(Map<String, DoubleSupplier> data, String filePath, String delimiter)
        throws FileNotFoundException {
        mData = data;
        mDelimiter = delimiter;
        mWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, false)));

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
            mWriter.write(getHeader());
            mIsHeaderWritten = true;
        }

        mWriter.newLine();
        mWriter.write(getProperties());
    }

    public void flush() throws IOException {
        mWriter.flush();
    }

    public void close() throws IOException {
        mWriter.flush();
        mWriter.close();
    }

}
