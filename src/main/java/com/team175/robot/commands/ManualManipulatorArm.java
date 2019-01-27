package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ManualManipulatorArm extends LoggableCommand {

    public ManualManipulatorArm() {
        requires(Manipulator.getInstance());
    }

    @Override
    protected void initialize() {
        mLogger.info("ManualManipulatorArm command initialized.");
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
        mLogger.info("ManualManipulatorArm command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}
