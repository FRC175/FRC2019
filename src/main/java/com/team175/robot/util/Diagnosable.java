package com.team175.robot.util;

import java.util.Map;

/**
 * @author Arvind
 */
public interface Diagnosable {

    boolean check();

    Map<String, Object> getTelemetry();

    void outputToDashboard();

    void updateFromDashboard();
    
}
