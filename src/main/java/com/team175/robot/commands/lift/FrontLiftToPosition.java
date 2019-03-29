package com.team175.robot.commands.lift;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Lift;

public class FrontLiftToPosition extends AldrinCommand {

    private LiftPosition mPosition;

    public FrontLiftToPosition(LiftPosition position) {
        requires(Lift.getInstance());
        mPosition = position;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Lift.getInstance().setFrontPosition(mPosition);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        if (mPosition == LiftPosition.EXTEND) {
            return Lift.getInstance().isFrontForwardLimitHit();
        } else if (mPosition == LiftPosition.RETRACT) {
            return Lift.getInstance().isFrontReverseLimitHit();
        } else {
            return true;
        }
    }

    @Override
    protected void end() {
        // This may stop the rear while it's rising
        // Lift.getInstance().stop();
        Lift.getInstance().setFrontPower(0);
        Lift.getInstance().setFrontBrake(true);
        super.end();
    }

}
