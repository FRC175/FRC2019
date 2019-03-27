package com.team175.robot.commands.elevator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.positions.ManipulatorArmPosition;
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
        // Ensure elevator cannot move when manipulator is in stow position
        if (Manipulator.getInstance().isDeployed() && !Manipulator.getInstance().isArmAtPosition(ManipulatorArmPosition.STOW)) {
            mLogger.debug("Setting elevator to {} position.", mPosition.toString());
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
        mLogger.debug("Final elevator position: {}", Elevator.getInstance().getPosition());
        Elevator.getInstance().stop();
        super.end();
    }

}
