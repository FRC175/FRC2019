package com.team175.robot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.wpi.first.wpilibj.command.Command;

/**
 * TODO: Perhaps be more useful.
 *
 * @author Arvind
 */
public abstract class LoggableCommand extends Command {

    protected Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

}