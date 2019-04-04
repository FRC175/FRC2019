package com.team175.robot.commands.manipulator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ManipulatorArmToPosition extends AldrinCommand {

    private ManipulatorArmPosition mPosition;
    private boolean mIsScorePosition, mAuto;

    public ManipulatorArmToPosition(ManipulatorArmPosition position) {
        requires(Manipulator.getInstance(), Elevator.getInstance());
        mPosition = position;
        mAuto = false;
        super.logInstantiation();
    }

    /**
     * Creates ManipulatorArmToPosition command by determining position from Manipulator mode and isScorePosition.
     *
     * @param isScorePosition Whether to move the Manipulator to a score position
     */
    public ManipulatorArmToPosition(boolean isScorePosition) {
        requires(Manipulator.getInstance(), Elevator.getInstance());
        mIsScorePosition = isScorePosition;
        mPosition = ManipulatorArmPosition.SCORE;
        mAuto = true;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        if (mAuto) {
            // Automatically detect mode and choose appropriate action
            switch (Manipulator.getInstance().getMode()) {
                case VELCRO_HATCH:
                    mPosition = mIsScorePosition ? ManipulatorArmPosition.SCORE : ManipulatorArmPosition.VELCRO_HATCH_PICKUP;
                    break;
                case FINGER_HATCH:
                    mPosition = ManipulatorArmPosition.FINGER_HATCH_PICKUP;
                    break;
                case CARGO:
                    mPosition = mIsScorePosition ? ManipulatorArmPosition.SCORE : ManipulatorArmPosition.BALL_PICKUP;
                    break;
            }
        }

        /*if (mPosition == ManipulatorArmPosition.VELCRO_HATCH_PICKUP || mPosition == ManipulatorArmPosition.BALL_PICKUP) {
            mLogger.debug("Bringing elevator down to above ground position to pickup hatch.");
            Elevator.getInstance().setPosition(ElevatorPosition.GROUND_PICKUP_ABOVE);
        }*/
        // Stow manipulator when going to stow, ball, or finger hatch pickup position
        if (mPosition == ManipulatorArmPosition.STOW || mPosition == ManipulatorArmPosition.FINGER_HATCH_PICKUP) {
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
        if (mPosition != ManipulatorArmPosition.STOW && mPosition != ManipulatorArmPosition.FINGER_HATCH_PICKUP) {
            Manipulator.getInstance().deploy(true);
        }
        mLogger.debug("Final arm position: {}", Manipulator.getInstance().getArmPosition());
        Manipulator.getInstance().stopArm();
        super.end();
    }

}
