package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ManualElevator extends AldrinCommand {

    private int mPosition;

    public ManualElevator() {
        requires(Elevator.getInstance(), Manipulator.getInstance());

        mPosition = 0;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        super.logInit();
    }

    @Override
    protected void execute() {
        // TODO: Check if-block
        if (!Manipulator.getInstance().isDeployed()) {
        }

        mLogger.debug("ElevatorPosition: {}", Elevator.getInstance().getPosition());
        Elevator.getInstance().setPower(OI.getInstance().getOperatorStickY() * 0.75);
        mPosition = Elevator.getInstance().getPosition();
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

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
