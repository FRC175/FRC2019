package com.team175.robot.util;

/**
 * This interface should be implemented in order to periodically log data to the smart dashboard, console, and log file.
 *
 * @author Arvind
 */
@Deprecated
public interface Loggable {

    void toDashboard();

    void toLog();

}
