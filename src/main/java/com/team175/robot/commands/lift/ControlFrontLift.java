package com.team175.robot.commands.lift;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class ControlFrontLift extends AldrinCommand {

    private LiftPosition mPosition;
    private boolean mIsManual;

    public ControlFrontLift(LiftPosition position) {
        requires(Lift.getInstance(), Drive.getInstance());
        mPosition = position;
        mIsManual = false;
        super.logInstantiation();
    }

    public ControlFrontLift() {
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
            Lift.getInstance().setFrontPower(OI.getInstance().getDriverStickY());
        } else {
            Lift.getInstance().setFrontPosition(mPosition);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().setFrontPower(0);
        Lift.getInstance().setFrontBrake(true);
        super.end();
    }

}
