package com.team175.robot.util.tuning;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A generic, reflective, immutable csv file writer that writes all of an objects' public accessor methods to a file.
 * Deprecated due to the fact that this is an over-engineered solution to a simple problem.
 *
 * @param <T>
 *         The type whose properties are written.
 * @author Arvind
 */
@Deprecated
public final class GenericCSVWriter<T> {

    private final T mType;
    private final Class mClass;
    private final String mDelimiter;
    private final BufferedWriter mWriter;
    private final List<Method> mData;

    private boolean mIsHeaderWritten;

    public GenericCSVWriter(T type, String filePath, String delimiter) throws FileNotFoundException {
        mType = type;
        mClass = mType.getClass();
        mDelimiter = delimiter;
        mWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, false)));
        mData = getMethods();

        mIsHeaderWritten = false;
    }

    private boolean isMethodOverridden(Method m) {
        if (m.getDeclaringClass() == Object.class) {
            return true; // Does so in order to remove default methods
        }

        try {
            // Check if superclass contains the method
            mClass.getSuperclass().getDeclaredMethod(m.getName(), m.getParameterTypes());

            return true;
        } catch (NoSuchMethodException e) {
            // Check if any of the class' interface(s) contain(s) the method
            for (Class c : mClass.getInterfaces()) {
                try {
                    c.getDeclaredMethod(m.getName(), m.getParameterTypes());

                    return true;
                } catch (NoSuchMethodException nsme) {
                }
            }

            return false;
        }
    }

    private boolean isMethodAccessor(Method m) {
        return !m.getReturnType().equals(Void.TYPE);
    }

    private List<Method> getMethods() {
        return Stream.of(mClass.getMethods())
                .filter(this::isMethodAccessor)
                .filter(m -> !isMethodOverridden(m))
                .sorted(Comparator.comparing(Method::getName)) // Sort alphabetically
                .collect(Collectors.toList());
    }

    private String invokeMethod(Method m) throws InvocationTargetException, IllegalAccessException {
        if (Modifier.isPrivate(m.getModifiers())) {
            return null;
        } else if (Modifier.isStatic(m.getModifiers())) {
            return m.invoke(null).toString();
        } else {
            return m.invoke(mType).toString();
        }
    }

    public String getHeader() {
        String header = "";
        boolean isFirst = true;

        for (Method m : mData) {
            // Don't add delimiter for first method
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
        String body = "";
        boolean isFirst = true;

        for (Method m : mData) {
            // Don't add delimiter for first method
            if (isFirst) {
                body = invokeMethod(m);
                isFirst = false;
            } else {
                String s = invokeMethod(m);
                body = (s == null) ? body : body + mDelimiter + s;
            }
        }

        return body;
    }

    public void write() throws IOException, InvocationTargetException, IllegalAccessException {
        if (!mIsHeaderWritten) {
            mWriter.write(getHeader());
            mIsHeaderWritten = true;
        }

        mWriter.newLine();
        mWriter.write(getBody());
    }

    public void flush() throws IOException {
        mWriter.flush();
    }

    public void close() throws IOException {
        flush();
        mWriter.close();
    }

}