package com.team175.robot.util;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CSVWriterTest {

    private CSVWriter mWriter;

    @Test
    public void testOutput() {
        Foo f = new Foo("Murph");

        try {
            mWriter = new CSVWriter(f.getCSVTelemetry(), "csv-test.csv", ",");
        } catch(FileNotFoundException e) {
            String pwd = System.getProperty("user.dir");
            System.out.println("PWD: " + pwd);

            e.printStackTrace();
        }

        // Run through writer 11 times (once for header)
        for (int i = 0; i < 10; i++) {
            mWriter.write();
        }

        mWriter.close();
    }

    // Just a random object that has really no purpose
    public class Foo implements CSVWritable {

        private String name;
        private int velocity;

        public Foo(String name) {
            this.name = name;
            velocity = -10;
        }

        public String getName() {
            return name;
        }

        public int getVelocity() {
            return velocity += 10;
        }

        @Override
        public Map<String, Supplier> getCSVTelemetry() {
            LinkedHashMap<String, Supplier> m = new LinkedHashMap<>();
            m.put("name", this::getName);
            m.put("velocity", this::getVelocity);
            return m;
        }

    }

}