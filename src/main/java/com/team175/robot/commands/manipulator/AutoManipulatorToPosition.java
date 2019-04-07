package com.team175.robot.commands.manipulator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.Elevator;
import com.team175.robot.subsystems.Manipulator;

public class AutoManipulatorToPosition extends AldrinCommand {

    private ManipulatorArmPosition mPosition;
    private boolean mIsScorePosition;

    public AutoManipulatorToPosition(boolean isScorePosition) {
        requires(Manipulator.getInstance(), Elevator.getInstance());
        mPosition = ManipulatorArmPosition.SCORE;
        mIsScorePosition = isScorePosition;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        // This cannot be placed in the constructor due to scheduler calling same command in OI.
        // The WPI Task Scheduler takes the command object associated with button and looks for the button to be called.
        // Basically, this makes it only construct the command once and thus not update parameters.
        mPosition = ManipulatorArmPosition.getHatchPosition(mIsScorePosition);

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
