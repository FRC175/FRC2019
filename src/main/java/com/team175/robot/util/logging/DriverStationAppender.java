package com.team175.robot.util.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * A logback appender to send error messages to the FRC DriverStation.
 *
 * DEPRECATED due to the fact that the Driver Station can just read console messages.
 *
 * @author Arvind
 */
@Deprecated
public class DriverStationAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        DriverStation.reportError(event.getFormattedMessage(), false);
    }

}
