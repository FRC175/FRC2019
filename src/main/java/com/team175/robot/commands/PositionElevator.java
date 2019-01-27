package com.team175.robot.commands;

import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.subsystems.Elevator;

/**
 * @author Arvind
 */
public class PositionElevator extends LoggableCommand {

    private ElevatorPosition mPosition;

    public PositionElevator(ElevatorPosition position) {
        requires(Elevator.getInstance());

        mPosition = position;
    }

    @Override
    protected void initialize() {
        mLogger.info("PositionElevator command initialized.");
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
        mLogger.info("PositionElevator command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}
