/*
package com.team175.robot.commands.old;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

*/
/**
 * @author Arvind
 *//*

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
        boolean isFinished = true;

        if (mFrontPosition == LiftPosition.EXTEND) {
            isFinished &= Lift.getInstance().isFrontForwardLimitHit();
        } else if (mFrontPosition == LiftPosition.RETRACT) {
            isFinished &= Lift.getInstance().isFrontReverseLimitHit();
        }

        if (mRearPosition == LiftPosition.EXTEND) {
            isFinished &= Lift.getInstance().isRearForwardLimitHit();
        } else if (mRearPosition == LiftPosition.RETRACT) {
            isFinished &= Lift.getInstance().isRearReverseLimitHit();
        }

        return isFinished;
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
*/
