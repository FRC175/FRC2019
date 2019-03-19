package com.team175.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.Map;
import java.util.function.Supplier;

/**
 * TODO: Integrate loops
 *
 * @author Arvind
 */
public abstract class AldrinSubsystem extends Subsystem {

    /**
     * A logger shared by various subsystems.
     */
    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * Called in order to stop all movement of a subsystem.
     */
    public abstract void stop();

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
