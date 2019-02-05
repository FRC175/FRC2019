package com.team175.robot.util.logging;

import java.util.LinkedHashMap;
import java.util.function.DoubleSupplier;

public interface CSVLoggable {

    LinkedHashMap<String, DoubleSupplier> getCSVProperties();

}
