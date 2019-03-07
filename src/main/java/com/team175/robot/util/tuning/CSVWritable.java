package com.team175.robot.util.tuning;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public interface CSVWritable {

    Map<String, Supplier> getCSVTelemetry();

}
