package com.team175.robot.commands;

import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.subsystems.Elevator;

/**
 * @author Arvind
 */
public class PositionElevator extends AldrinCommand {

    private ElevatorPosition mPosition;

    public PositionElevator(ElevatorPosition position) {
        requires(Elevator.getInstance());

        mPosition = position;

        super.instantiationLog();
    }

    @Override
    protected void initialize() {
        super.initLog();
    }

    @Override
    protected void execute() {
        Elevator.getInstance().setPosition(mPosition);
    }

    @Override
    protected boolean isFinished() {
        return Elevator.getInstance().isAtWantedPosition();
    }

    @Override
    protected void end() {
        super.endLog();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
