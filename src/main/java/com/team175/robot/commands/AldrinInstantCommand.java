package com.team175.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AldrinInstantCommand extends InstantCommand implements LoggableCommand {

    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    public AldrinInstantCommand() {
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

    @Override
    protected void end() {
        logEnd();
    }

    @Override
    protected void interrupted() {
        logEnd();
    }

}
