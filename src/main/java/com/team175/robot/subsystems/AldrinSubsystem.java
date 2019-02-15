package com.team175.robot.subsystems;

import com.team175.robot.util.Loggable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.Map;

/**
 * @author Arvind
 */
public abstract class AldrinSubsystem extends Subsystem implements Loggable {

    /* Declarations */
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
    public abstract Map<String, Object> getTelemetry();

    /**
     * Filters the different types of data from the getTelemetry() Map and sends it to the SmartDashboard.
     */
    @Override
    public void outputToDashboard() {
        if (getTelemetry() != null) {
            getTelemetry().forEach((k, v) -> {
                if (v instanceof Double || v instanceof Integer) {
                    try {
                        SmartDashboard.putNumber(k, Double.parseDouble(v.toString()));
                    } catch (NumberFormatException e) {
                        mLogger.error("Failed to parse number to SmartDashboard!", e);
                    }
                } else if (v instanceof Boolean) {
                    SmartDashboard.putBoolean(k, Boolean.parseBoolean(v.toString()));
                } else {
                    SmartDashboard.putString(k, v.toString());
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
     * A simple method to zero sensor values. For use in subsystems with closed loop control.
     */
    public void resetSensors() {
    }

    /**
     * Makes initDefaultCommand() optional.
     */
    @Override
    protected void initDefaultCommand() {
    }

}
