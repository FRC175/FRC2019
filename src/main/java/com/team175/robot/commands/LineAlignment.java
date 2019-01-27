package com.team175.robot.commands;

import com.team175.robot.subsystems.LateralDrive;

/**
 * TODO: Update for lateral drive method calls.
 *
 * @author Arvind
 */
public class LineAlignment extends LoggableCommand {

    public LineAlignment() {
        // requires(Breadboard.getInstance());
        // requires(LateralDrive.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        // Breadboard.getInstance().setCIMPower(0);
        // Breadboard.getInstance().deployLateral(true);
        mLogger.info("LineAlignment command initialized.");
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        // Breadboard.getInstance().setCIMPosition(Breadboard.getInstance().getLineSensorPosition().positionToMove());
        // LateralDrive.getInstance().setMotionMagic(mPosition);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        // Breadboard.getInstance().deployLateral(false);
        mLogger.info("LineAlignment command ended/interrupted.");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

}