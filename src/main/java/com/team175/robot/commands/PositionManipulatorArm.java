package com.team175.robot.commands;

import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class PositionManipulatorArm extends LoggableCommand {

    private ManipulatorArmPosition mPosition;
    
    public PositionManipulatorArm(ManipulatorArmPosition position) {
        requires(Manipulator.getInstance());

        mPosition = position;
    }

    @Override
    protected void initialize() {
        mLogger.info("PositionManipulatorArm command initialized.");
    }

    @Override
    protected void execute() {
        Manipulator.getInstance().setArmPosition(mPosition);
    }

    @Override
    protected boolean isFinished() {
        return Manipulator.getInstance().isArmAtWantedPosition();
    }

    @Override
    protected void end() {
        mLogger.info("PositionManipulatorArm command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}
