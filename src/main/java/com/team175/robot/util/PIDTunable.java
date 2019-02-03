package com.team175.robot.util;

import com.team175.robot.util.logging.CSVLoggable;

/**
 * @author Arvind
 */
public interface PIDTunable extends CSVLoggable {

    void updatePID();

}
