package com.team175.robot.util;

/**
 * @author Arvind
 */
public interface ClosedLoopTunable extends CSVWritable {

    void updateGains();

    void resetSensors();

}
