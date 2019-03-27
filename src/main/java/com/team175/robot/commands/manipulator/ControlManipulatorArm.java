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
    protected void initialize() {
        mLogger.debug("Starting arm position: {}", Manipulator.getInstance().getArmPosition());
    }

    @Override
    protected void execute() {
        Manipulator.getInstance().setArmPower(OI.getInstance().getOperatorStickX());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        mLogger.debug("Final arm position: {}", Manipulator.getInstance().getArmPosition());
        Manipulator.getInstance().stopArm();
        super.end();
    }

}
