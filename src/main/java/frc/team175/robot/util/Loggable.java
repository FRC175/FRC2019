package frc.team175.robot.util;

/**
 * @author Arvind
 */
public interface Loggable {

    void outputToDashboard();

    void outputToConsole();

    void outputToCSV();

    void setLoggingState();

}
