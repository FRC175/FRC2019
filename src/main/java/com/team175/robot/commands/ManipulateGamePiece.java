package com.team175.robot.commands;

import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ManipulateGamePiece extends AldrinCommand {

    private ManipulatorRollerPosition mPosition;

    public ManipulateGamePiece(ManipulatorRollerPosition position) {
        requires(Manipulator.getInstance());

        mPosition = position;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        super.logInit();
    }

    @Override
    protected void execute() {
        Manipulator.getInstance().setRollerPosition(mPosition);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Manipulator.getInstance().stopRollers();

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
