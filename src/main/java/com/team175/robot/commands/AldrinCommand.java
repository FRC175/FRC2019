package com.team175.robot.commands;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.wpi.first.wpilibj.command.Command;

import java.util.List;

/**
 * @author Arvind
 */
public abstract class AldrinCommand extends Command implements LoggableCommand {

    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    public AldrinCommand() {
        super.setName(getClass().getSimpleName());
    }

    protected synchronized void requires(Subsystem... subsystems) {
        for (Subsystem s : subsystems) {
            super.requires(s);
        }
    }

    @Override
    public void logInstantiation() {
        mLogger.info("Command instantiated.");
    }

    @Override
    public void logInit() {
        mLogger.info("Command initialized.");
    }

    @Override
    public void logEnd() {
        mLogger.info("Command ended/interrupted.");
    }

    @Override
    protected void end() {
        logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}