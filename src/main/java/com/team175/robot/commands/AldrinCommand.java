package com.team175.robot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.wpi.first.wpilibj.command.Command;

/**
 * @author Arvind
 */
public abstract class AldrinCommand extends Command implements LoggableCommand {

    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    public AldrinCommand() {
        super.setName(getClass().getSimpleName());
    }

    @Override
    public void logInstantiation() {
        mLogger.info("{} command instantiated.", super.getName());
    }

    @Override
    public void logInit() {
        mLogger.info("{} command initialized.", super.getName());
    }

    @Override
    public void logEnd() {
        mLogger.info("{} command ended/interrupted.", super.getName());
    }

}