package com.team175.robot.commands.manipulator;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ManipulateGamePieces extends AldrinCommand {

    private ManipulatorRollerPosition mPosition;

    public ManipulateGamePieces(ManipulatorRollerPosition position) {
        requires(Manipulator.getInstance());
        mPosition = position;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Manipulator.getInstance().setRollerPosition(mPosition);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Manipulator.getInstance().stopRollers();
        super.end();
    }

}
