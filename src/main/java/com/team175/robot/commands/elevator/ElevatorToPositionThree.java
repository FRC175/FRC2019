package com.team175.robot.commands.elevator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.positions.ManipulatorMode;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.Manipulator;

public class ElevatorToPositionThree extends AldrinCommand {

    public ElevatorToPositionThree() {
        requires(Elevator.getInstance(), Manipulator.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        ElevatorPosition position = ElevatorPosition.GROUND_PICKUP;

        switch (ManipulatorMode.getMode()) {
            case VELCRO_HATCH:
                position = ElevatorPosition.VELCRO_HATCH_LEVEL_THREE;
                break;
            case FINGER_HATCH:
                position = ElevatorPosition.FINGER_HATCH_LEVEL_THREE;
                break;
            case CARGO:
                position = ElevatorPosition.CARGO_LEVEL_THREE;
                break;
        }

        // Ensure elevator cannot move when manipulator is in stow position
        if (!Manipulator.getInstance().isArmAtPosition(ManipulatorArmPosition.STOW)) {
            mLogger.debug("Setting elevator to {} position.", position.toString());
            Elevator.getInstance().setPosition(position);
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
