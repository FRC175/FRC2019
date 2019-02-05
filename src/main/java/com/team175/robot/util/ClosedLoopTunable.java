package com.team175.robot.util;

import com.team175.robot.util.logging.CSVLoggable;

/**
 * @author Arvind
 */
public interface ClosedLoopTunable extends CSVLoggable {

    void updatePIDF();

    void updateWantedPosition();

}
