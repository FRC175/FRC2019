package com.team175.robot.util.tuning;

/**
 * @author Arvind
 */
public interface ClosedLoopTunable extends CSVLoggable {

    void updateGains();

    void reset();

}
