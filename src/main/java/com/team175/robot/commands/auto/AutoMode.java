package com.team175.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Determine if this subclass is needed.
 *
 * @author Arvind
 */
@Deprecated
public class AutoMode extends CommandGroup {

    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    // I don't know if this is how it works...
    // It could be addParallel() and then addSequential()
    public void addParallel(Command... commands) {
        for (Command c : commands) {
            super.addParallel(c);
        }
    }

    public void addParallel(double timeout, Command... commands) {
        for (Command c : commands) {
            super.addParallel(c, timeout);
        }
    }

}
