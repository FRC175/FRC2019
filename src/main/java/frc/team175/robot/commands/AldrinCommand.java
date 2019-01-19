package frc.team175.robot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.wpi.first.wpilibj.command.Command;
import frc.team175.robot.subsystems.Breadboard.LineSensorPosition;
import frc.team175.robot.subsystems.Breadboard;

/**
 * @author Arvind
 */
public abstract class AldrinCommand extends Command {

    protected Logger mLogger;

    {
        mLogger = LoggerFactory.getLogger(getClass());
    }

}