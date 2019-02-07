package com.team175.robot.subsystems;

import com.team175.robot.util.Diagnosable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author Arvind
 */
public abstract class AldrinSubsystem extends Subsystem implements Diagnosable {

    /* Declarations */
    /**
     * A logger shared by various subsystems.
     */
    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * A simple method to call in order to stop all movement of a subsystem.
     */
    public abstract void stop();

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

    /**
     * An optional design pattern
     * @return Whether the subsystem is good
     */
    @Override
    public boolean check() {
        return true;
    }

    /**
     * A method called periodically by the Scheduler.
     */
    @Override
    public void periodic() {
        outputToDashboard();
    }

}
