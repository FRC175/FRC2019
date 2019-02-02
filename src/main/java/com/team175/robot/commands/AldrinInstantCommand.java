package com.team175.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AldrinInstantCommand extends InstantCommand {

    protected final Logger mLogger = LoggerFactory.getLogger(getClass().getSimpleName());

    public AldrinInstantCommand() {
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

    @Override
    protected void end() {
        endLog();
    }

    @Override
    protected void interrupted() {
        endLog();
    }

}
