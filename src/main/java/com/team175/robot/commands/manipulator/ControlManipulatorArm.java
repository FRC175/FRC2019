package com.team175.robot.commands.manipulator;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ControlManipulatorArm extends AldrinCommand {

    private int mPosition;

    public ControlManipulatorArm() {
        requires(Manipulator.getInstance());
        mPosition = 0;
        super.logInstantiation();
    }

    @Override
    protected void execute() {
        // mLogger.debug("ManipulatorPosition: {}", Manipulator.getInstance().getArmPosition());
        Manipulator.getInstance().setArmPower(OI.getInstance().getOperatorStickX());
        mPosition = Manipulator.getInstance().getArmPosition();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        // Keep manipulator at wanted position
        Manipulator.getInstance().setArmWantedPosition(mPosition);
        Manipulator.getInstance().stopArm();
        super.end();
    }

}
