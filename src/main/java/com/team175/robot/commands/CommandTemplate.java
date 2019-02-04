package com.team175.robot.commands;

/**
 * @author Arvind
 */
public class CommandTemplate extends AldrinCommand {

    public CommandTemplate() {
        // requires();

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        super.logInit();
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
        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
