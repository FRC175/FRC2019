package com.team175.robot.subsystems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author Arvind
 */
public abstract class AldrinSubsystem extends Subsystem {

    /* Declarations */
    // Logger
    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    // Boolean
    protected boolean mSubsystemState = true;

    // Optional design patterns
    // To be used if multithreaded periodic calls do not work
    public void onInit() {

    }

    public void onPeriodic() {

    }

    public void onDisabled() {

    }

    public void setSubsystemState(boolean enable) {
        mSubsystemState = enable;
    }

    /*public abstract void onAuto();
    public abstract void onDisabled();
    public abstract void onTeleop();*/

}
