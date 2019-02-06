package com.team175.robot.util;

import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * @author Arvind
 */
public interface Diagnosable {

    boolean checkSubystem();

    Map<String, DoubleSupplier> getTelemetry();

    void outputToDashboard();

    void updateFromDashboard();
    
}
