package com.team175.robot.commands.elevator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ElevatorToPosition extends AldrinCommand {

    private ElevatorPosition mPosition;

    public ElevatorToPosition(ElevatorPosition position) {
        requires(Elevator.getInstance());
        mPosition = position;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        // TODO: Check if-block
        // Ensure elevator cannot move when manipulator is stowed
        if (Manipulator.getInstance().isDeployed()) {
            Elevator.getInstance().setPosition(mPosition);
        }
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return Elevator.getInstance().isAtWantedPosition();
    }

    @Override
    protected void end() {
        Elevator.getInstance().stop();
        super.end();
    }

}