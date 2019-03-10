package com.team175.robot.commands.elevator;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ControlElevator extends AldrinCommand {

    private int mPosition;

    public ControlElevator() {
        requires(Elevator.getInstance(), Manipulator.getInstance());
        mPosition = 0;
        super.logInstantiation();
    }

    @Override
    protected void execute() {
        // TODO: Check if-block
        // Ensure elevator cannot move when manipulator is stowed
        if (Manipulator.getInstance().isDeployed()) {
            // mLogger.debug("ElevatorPosition: {}", Elevator.getInstance().getPosition());
            Elevator.getInstance().setPower(OI.getInstance().getOperatorStickY());
            mPosition = Elevator.getInstance().getPosition();
        }
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        // Keep elevator at wanted position
        Elevator.getInstance().setWantedPosition(mPosition);
        Elevator.getInstance().stop();
        super.end();
    }

}
