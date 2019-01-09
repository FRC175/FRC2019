package frc.robot.util;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author Arvind
 */
public abstract class BuzzSubsystem extends Subsystem {

    public abstract void onAuto();

    public abstract void onDisabled();

    public abstract void onTeleop();

    public abstract void setSubsystemState();

}
