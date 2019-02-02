package com.team175.robot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.wpi.first.wpilibj.command.Command;

/**
 * @author Arvind
 */
public abstract class AldrinCommand extends Command {

    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    public AldrinCommand() {
        super.setName(getClass().getSimpleName());
    }

    protected void instantiationLog() {
        mLogger.info("{} command instantiated.", super.getName());
    }

    protected void initLog() {
        mLogger.info("{} command initialized.", super.getName());
    }

    protected void endLog() {
        mLogger.info("{} command ended/interrupted.", super.getName());
    }

}