package frc.team175.robot.subsystems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author Arvind
 */
public abstract class AldrinSubsystem extends Subsystem {

    // Logger
    protected Logger mLogger = LoggerFactory.getLogger(getClass()); 

    // public abstract void onAuto();

    // public abstract void onDisabled();

    public abstract void onTeleop();

    // public abstract void setSubsystemState();

}
