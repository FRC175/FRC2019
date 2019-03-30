package com.team175.robot.commands.manipulator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.ElevatorPosition;
import com.team175.robot.positions.IntakeMode;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ManipulatorArmToPosition extends AldrinCommand {

    private ManipulatorArmPosition mPosition;
    
    public ManipulatorArmToPosition(ManipulatorArmPosition position) {
        requires(Manipulator.getInstance(), Elevator.getInstance());
        mPosition = position;
        super.logInstantiation();
    }

    public ManipulatorArmToPosition(boolean toScorePosition) {
        requires(Manipulator.getInstance(), Elevator.getInstance());
        switch (IntakeMode.getMode()) {
            case VELCRO_HATCH:
                mPosition = toScorePosition ? ManipulatorArmPosition.SCORE : ManipulatorArmPosition.VELCRO_HATCH_PICKUP;
                break;
            case FINGER_HATCH:
                mPosition = ManipulatorArmPosition.FINGER_HATCH_PICKUP;
                break;
            case CARGO:
                mPosition = toScorePosition ? ManipulatorArmPosition.SCORE : ManipulatorArmPosition.BALL_PICKUP;
                break;
        }
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        /*if (mPosition == ManipulatorArmPosition.VELCRO_HATCH_PICKUP || mPosition == ManipulatorArmPosition.BALL_PICKUP) {
            mLogger.debug("Bringing elevator down to above ground position to pickup hatch.");
            Elevator.getInstance().setPosition(ElevatorPosition.GROUND_PICKUP_ABOVE);
        }*/
        // Stow manipulator when going to stow, ball, or finger hatch pickup position
        if (mPosition != ManipulatorArmPosition.SCORE && mPosition != ManipulatorArmPosition.VELCRO_HATCH_PICKUP) {
            Manipulator.getInstance().deploy(false);
        }
        mLogger.debug("Setting arm to {} position.", mPosition.toString());
        Manipulator.getInstance().setArmPosition(mPosition);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return Manipulator.getInstance().isArmAtWantedPosition();
    }

    @Override
    protected void end() {
        // Deploy manipulator after reaching position
        if (mPosition == ManipulatorArmPosition.SCORE || mPosition == ManipulatorArmPosition.VELCRO_HATCH_PICKUP) {
            Manipulator.getInstance().deploy(true);
        }
        mLogger.debug("Final arm position: {}", Manipulator.getInstance().getArmPosition());
        Manipulator.getInstance().stopArm();
        super.end();
    }

}
