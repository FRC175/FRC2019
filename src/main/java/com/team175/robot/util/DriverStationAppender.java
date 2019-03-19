package com.team175.robot.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * A logback appender to send error messages to the FRC DriverStation.
 *
 * @author Arvind
 */
public final class DriverStationAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        DriverStation.reportError(event.getFormattedMessage(), false);
    }

}