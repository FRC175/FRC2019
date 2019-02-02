package com.team175.robot.util;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO: Determine if method is overridden and filter it out.
 *
 * A generic, reflective, immutable csv file writer.
 *
 * @param <T> The type whose properties are written.
 * @author Arvind
 */
public final class CSVWriter<T> {

    private final T mType;
    private final String mFilePath, mDelimiter;
    private final PrintWriter mWriter;
    private final List<Method> mData;

    private boolean mIsHeaderWritten;

    public CSVWriter(T type, String filePath, String delimiter) throws FileNotFoundException {
        mType = type;
        mFilePath = filePath;
        mDelimiter = delimiter;
        mWriter = new PrintWriter(new FileOutputStream(new File(mFilePath), false));
        mData = getAccessors();

        mIsHeaderWritten = false;
    }

    private boolean isMethodAccessor(Method m) {
        return !m.getReturnType().equals(Void.TYPE);
    }

    private List<Method> getAccessors() {
        return Stream.of(mType.getClass().getDeclaredMethods())
            .filter(this::isMethodAccessor)
            /*.filter(m -> {
                return !m.isAnnotationPresent(Override.class);
            })*/
            .collect(Collectors.toList());
    }

    private String invokeMethod(Method m) throws InvocationTargetException, IllegalAccessException {
        if (Modifier.isPrivate(m.getModifiers())) {
            m.setAccessible(true);
        }

        if (Modifier.isStatic(m.getModifiers())) {
            return m.invoke(null).toString();
        } else {
            return m.invoke(mType).toString();
        }
    }

    public String getHeader() {
        boolean isFirst = true;
        String header = "";

        for (Method m : mData) {
            if (isFirst) {
                header = m.getName();
                isFirst = false;
            } else {
                header += mDelimiter + m.getName();
            }
        }

        return header;
    }

    public String getBody() throws InvocationTargetException, IllegalAccessException {
        boolean isFirst = true;
        String body = "";

        for (Method m : mData) {
            if (isFirst) {
                body = invokeMethod(m);
                isFirst = false;
            } else {
                String s = invokeMethod(m);
                body += mDelimiter + invokeMethod(m);
            }
        }

        return body;
    }

    public void write() throws InvocationTargetException, IllegalAccessException {
        if (!mIsHeaderWritten) {
            mWriter.println(getHeader());
            mIsHeaderWritten = true;
        }

        mWriter.println(getBody());
    }

    public void flush() {
        mWriter.flush();
    }

    public void close() {
        mWriter.close();
        // mIsHeaderWritten = false;
    }

}
