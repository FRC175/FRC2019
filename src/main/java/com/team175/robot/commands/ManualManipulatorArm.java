package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ManualManipulatorArm extends AldrinCommand {

    private int mPosition;

    public ManualManipulatorArm() {
        requires(Manipulator.getInstance());

        mPosition = 0;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        super.logInit();
    }

    @Override
    protected void execute() {
        mLogger.debug("ManipulatorPosition: {}", Manipulator.getInstance().getArmPosition());
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
        // Manipulator.getInstance().setArmWantedPosition(mPosition);
        Manipulator.getInstance().stopArm();

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
