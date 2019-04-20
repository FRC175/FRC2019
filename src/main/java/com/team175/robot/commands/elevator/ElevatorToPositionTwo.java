package com.team175.robot.commands.elevator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.positions.ManipulatorMode;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.Manipulator;

public class ElevatorToPositionTwo extends AldrinCommand {

    public ElevatorToPositionTwo() {
        requires(Elevator.getInstance(), Manipulator.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        // Ensure elevator cannot move when manipulator is in stow position
        if (!Manipulator.getInstance().isArmAtPosition(ManipulatorArmPosition.STOW)) {
            mLogger.debug("Setting elevator to {} position.", ElevatorPosition.getPositionTwo().toString());
            Elevator.getInstance().setPosition(ElevatorPosition.getPositionTwo());
        }
        if (ElevatorPosition.getPositionTwo() == ElevatorPosition.FINGER_HATCH_LEVEL_TWO) {
            mLogger.debug("Bringing manipulator to finger hatch pickup position.");
            Manipulator.getInstance().setArmPosition(ManipulatorArmPosition.FINGER_HATCH_PICKUP);
        }
        if (ElevatorPosition.getPositionTwo() == ElevatorPosition.VELCRO_HATCH_LEVEL_TWO) {
            mLogger.debug("Bringing manipulator to score position.");
            Manipulator.getInstance().setArmPosition(ManipulatorArmPosition.SCORE);
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
