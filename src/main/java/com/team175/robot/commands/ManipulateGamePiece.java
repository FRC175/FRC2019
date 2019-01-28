package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.positions.ManipulatorRollerPosition;
import com.team175.robot.subsystems.Manipulator;

/**
 * @author Arvind
 */
public class ManipulateGamePiece extends LoggableCommand {

    private ManipulatorRollerPosition mPosition;

    public ManipulateGamePiece(ManipulatorRollerPosition position) {
        requires(Manipulator.getInstance());

        mPosition = position;
    }

    @Override
    protected void initialize() {
        mLogger.info("ManipulateGamePiece command initialized.");
    }

    @Override
    protected void execute() {
        // Manipulator.getInstance().setRollerPosition(mPosition);
        double power = OI.getInstance().getOperatorStickX();
        Manipulator.getInstance().setRollerPower(power, power);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Manipulator.getInstance().setRollerPosition(ManipulatorRollerPosition.IDLE);

        mLogger.info("ManipulateGamePiece command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}
