package com.team175.robot.subsystems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author Arvind
 */
public abstract class AldrinSubsystem extends Subsystem {

    // Logger
    protected Logger mLogger = LoggerFactory.getLogger(getClass());

    protected boolean mSubsystemState = true;

    // public abstract void onAuto();

    // public abstract void onDisabled();

    // public abstract void setSubsystemState();

    public abstract void onTeleop();

    /* Optional design patterns */
    public void onInit() {

    }

    public void onPeriodic() {

    }

    public void onDisabled() {

    }

}
