package com.team175.robot.commands;

import com.team175.robot.positions.ManipulatorArmPosition;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class PositionManipulatorArm extends AldrinCommand {

    private ManipulatorArmPosition mPosition;
    
    public PositionManipulatorArm(ManipulatorArmPosition position) {
        requires(Manipulator.getInstance());

        mPosition = position;

        super.instantiationLog();
    }

    @Override
    protected void initialize() {
        super.initLog();
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
        super.endLog();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
