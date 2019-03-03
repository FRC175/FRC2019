package com.team175.robot.commands;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvind
 * @see Command
 */
public abstract class AldrinCommand extends Command {

    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    public AldrinCommand() {
        super.setName(getClass().getSimpleName());
    }

    protected synchronized void requires(Subsystem... subsystems) {
        for (Subsystem s : subsystems) {
            super.requires(s);
        }
    }

    public void logInstantiation() {
        mLogger.info("Command instantiated.");
    }

    @Override
    protected void initialize() {
        mLogger.info("Command initialized.");
    }

    @Override
    protected void end() {
        mLogger.info("Command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}