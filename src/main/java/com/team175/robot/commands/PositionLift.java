package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

/**
 * @author Arvind
 */
public class PositionLift extends AldrinCommand {

    private LiftPosition mFrontPosition;
    private LiftPosition mRearPosition;

    public PositionLift(LiftPosition frontPosition, LiftPosition rearPosition) {
        requires(Lift.getInstance(), Drive.getInstance());

        mFrontPosition = frontPosition;
        mRearPosition = rearPosition;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().stop();

        super.logInit();
    }

    @Override
    protected void execute() {
        Lift.getInstance().setFrontPosition(mFrontPosition);
        Lift.getInstance().setRearPosition(mRearPosition);
    }

    @Override
    protected boolean isFinished() {
        return Lift.getInstance().isFrontLimitHit() && Lift.getInstance().isRearLimitHit();
    }

    @Override
    protected void end() {
        Lift.getInstance().stop();

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
