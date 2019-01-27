package com.team175.robot.commands;

/**
 * @author Arvind
 */
public class CommandTemplate extends LoggableCommand {

    public CommandTemplate() {
        // requires();
    }

    @Override
    protected void initialize() {
        mLogger.info("CommandTemplate command initialized.");
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        mLogger.info("CommandTemplate command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}
