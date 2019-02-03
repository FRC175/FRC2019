package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

/**
 * @author Arvind
 */
public class PositionLift extends AldrinCommand {

    private LiftPosition mPosition;

    public PositionLift(LiftPosition position) {
        requires(Lift.getInstance());
        requires(Drive.getInstance());

        mPosition = position;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setPower(0, 0);

        super.logInit();
    }

    @Override
    protected void execute() {
        // Lift.getInstance().setPosition(mPosition);
        Lift.getInstance().setLiftPower(OI.getInstance().getDriverStickY());
        // Lift.getInstance().setDrivePower(-1);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().setLiftPower(0);
        // Lift.getInstance().setDrivePower(0);

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
