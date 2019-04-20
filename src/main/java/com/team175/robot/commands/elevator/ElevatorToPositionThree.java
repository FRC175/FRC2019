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
        // Ensure elevator cannot move when manipulator is in stow position
        if (!Manipulator.getInstance().isArmAtPosition(ManipulatorArmPosition.STOW)) {
            mLogger.debug("Setting elevator to {} position.", ElevatorPosition.getPositionThree().toString());
            Elevator.getInstance().setPosition(ElevatorPosition.getPositionThree());
        }
        if (ElevatorPosition.getPositionThree() == ElevatorPosition.FINGER_HATCH_LEVEL_THREE) {
            mLogger.debug("Bringing manipulator to tilted finger hatch position.");
            Manipulator.getInstance().setArmPosition(ManipulatorArmPosition.FINGER_HATCH_TILT);
        }
        if (ElevatorPosition.getPositionThree() == ElevatorPosition.VELCRO_HATCH_LEVEL_THREE
                || ElevatorPosition.getPositionThree() == ElevatorPosition.CARGO_LEVEL_THREE) {
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
