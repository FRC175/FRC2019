package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ManualManipulatorArm extends AldrinCommand {

    public ManualManipulatorArm() {
        requires(Manipulator.getInstance());

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        super.logInit();
    }

    @Override
    protected void execute() {
        Manipulator.getInstance().setArmPower(OI.getInstance().getOperatorStickX());
    }

    @Override
    protected boolean isFinished() {
        return Manipulator.getInstance().isArmAtWantedPosition();
    }

    @Override
    protected void end() {
        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
