package com.team175.robot.commands.lift;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Lift;

public class RearLiftToPosition extends AldrinCommand {

    private LiftPosition mPosition;

    public RearLiftToPosition(LiftPosition position) {
        requires(Lift.getInstance());
        mPosition = position;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Lift.getInstance().setRearPosition(mPosition);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        if (mPosition == LiftPosition.EXTEND) {
            return Lift.getInstance().isRearForwardLimitHit();
        } else if (mPosition == LiftPosition.RETRACT) {
            return Lift.getInstance().isRearReverseLimitHit();
        } else {
            return true;
        }
    }

    @Override
    protected void end() {
        // This may stop the front while it's rising
        // Lift.getInstance().stop();
        Lift.getInstance().setRearPower(0);
        Lift.getInstance().setRearBrake(true);
        super.end();
    }

}
