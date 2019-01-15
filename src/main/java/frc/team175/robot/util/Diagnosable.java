package frc.team175.robot.util;

/**
 * @author Arvind
 */
public interface Diagnosable {

    void outputToDashboard();

    void outputToCSV();

    void outputToLog();

    void outputToConsole();

    void outputToDriverStation();

    void setLoggingState();

    boolean checkSubsystem();
    
}
