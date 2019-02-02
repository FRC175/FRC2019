package com.team175.robot.commands.deprecated;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.deprecated.Breadboard;

/**
 * @author Arvind
 */
@Deprecated
public class ManualNEOControl extends AldrinCommand {

    public ManualNEOControl() {
        requires(Breadboard.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }

}