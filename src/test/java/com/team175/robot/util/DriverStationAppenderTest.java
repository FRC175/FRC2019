package com.team175.robot.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverStationAppenderTest {

    @Test
    public void testAppender() {
        Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
        logger.error("This is a test of the DriverStation error appender!");
    }

}