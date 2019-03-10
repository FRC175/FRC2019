package com.team175.robot.commands.lift;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class ControlRearLift extends AldrinCommand {

    private LiftPosition mPosition;
    private boolean mIsManual;

    public ControlRearLift(LiftPosition position) {
        requires(Lift.getInstance(), Drive.getInstance());
        mPosition = position;
        mIsManual = false;
        super.logInstantiation();
    }

    public ControlRearLift() {
        requires(Lift.getInstance(), Drive.getInstance());
        mPosition = null;
        mIsManual = true;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().stop();
        super.initialize();
    }

    @Override
    protected void execute() {
        if (mIsManual) {
            Lift.getInstance().setRearPower(OI.getInstance().getDriverStickY());
        } else {
            Lift.getInstance().setRearPosition(mPosition);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().setRearPower(0);
        Lift.getInstance().setRearBrake(true);
        super.end();
    }

}
