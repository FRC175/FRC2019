package com.team175.robot.commands.lift;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.Timer;

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
        /*if (!mIsManual) {
            if (mPosition == LiftPosition.RETRACT) {
                // Lift.getInstance().setFrontBrake(false);
                // Bring up and then disengage brake
                Lift.getInstance().setFrontPosition(LiftPosition.EXTEND);
                Timer.delay(0.175);
                Lift.getInstance().setFrontBrake(false);
                Lift.getInstance().setFrontPower(0);
            } else if (mPosition == LiftPosition.EXTEND) {
                Lift.getInstance().setFrontBrake(false);
            }
        }*/
        Lift.getInstance().setFrontBrake(false);
        /*if (!mIsManual) {
            if (mPosition == LiftPosition.EXTEND) {
                Drive.getInstance().setHighGear(true);
                Drive.getInstance().setPower(1);
            } else {
                Drive.getInstance().stop();
            }
        }*/

        if (!mIsManual) {
            Lift.getInstance().setFrontPosition(mPosition);
        }

        super.initialize();
    }

    @Override
    protected void execute() {
        if (mIsManual) {
            Lift.getInstance().setFrontPower(OI.getInstance().getDriverStickY());
        }

        if (Lift.getInstance().getFrontPosition() < -400) {
            Drive.getInstance().setHighGear(true);
            Drive.getInstance().setPower(0.75);
        } else {
            Drive.getInstance().setHighGear(false);
            Drive.getInstance().stop();
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
