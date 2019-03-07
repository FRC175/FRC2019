package com.team175.robot.util.tuning;

/**
 * @author Arvind
 */
public interface ClosedLoopTunable extends CSVWritable {

    void updateGains();

    void resetSensors();

}
