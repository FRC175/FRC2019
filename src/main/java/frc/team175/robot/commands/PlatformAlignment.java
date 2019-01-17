package frc.team175.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team175.robot.subsystems.Breadboard.LineSensorPosition;
import frc.team175.robot.subsystems.Breadboard;

/**
 * @author Arvind
 */
public class PlatformAlignment extends Command {

    public PlatformAlignment() {
        requires(Breadboard.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Breadboard.getInstance().setCIMPower(0);
        Breadboard.getInstance().deployLateral(true);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Breadboard.getInstance().setCIMPosition(Breadboard.getInstance().getLineSensorPosition().positionToMove());
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Breadboard.getInstance().deployLateral(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

}