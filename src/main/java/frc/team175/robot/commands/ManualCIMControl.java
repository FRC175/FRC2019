package frc.team175.robot.commands;

import frc.team175.robot.subsystems.Breadboard;
import frc.team175.robot.util.OI;

/**
 * @author Arvind
 */
public class ManualCIMControl extends AldrinCommand {

    public ManualCIMControl() {
        requires(Breadboard.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        mLogger.info("ManualCIMCommand command initialized.");
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Breadboard.getInstance().setCIMPower(OI.getInstance().getDriverStickY());
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Breadboard.getInstance().setCIMPower(0);
        mLogger.info("ManualCIMControl command ended/interrupted.");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

}