package com.team175.robot.util.logging;

import java.util.Map;
import java.util.function.DoubleSupplier;

public interface CSVLoggable {

    Map<String, DoubleSupplier> getCSVProperties();

}
