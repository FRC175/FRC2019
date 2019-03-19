package com.team175.robot.commands.manipulator;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ControlManipulatorArm extends AldrinCommand {

    public ControlManipulatorArm() {
        requires(Manipulator.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void execute() {
        // mLogger.debug("ManipulatorPosition: {}", Manipulator.getInstance().getArmPosition());
        Manipulator.getInstance().setArmPower(OI.getInstance().getOperatorStickX());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Manipulator.getInstance().stopArm();
        super.end();
    }

}
