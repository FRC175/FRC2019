package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

/**
 * @author Arvind
 */
public class PositionLift extends LoggableCommand {

    private LiftPosition mPosition;

    public PositionLift(LiftPosition position) {
        requires(Lift.getInstance());
        requires(Drive.getInstance());

        mPosition = position;
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setPower(0, 0);

        mLogger.info("PositionLift command initialized.");
    }

    @Override
    protected void execute() {
        // Lift.getInstance().setPosition(mPosition);
        Lift.getInstance().setLiftPower(OI.getInstance().getDriverStickY());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().setLiftPower(0);

        mLogger.info("PositionLift command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}
