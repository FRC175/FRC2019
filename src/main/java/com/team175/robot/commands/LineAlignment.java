package com.team175.robot.commands;

/**
 * TODO: Update for lateral drive method calls.
 *
 * @author Arvind
 */
public class LineAlignment extends AldrinCommand {

    public LineAlignment() {
        // requires(Breadboard.getInstance());
        // requires(LateralDrive.getInstance());

        super.initLog();
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        super.initLog();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        super.endLog();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

}