package com.team175.robot.subsystems;

import com.team175.robot.loops.Loop;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Arvind
 */
public abstract class AldrinSubsystem extends Subsystem implements Loop {

    /**
     * A logger shared by various subsystems.
     */
    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * Returns a map with telemetry data of a subsystem.
     *
     * @return Map with telemetry data of subsystem
     */
    public abstract Map<String, Supplier> getTelemetry();

    /**
     * Filters the different types of data from the getTelemetry() map and sends it to the SmartDashboard.
     */
    public void outputToDashboard() {
        if (getTelemetry() != null) {
            getTelemetry().forEach((k, v) -> {
                if (v.get() instanceof Double || v.get() instanceof Integer) {
                    try {
                        SmartDashboard.putNumber(k, Double.parseDouble(v.get().toString()));
                    } catch (NumberFormatException e) {
                        mLogger.error("Failed to parse number to SmartDashboard!", e);
                    }
                } else if (v.get() instanceof Boolean) {
                    SmartDashboard.putBoolean(k, Boolean.parseBoolean(v.get().toString()));
                } else {
                    SmartDashboard.putString(k, v.get().toString());
                }
            });
        }
    }

    /**
     * Called to perform actions periodically.
     */
    @Override
    public void loop() {
        outputToDashboard();
    }

    /* Optional Design Patterns */

    /**
     * Checks whether subsystem is good to go.
     *
     * @return Whether the subsystem is in good shape
     */
    public boolean checkSubsystem() {
        return true;
    }

    /**
     * Called to perform actions periodically.
     */
    public void onPeriodic() {
        outputToDashboard();
    }

    /**
     * Updates subsystems with modified fields from the SmartDashboard.
     */
    public void updateFromDashboard() {
    }

    /**
     * Makes initDefaultCommand() optional.
     */
    @Override
    protected void initDefaultCommand() {
    }

}
