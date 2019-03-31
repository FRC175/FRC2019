package com.team175.robot.commands.lift;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.Timer;

public class ControlLift extends AldrinCommand {

    private LiftPosition mPosition;
    private boolean mIsManual;

    public ControlLift(LiftPosition position) {
        requires(Lift.getInstance(), Drive.getInstance());
        mPosition = position;
        mIsManual = false;
        super.logInstantiation();
    }

    public ControlLift() {
        requires(Lift.getInstance(), Drive.getInstance());
        mPosition = null;
        mIsManual = true;
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().stop();
        if (!mIsManual) {
            if (mPosition == LiftPosition.RETRACT) {
                // Lift.getInstance().setFrontBrake(false);
                // Bring up and then disengage brake
                Lift.getInstance().setFrontPosition(LiftPosition.EXTEND);
                Lift.getInstance().setRearPosition(LiftPosition.EXTEND);
                Timer.delay(0.175);
                Lift.getInstance().setFrontBrake(false);
                Lift.getInstance().setRearBrake(false);
                Lift.getInstance().setFrontPower(0);
                Lift.getInstance().setFrontPower(0);
            } else if (mPosition == LiftPosition.EXTEND) {
                Lift.getInstance().setFrontBrake(false);
                Lift.getInstance().setRearBrake(false);
            }
        }
        super.initialize();
    }

    @Override
    protected void execute() {
        if (mIsManual) {
            Lift.getInstance().setFrontPower(OI.getInstance().getDriverStickY());
            Lift.getInstance().setRearPower(OI.getInstance().getDriverStickY());
        } else {
            Lift.getInstance().setFrontPosition(mPosition);
            Lift.getInstance().setRearPosition(mPosition);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().stop();
        super.end();
    }

}
